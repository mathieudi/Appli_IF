package com.example.mathieu.testmenu2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by mathieu on 15/05/15.
 */
public class elementArtiste extends LinearLayout {
    ImageView image;
    TextView nom;
    TextView type;
    TextView description;
    Button acceder;

    public elementArtiste(Context context) {
        super(context);
        LayoutInflater li =(LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        li.inflate(R.layout.element_artiste, this, true);

        image = (ImageView) findViewById(R.id.image_artiste);
        nom = (TextView) findViewById(R.id.nom_artiste);
        type = (TextView) findViewById(R.id.type_artiste);
        description = (TextView) findViewById(R.id.description_artiste);

        acceder = (Button) findViewById(R.id.bouton_artiste);

    }
}
