package com.example.mathieu.testmenu2;

import android.app.Activity;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.*;
import android.view.View;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Partenaires extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.partenaires); //Mise en place du conteneur principal

        LinearLayout layout = (LinearLayout)findViewById(R.id.partenaireLayout); //Récupération du conteneur dans lequel ajouter les partenaires

        elementPartenaire e;

        //Récupération liste des partenaires
        Resources res = getResources();
        TypedArray  ar = res.obtainTypedArray(R.array.liste_partenaires);

        int n = ar.length();
        for (int i = 0; i < n; ++i)
        {
            //Pour chaque partenaire récupération du nom et de la source
            int resId = ar.getResourceId(i, 0);
            String key = res.getResourceEntryName(resId);
            List<String> values = Arrays.asList(res.getStringArray(resId));

            //Récupération de la ressource image
            int ident = getResources().getIdentifier(values.get(1), "drawable", getPackageName());
            Bitmap bm;
            bm = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(res, ident), 300, 200, true);


            e= new elementPartenaire(this);

            //Configuration du composant et ajout dans le LinearLayout
            //e.image.setBackgroundResource(ident);
            e.image.setImageBitmap(bm);
            e.texte.setText(values.get(0));

            layout.addView(e);

        }

        ar.recycle();

        Button retour = (Button) findViewById(R.id.partenaires_retour);

        retour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
