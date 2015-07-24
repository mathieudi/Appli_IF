package com.example.mathieu.testmenu2;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by mathieu on 04/06/15.
 */
public class ficheArtiste extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fiche_artiste);

        final Intent intent = getIntent();

        PictureWorkerTask task = new PictureWorkerTask(intent.getStringExtra("aPicture"));
        task.execute();

        TextView nom = (TextView)findViewById(R.id.nom_fiche_artiste);
        nom.setText(intent.getStringExtra("aNom"));
        nom.setPaintFlags(nom.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        ((TextView)findViewById(R.id.type_fiche_artiste)).setText("Type : "+intent.getStringExtra("aType"));
        ((TextView)findViewById(R.id.passage_fiche_artiste)).setText("Début : "+intent.getStringExtra("aStart"));
        ((TextView)findViewById(R.id.fin_passage_fiche_artiste)).setText("Fin : "+intent.getStringExtra("aEnd"));
        ((TextView)findViewById(R.id.scene_fiche_artiste)).setText("Scène : "+intent.getStringExtra("aScene"));
        ((TextView)findViewById(R.id.description_fiche_artiste)).setText(Html.fromHtml(intent.getStringExtra("aPresentation")));

        //Demander d'autres photos via l'API
        //
        //((ImageView)findViewById(R.id.image_bas1_fiche_artiste)).setBackground();
        //((ImageView)findViewById(R.id.image_bas2_fiche_artiste)).setBackground();
        //((ImageView)findViewById(R.id.image_bas3_fiche_artiste)).setBackground();


        final String facebook = intent.getStringExtra("aFacebook");
        final String twitter = intent.getStringExtra("aTwitter");
        final String soundcloud = intent.getStringExtra("aSoundcloud");
        //String youtube = intent.getStringExtra("aYoutube");

        if(!facebook.equals("null")){
            ImageView fb = (ImageView)findViewById(R.id.facebook_fiche_artiste);
            fb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent accesFacebook = new Intent(Intent.ACTION_VIEW, Uri.parse(facebook));
                    startActivity(accesFacebook);
                }
            });
        }

        if(!twitter.equals("null")){
            ImageView tw = (ImageView)findViewById(R.id.twitter_fiche_artiste);
            tw.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent accesTwitter = new Intent(Intent.ACTION_VIEW, Uri.parse(twitter));
                    startActivity(accesTwitter);
                }
            });
        }

        if(!soundcloud.equals("null")){
            ImageView sc = (ImageView)findViewById(R.id.soundcloud_fiche_artiste);
            sc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent accesSoundcloud = new Intent(Intent.ACTION_VIEW, Uri.parse(soundcloud));
                    startActivity(accesSoundcloud);
                }
            });
        }

        //Youtube n'est plus présent dans les informations de l'artiste
        //
/*
        if(!youtube.equals("null")){
            final String idYoutube = youtube.split(",")[0];
            ImageView yt = (ImageView)findViewById(R.id.youtube_fiche_artiste);
            yt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent accesYoutube = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v="+idYoutube));
                    startActivity(accesYoutube);
                }
            });
        }*/
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

        protected void onPostExecute(Bitmap res) {
            if(res != null) {
                ((ImageView) findViewById(R.id.image_fiche_artiste)).setImageBitmap(res);
                ((ImageView) findViewById(R.id.image_bas1_fiche_artiste)).setImageBitmap(res);
                ((ImageView) findViewById(R.id.image_bas2_fiche_artiste)).setImageBitmap(res);
                ((ImageView) findViewById(R.id.image_bas3_fiche_artiste)).setImageBitmap(res);
            }
        }
    }
}
