package com.example.mathieu.testmenu2;

import android.app.Fragment;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.*;
import android.view.View;

import java.util.Arrays;
import java.util.List;


public class InfosPratiques extends Fragment {
    elementInfos[] e;

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle args) {
        View view = inflater.inflate(R.layout.infos_pratiques, container, false);

        initVue(view);

        return view;
    }

    @Override
    public void onStart(){
        super.onStart();
    }

    private void initVue(View view){
        LinearLayout layout = (LinearLayout)view.findViewById(R.id.menu_infos); //Récupération du conteneur dans lequel ajouter les partenaires

        //Récupération liste des partenaires
        Resources res = getResources();
        TypedArray ar = res.obtainTypedArray(R.array.liste_infos);

        int n = ar.length();

        e = new elementInfos[n];

        for (int i = 0; i < n; ++i)
        {
            //Pour chaque partenaire récupération du nom et de la source
            int resId = ar.getResourceId(i, 0);
            List<String> values = Arrays.asList(res.getStringArray(resId));


            //Création du composant
            e[i]= new elementInfos(this.getActivity().getBaseContext());

            //Configuration du composant et ajout dans le LinearLayout
            //
            //Ajout du texte
            e[i].texte.setText(values.get(0));

            //Selection de l'activité à lancer selon le bouton

            switch (i) {
                case 0:
                    e[i].bouton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent nouvelleActivite = new Intent(getActivity(), PresentationAsso.class);

                            startActivity(nouvelleActivite);
                        }
                    });
                    break;
                case 1:
                    e[i].bouton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent nouvelleActivite = new Intent(getActivity(), Billeterie.class);

                            startActivity(nouvelleActivite);
                        }
                    });
                    break;
                case 2:
                    e[i].bouton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent nouvelleActivite = new Intent(getActivity(), Reglement.class);

                            startActivity(nouvelleActivite);
                        }
                    });
                    break;
                case 3:
                    e[i].bouton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent nouvelleActivite = new Intent(getActivity(), Partenaires.class);

                            startActivity(nouvelleActivite);
                        }
                    });
                    break;
            }

            //Ajout du composant
            layout.addView(e[i]);

        }

        ar.recycle();
    }
}
