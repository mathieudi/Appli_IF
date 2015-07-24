package com.example.mathieu.testmenu2;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by mathieu on 22/05/15.
 */
public class elementInfos extends LinearLayout {
    TextView texte;
    Button bouton;

    public elementInfos(Context context) {
        super(context);
        LayoutInflater li =(LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        li.inflate(R.layout.element_infos,this,true);

        bouton =(Button)findViewById(R.id.accederInfo);
        texte =(TextView)findViewById(R.id.texteInfo);
    }
}
