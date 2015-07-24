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
import java.util.List;


public class Reglement extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reglement);

        LinearLayout layout = (LinearLayout)findViewById(R.id.reglementLayout); //Récupération du conteneur dans lequel ajouter les partenaires

        elementReglement e;

        //Récupération liste des partenaires
        Resources res = getResources();
        TypedArray ar = res.obtainTypedArray(R.array.liste_reglements);

        int n = ar.length();
        for (int i = 0; i < n; ++i)
        {
            //Pour chaque partenaire récupération du nom et de la source
            int resId = ar.getResourceId(i, 0);
            String key = res.getResourceEntryName(resId);
            List<String> values = Arrays.asList(res.getStringArray(resId));

            e= new elementReglement(this);

            //Configuration du composant et ajout dans le LinearLayout
            //e.image.setBackgroundResource(ident);
            e.titre.setText(values.get(0));
            e.texte.setText(values.get(1));

            layout.addView(e);

        }

        ar.recycle();

        //Ajout du listener sur le bouton retour
        Button retour = (Button) findViewById(R.id.reglement_retour);

        retour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
