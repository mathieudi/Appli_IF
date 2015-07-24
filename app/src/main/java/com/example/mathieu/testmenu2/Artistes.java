package com.example.mathieu.testmenu2;

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
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

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

/**
 * Created by mathieu on 03/06/15.
 */
public class Artistes extends Fragment{
    LinearLayout layout;
    JSONObject listeArtiste;

    Context cont;

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle args) {
        View view = inflater.inflate(R.layout.artistes, container, false);

        initView(view);

        return view;
    }

    @Override
    public void onStart(){
        super.onStart();
    }

    protected void initView(View view) {

        cont = getActivity().getBaseContext();

        layout = (LinearLayout) view.findViewById(R.id.liste_artistes);

        //Récupération connectivité portable
        //
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();


        // Vérification présence des données en locale
        //
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(cont);
        SharedPreferences.Editor editor = preferences.edit();
        String strJson = preferences.getString("liste_artistes", "0");

        if (!strJson.equals("0")){
            try {
                // Récupération des données sur l'appareil
                //
                listeArtiste = new JSONObject(strJson);

            } catch (JSONException e) {
            }
            showArtistes();
        }else {

            // Vérification disponibilité réseau
            //
            if (networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected()) {
                // Récupération des données via l'API de l'IF
                //
                DataWorkerTask task = new DataWorkerTask("http://www.imaginariumfestival.com/api/v1/artists/.json");

                task.execute();

            }
            else{
                //Mettre un message de réseau indisponible
                //
                TextView errorNetwork = new TextView(getActivity().getBaseContext());
                errorNetwork.setText("Connexion réseau indisponible, Réessayez !");
                errorNetwork.setPadding(0, 50, 0, 0);

                layout.addView(errorNetwork);

            }
        }

    }

    protected void showArtistes(){
        JSONArray ar = null;

        try {

            ar = listeArtiste.getJSONArray("artists");

            Log.d("artistes", listeArtiste.toString());

        } catch (JSONException e) {
        }

        if (ar != null){

            //Pour chaque artiste ajout d'un élément dans la liste
            //
            for (int i = 0; i < ar.length(); i++) {
                JSONObject artiste = null;
                try {

                    artiste =ar.getJSONObject(i);
                    addArtiste(artiste);

                }catch (JSONException e){}

            }
        }
    }

    protected void addArtiste(final JSONObject artiste) {
        elementArtiste artistLayout = new elementArtiste(getActivity().getBaseContext());

        // Création de l'élément artiste, mise en place des différentes informations
        //
        try {
            Bitmap bm = null;
            //Vérification présence de l'image en local
            //
            FileInputStream fis;
            try{
                String[] fileName = artiste.getString("picture").split("/");
                fis = getActivity().getBaseContext().openFileInput(fileName[fileName.length-1]);
                bm = BitmapFactory.decodeStream(fis);
                fis.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            //Si l'image a été trouvée en locale on la charge directement sinon on la récupère via le réseau si
            //
            if(bm != null) {
                artistLayout.image.setImageBitmap(bm);
            }else{
                PictureWorkerTask task = new PictureWorkerTask(artiste.getString("picture"));
                task.setLayout(artistLayout);
                task.execute();
            }



            artistLayout.nom.setText(artiste.getString("nickname"));
            artistLayout.nom.setPaintFlags(artistLayout.nom.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            artistLayout.type.setText("Type : " + artiste.getString("music_type"));
            artistLayout.description.setText(Html.fromHtml(artiste.getString("presentation")));

            // Création du bouton d'accès à la fiche détaillée de l'artiste
            //
            artistLayout.acceder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Passer en paramètre les infos de l'artiste
                    //
                    //
                    Intent newActivity = new Intent(getActivity(), ficheArtiste.class);
                    try {
                        newActivity.putExtra("aNom", artiste.getString("nickname"));
                        newActivity.putExtra("aType", artiste.getString("music_type"));
                        newActivity.putExtra("aStart", artiste.getJSONArray("concerts").getJSONObject(0).getString("start_at"));
                        newActivity.putExtra("aEnd", artiste.getJSONArray("concerts").getJSONObject(0).getString("end_at"));
                        newActivity.putExtra("aScene", artiste.getJSONArray("concerts").getJSONObject(0).getJSONObject("stage").getString("title"));
                        newActivity.putExtra("aPresentation", artiste.getString("presentation"));
                        newActivity.putExtra("aPicture", artiste.getString("picture"));
                        newActivity.putExtra("aFacebook", artiste.getString("facebook_link"));
                        newActivity.putExtra("aTwitter", artiste.getString("twitter_link"));
                        newActivity.putExtra("aSoundcloud", artiste.getString("soundcloud_link"));
                        //newActivity.putExtra("aYoutube", artiste.getString("youtube_videos_ids"));
                    } catch (JSONException e) {
                    }

                    startActivityForResult(newActivity, 0);
                }
            });

        }catch (JSONException e){}

        //Ajout de l'élément à la fenêtre
        //
        layout.addView(artistLayout);
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

    class DataWorkerTask extends AsyncTask<String, Void, String> {
        private String data = "";

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

                listeArtiste = new JSONObject(content);

                //Enregistrement des données en locales
                //
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(cont);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("liste_artistes", listeArtiste.toString());
                editor.commit();

            }catch(IOException e){
                e.printStackTrace();
            } catch(JSONException e){
                e.printStackTrace();
            }
            return "";
        }

        protected void onPostExecute(String res) {
            showArtistes();
        }
    }

    class PictureWorkerTask extends AsyncTask<String, Void, Bitmap> {
        private String data = "";
        elementArtiste layout;

        public PictureWorkerTask(String path) {
            data=path;
        }

        @Override
            protected Bitmap doInBackground(String... params) {
            Bitmap myBitmap = null;
            try {
                URL url = new URL(data);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                myBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeStream(input), 100, 130, true);

            }catch(IOException e){
                e.printStackTrace();
            }
            return myBitmap;
        }

        public void setLayout(elementArtiste element){
            layout = element;
        }

        protected void onPostExecute(Bitmap res) {
            if(res != null) {

                //Mise en place de la photo de l'artiste
                //
                layout.image.setImageBitmap(res);

                //Enregistrement de l'image en local
                //
                FileOutputStream fos;
                try{
                    String[] fileName = data.split("/");
                    fos = getActivity().getBaseContext().openFileOutput(fileName[fileName.length-1], Context.MODE_PRIVATE);
                    res.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                    fos.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
