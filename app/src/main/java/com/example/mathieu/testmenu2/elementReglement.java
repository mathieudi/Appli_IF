package com.example.mathieu.testmenu2;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by mathieu on 22/05/15.
 */
public class elementReglement extends LinearLayout{
    TextView titre;
    TextView texte;

    public elementReglement(Context context) {
        super(context);

        LayoutInflater li =(LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        li.inflate(R.layout.element_reglement,this,true);

        titre =(TextView)findViewById(R.id.titreReglement);
        texte =(TextView)findViewById(R.id.texteReglement);
    }
}
