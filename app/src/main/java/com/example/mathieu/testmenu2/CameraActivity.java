package com.example.mathieu.testmenu2;

import android.app.Activity;
import android.app.Fragment;
import android.content.ContentValues;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.provider.MediaStore.Images.Media;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by mathieu on 23/05/15.
 */
@SuppressWarnings("deprecation")
public class CameraActivity extends Fragment implements SurfaceHolder.Callback {
    private Camera camera;
    private SurfaceView surfaceCamera;
    private Boolean isPreview;

    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle args) {
        View view = inflater.inflate(R.layout.camera, container, false);

        initVue(view);

        return view;
    }

    @Override
    public void onStart(){
        super.onStart();
    }

    public void initVue(View view) {

        // Pour que l'activité soit en plein écran
        //
        getActivity().getWindow().setFormat(PixelFormat.TRANSLUCENT);
        //getActivity().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        isPreview = false;

        // On récupère notre surfaceView ou on mettra la camera
        //
        surfaceCamera = (SurfaceView) view.findViewById(R.id.surfaceViewCamera);

        Button photo = (Button) view.findViewById(R.id.photo);
        Button gallerie = (Button) view.findViewById(R.id.gallerie);

        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SavePicture();
            }
        });

        gallerie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nouvelleActivite = new Intent(getActivity(), Calques.class);

                startActivity(nouvelleActivite);
            }
        });

        // Et on initialise la caméra
        //
        InitializeCamera();
    }

    public void InitializeCamera() {

        // On attache le retour de la surfaceView à notre activité
        //
        surfaceCamera.getHolder().addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        // On ouvre et on récupère la camera
        //
       if (camera == null)
            camera = Camera.open();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        // Si le mode preview est lancé on l'arrête
        //
        if (isPreview) {
            camera.stopPreview();
        }

        // On récupère les paramètres de la caméra
        //
        Camera.Parameters parameters = camera.getParameters();

        //On récupère les tailles supportés par le téléphone
        //
        List<Camera.Size> sizes = parameters.getSupportedPreviewSizes();

        int max = 0;
        int tmp = 0;
        for (int i=0; i<sizes.size(); i++){

            if(sizes.get(i).width>tmp) {
                tmp = sizes.get(i).width;
                max = i;
            }
            Log.d("sizeEcran", ""+sizes.get(i).height+"    "+sizes.get(i).width);
        }

        Camera.Size cs = sizes.get(max);
        Log.d("sizeEcranMax", ""+sizes.get(max).height+"    "+sizes.get(max).width);

        // On lui affecte la taille de l'écran
        //
        parameters.setPreviewSize(cs.width, cs.height);

        // On lui applique les paramètres
        //
        camera.setParameters(parameters);

        camera.setDisplayOrientation(90);

        try {

            // On attache la camera à la Surfaceview
            //
            camera.setPreviewDisplay(surfaceCamera.getHolder());
        } catch (IOException e) {
        }

        // Nous lançons la preview
        //
        camera.startPreview();

        isPreview = true;
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

        // On arrête la caméra
        //
        if (camera != null) {
            camera.stopPreview();
            isPreview = false;

            //Et on libère le péréphérique
            //
            camera.release();
        }
    }

    // Callback pour la prise de photo
    Camera.PictureCallback pictureCallback = new Camera.PictureCallback() {

        public void onPictureTaken(byte[] data, Camera camera) {
            if (data != null) {

                // Redémarage la prévisualisation
                //
                camera.startPreview();

                //Récupération de l'image sous forme de Bitmap
                //
                Bitmap image =  BitmapFactory.decodeByteArray(
                        data, 0, data.length);

                // Rotation de l'image
                //
                Matrix mtx = new Matrix();
                mtx.preRotate(90);
                image = Bitmap.createBitmap(image, 0, 0, image.getWidth(), image.getHeight(), mtx, false);

                saveToInternalSorage(image);

            }
        }
    };

    private void saveToInternalSorage(Bitmap bitmapImage){
        ContextWrapper cw = new ContextWrapper(getActivity().getApplicationContext());

        //Création du nom du fichier
        //
        SimpleDateFormat timeStampFormat = new SimpleDateFormat(
                "yyyy-MM-dd-HH.mm.ss");
        String name = "photoIF_" + timeStampFormat.format(new Date())
                + ".jpg";

        // Metadata pour la photo
        //
        ContentValues values = new ContentValues();
        values.put(Media.TITLE, name);
        values.put(Media.DISPLAY_NAME, name);
        values.put(Media.DESCRIPTION, "Image prise depuis l'application IF");
        values.put(Media.DATE_TAKEN, new Date().getTime());
        values.put(Media.MIME_TYPE, "image/jpeg");

        // Support de stockage
        //
        Uri taken = cw.getContentResolver().insert(Media.EXTERNAL_CONTENT_URI,
                values);

        FileOutputStream fos = null;
        try {

            //Création du flux de sortie précédemment configuré
            //
            fos = (FileOutputStream) cw.getContentResolver().openOutputStream(
                    taken);

            // Ecriture de l'image sur l'OutputStream
            //
            bitmapImage.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void SavePicture() {
        try {
            camera.takePicture(null, pictureCallback, pictureCallback);
        } catch (Exception e) {
            // TODO: handle exception
        }

    }

    // Mise en pause de l'application
    @Override
    public void onPause() {
        super.onPause();

        if (camera != null) {
            camera.release();
            camera = null;
        }
    }

    // Retour sur l'application
    @Override
    public void onResume() {
        super.onResume();
        if(camera == null)
            camera = Camera.open();
    }

}
