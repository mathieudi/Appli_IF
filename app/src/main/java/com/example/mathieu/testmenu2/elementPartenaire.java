package com.example.mathieu.testmenu2;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by mathieu on 15/05/15.
 */
public class elementPartenaire extends LinearLayout {
    ImageView image;
    TextView texte;

    public elementPartenaire(Context context) {
        super(context);
        LayoutInflater li =(LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        li.inflate(R.layout.element_partenaire,this,true);

        image =(ImageView)findViewById(R.id.image_partenaire);
        texte =(TextView)findViewById(R.id.nom_partenaire);
    }
}
