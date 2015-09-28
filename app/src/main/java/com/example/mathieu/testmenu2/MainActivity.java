package com.example.mathieu.testmenu2;

import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.mathieu.testmenu2.LineUp.LineUp;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends Activity {
    String[] menu;
    DrawerLayout dLayout;
    ListView dList;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Récupération titres du menu
        //
        Resources res = getResources();
        menu = res.getStringArray(R.array.items_menu);

        //Récupération composant de la vue
        //
        dLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        dList = (ListView) findViewById(R.id.left_drawer);

        //Création du menu
        //
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,menu);
        dList.setAdapter(adapter);
        dList.setSelector(android.R.color.holo_blue_dark);

        //Mise en place du listener pour chaque item du menu
        //
        dList.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View v, int position, long id) {
                dLayout.closeDrawers();

                Fragment frag = null;

                switch (position) {
                    case 0:
                        frag = new InfosPratiques();
                        break;
                    case 1:
                        frag = new CameraActivity();
                        break;
                    case 2:
                        frag = new Artistes();
                        break;
                    case 3:
                        frag = new LineUp();
                        break;
                }

                //Lancement du fragment associé à la fonctionnalité choisie
                //
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_frame, frag).commit();
            }
        });
    }
}
