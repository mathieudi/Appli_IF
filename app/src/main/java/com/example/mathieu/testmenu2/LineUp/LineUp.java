package com.example.mathieu.testmenu2.LineUp;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mathieu.testmenu2.R;
import com.example.mathieu.testmenu2.elementArtiste;
import com.example.mathieu.testmenu2.ficheArtiste;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Calendar;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by Mathieu on 18/09/2015.
 */
public class LineUp extends Fragment{
    LinearLayout _layout;
    List<Show> _showListD1;
    List<Show> _showListD2;
    Context _cont;

    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle args) {
        View view = inflater.inflate(R.layout.lineup_grid, container, false);

        initView(view);

        return view;
    }

    @Override
    public void onStart(){
        super.onStart();
    }

    protected void initView(View view) {

        _cont = getActivity().getBaseContext();

        _layout = (LinearLayout) view.findViewById(R.id.lineup);

        //Récupération connectivité portable
        //
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();


        // Vérification présence des données en locale
        //
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(_cont);
        SharedPreferences.Editor editor = preferences.edit();
        String strJson = preferences.getString("list_shows", "0");

        JSONObject showList;

        if (!strJson.equals("0")){
            try {
                // Récupération des données sur l'appareil
                //
                showList = new JSONObject(strJson);

                createShows(showList);

            } catch (JSONException e) {
            }
        }else {

            // Vérification disponibilité réseau
            //
            if (networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected()) {
                // Récupération des données via l'API de l'IF
                //
                DataWorkerTask task = new DataWorkerTask("http://www.imaginariumfestival.com/api/v1/concerts/.json");

                task.execute();

            }
            else{
                //Mettre un message de réseau indisponible
                //
                TextView errorNetwork = new TextView(getActivity().getBaseContext());
                errorNetwork.setText("Connexion réseau indisponible, Réessayez !");
                errorNetwork.setPadding(0, 50, 0, 0);

                _layout.addView(errorNetwork);

            }
        }



    }

    protected void createShows(final JSONObject showList) {

        JSONArray ar = null;

        try {

            ar = showList.getJSONArray("lineup");

            Log.d("concerts", showList.toString());

            //Récuparation des concerts des deux jours dans des listes distinctes
            //
            if (ar != null) {
                fillList(_showListD1, ar.getJSONObject(0).getJSONArray("concerts"));
                fillList(_showListD2, ar.getJSONObject(1).getJSONArray("concerts"));
            }

        } catch (JSONException e) {
        }




    }

    public void fillList(List<Show> listToFill, JSONArray data) {

        if (data == null) return;

        for (int i = 0; i < data.length(); i++) {
            try {
                JSONObject show = data.getJSONObject(i);

                //Récupération de la date au bon format
                //
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
                Date start = new Date();
                Date end = new Date();
                try {
                    start = dateFormat.parse(show.getString("start_at"));
                    end = dateFormat.parse(show.getString("end_at"));

                } catch (ParseException e) {

                }

                //Création du concert
                //
                Show showToAdd = new Show(
                        start,
                        end,
                        show.getJSONObject("artist").getString("nickname"),
                        show.getJSONObject("artist").getString("picture"),
                        show.getJSONObject("stage").getString("title"),
                        _cont);

                //Ajout du concert à la liste
                //
                listToFill.add(showToAdd);

            } catch (JSONException e) {
            }

        }
    }

    class DataWorkerTask extends AsyncTask<String, Void, String> {
        private String data = "";
        JSONObject showList;

        public DataWorkerTask(String path) {
            data=path;
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                // Envoie de la requête et récupération de la réponse
                //
                URLConnection connection = new URL(data).openConnection();
                InputStream response = connection.getInputStream();
                String content = getStringFromInputStream(response);

                showList = new JSONObject(content);

                //Enregistrement des données en locales
                //
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(_cont);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("list_shows", showList.toString());
                editor.commit();

            }catch(IOException e){
                e.printStackTrace();
            } catch(JSONException e){
                e.printStackTrace();
            }
            return "";
        }

        protected void onPostExecute(String res) {
            createShows(showList);
        }
    }

    private static String getStringFromInputStream(InputStream is) {
        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();

        String line;
        try {

            br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return sb.toString();

    }
}
