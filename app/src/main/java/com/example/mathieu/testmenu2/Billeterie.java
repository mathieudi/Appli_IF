package com.example.mathieu.testmenu2;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.*;
import android.view.View;


public class Billeterie extends Activity{
    Button retour;
    Button boutique;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.billeterie);

        retour = (Button) findViewById(R.id.billeterie_retour);

        boutique = (Button) findViewById(R.id.billeterie_boutique);

        boutique.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent accesBoutique = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.imaginariumfestival.com/#ticketing"));
                startActivity(accesBoutique);
            }
        });

        retour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
