package com.example.mathieu.testmenu2.LineUp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

/**
 * Created by Mathieu on 18/09/2015.
 */
public class Show {
    Date _start;
    Date _end;
    String _artist;
    Bitmap _picture;
    String _stage;
    Context _cont;

    public Show(Date start, Date end, String artist, String picture, String stage, Context cont) {
        _start = start;
        _end = end;
        _artist = artist;
        _stage = stage;
        _cont = cont;

        Bitmap bm = null;
        //Vérification présence de l'image en local
        //
        FileInputStream fis;
        try {
            String[] fileName = picture.split("/");
            fis = _cont.openFileInput(fileName[fileName.length - 1]);
            bm = BitmapFactory.decodeStream(fis);
            fis.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Si l'image a été trouvée en locale on la charge directement sinon on la récupère via le réseau si
        //
        if (bm != null) {
            _picture = bm;
        } else {
            PictureWorkerTask task = new PictureWorkerTask(picture);
            task.execute();
        }
    }


    class PictureWorkerTask extends AsyncTask<String, Void, Bitmap> {
        private String data = "";

        public PictureWorkerTask(String path) {
            data = path;
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
                myBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeStream(input), 50, 65, true);

            } catch (IOException e) {
                e.printStackTrace();
            }
            return myBitmap;
        }

        protected void onPostExecute(Bitmap res) {
            //Enregistrement de l'image en local
            //
            FileOutputStream fos;
            try{
                String[] fileName = data.split("/");
                fos = _cont.openFileOutput(fileName[fileName.length-1], Context.MODE_PRIVATE);
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