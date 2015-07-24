package com.example.mathieu.testmenu2;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class Calques extends Activity {

    //Image selectionnée
    //
    Bitmap picture;
    Bitmap newPicture;
    TypedArray calqueArray;
    int currentCalque;
    ImageView imgView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gallerie);

        Button retour = (Button) findViewById(R.id.calques_retour);

        retour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calqueArray.recycle();
                finish();
            }
        });

        Button select = (Button) findViewById(R.id.calques_photo_select);

        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPicture();
            }
        });

        Button save = (Button) findViewById(R.id.calques_photo_save);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savePicture();
            }
        });

        Button right = (Button) findViewById(R.id.calques_right);

        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentCalque == calqueArray.length()-1)
                    currentCalque = 0;
                else
                    currentCalque++;

                overlay();
            }
        });

        Button left = (Button) findViewById(R.id.calques_left);

        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentCalque == 0)
                    currentCalque = calqueArray.length()-1;
                else
                    currentCalque--;

                overlay();
            }
        });

        //Ouvre la fenêtre de sélection et récupére la photo choisie
        //
        selectPicture();

        //Récupération des différents calques dans les ressources
        //
        calqueArray = getResources().obtainTypedArray(R.array.liste_calques);
    }

    public void selectPicture(){
        // Création de l'intent pour la gallerie photo
        //
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        galleryIntent.putExtra("activity", "selectPicture");

        // Lancement de l'intent
        //
        startActivityForResult(galleryIntent, 1);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {

            // Lorsque l'image est sélectionnée
            //
            if (requestCode == 1 && resultCode == RESULT_OK && data != null) {

                //On récupère l'image
                //
                Uri selectedImage = data.getData();
                String[] filePathColumn = { MediaStore.Images.Media.DATA };

                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String imgDecodableString = cursor.getString(columnIndex);
                cursor.close();

                //On récupère le conteneur de l'image
                //
                imgView = (ImageView) findViewById(R.id.image1);

                //On ajoute l'image sélectionnée à l'imageView
                //
                picture = decodeFile(imgDecodableString);
                imgView.setImageBitmap(picture);

            } else {
                Toast.makeText(this, "Aucune Image n'a été séectionnée",
                        Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "La sélection a échoué", Toast.LENGTH_LONG)
                    .show();
        }

        currentCalque = 0;

        overlay();
    }


    private void overlay() {

        //Récupération du calque correspondant à l'index currentCalque
        //
        int resId = calqueArray.getResourceId(currentCalque, 0);
        List<String> values = Arrays.asList(getResources().getStringArray(resId));

        //Récupération de la ressource image
        //
        int ident = getResources().getIdentifier(values.get(0), "drawable", getPackageName());
        Bitmap bm = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), ident), picture.getWidth(), picture.getHeight(), true);

        //Création du Bitmap pour la superposition
        //
        newPicture = Bitmap.createBitmap(picture.getWidth(), picture.getHeight(), picture.getConfig());

        //Mise en place de la superposition
        //
        Canvas canvas = new Canvas(newPicture);
        canvas.drawBitmap(picture, new Matrix(), null);
        canvas.drawBitmap(bm, new Matrix(), null);

        //Visualisation du résultat
        //
        imgView.setImageBitmap(newPicture);
    }



    public void savePicture (){
        //Création du nom du fichier
        //
        SimpleDateFormat timeStampFormat = new SimpleDateFormat(
                "yyyy-MM-dd-HH.mm.ss");
        String name = "photoIF_" + timeStampFormat.format(new Date())
                + ".jpg";

        // Metadata pour la photo
        //
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, name);
        values.put(MediaStore.Images.Media.DISPLAY_NAME, name);
        values.put(MediaStore.Images.Media.DESCRIPTION, "Image modifiée sur l'appli IF");
        values.put(MediaStore.Images.Media.DATE_TAKEN, new Date().getTime());
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");

        // Support de stockage
        //
        Uri taken = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                values);

        FileOutputStream fos = null;
        try {

            //Création du flux de sortie précédemment configuré
            //
            fos = (FileOutputStream) getContentResolver().openOutputStream(
                    taken);

            // Ecriture de l'image sur l'OutputStream
            //
            newPicture.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Decodes image and scales it to reduce memory consumption
        private Bitmap decodeFile(String imagePath) {
            // Decode image size
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imagePath, o);
        //BitmapFactory.decodeStream(new FileInputStream(f), null, o);

        // The new size we want to scale to
        final int REQUIRED_SIZE=720;

        // Find the correct scale value. It should be the power of 2.
        int scale = 1;
        while(o.outWidth / scale / 2 >= REQUIRED_SIZE &&
                o.outHeight / scale / 2 >= REQUIRED_SIZE) {
            scale *= 2;
        }

        // Decode with inSampleSize
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;

        Bitmap bitm = BitmapFactory.decodeFile(imagePath, o2);
        //Bitmap bitm = BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
        if(o.outWidth>o.outHeight) {
            Matrix matrix = new Matrix();
            matrix.postRotate(90);
            return Bitmap.createBitmap(bitm, 0, 0, bitm.getWidth(), bitm.getHeight(), matrix, true);
        }else {
            return bitm;
        }
    }

}