package com.example.mathieu.testmenu2;

import android.app.Fragment;
import android.app.Instrumentation;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.SmallTest;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;

import java.util.Arrays;
import java.util.List;

/**
 * Created by mathieu on 13/06/15.
 */
public class InfosPratiquesTest extends ActivityInstrumentationTestCase2<MainActivity> {
    MainActivity myActivity;
    ListView listMenu;
    InfosPratiques frag;

    public InfosPratiquesTest() {
        super(MainActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        myActivity = getActivity();
        listMenu =
                (ListView) myActivity
                        .findViewById(R.id.left_drawer);

        myActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                listMenu.performItemClick(
                        listMenu.getAdapter().getView(0, null, null),
                        0,
                        listMenu.getAdapter().getItemId(0));
            }
        });

        getInstrumentation().waitForIdleSync();

        frag = (InfosPratiques) myActivity.getFragmentManager().findFragmentById(R.id.content_frame);
    }


    //Test récupération titre et création des éléments d'infos pratiques
    //
    @SmallTest
    public void testListeInfos() {
        Resources res = myActivity.getResources();
        TypedArray ar = res.obtainTypedArray(R.array.liste_infos);

        for (int i = 0; i < ar.length(); ++i) {

            int resId = ar.getResourceId(i, 0);
            List<String> values = Arrays.asList(res.getStringArray(resId));

            assertEquals(frag.e[i].texte.getText(), values.get(0));
        }
    }


    //Test onclick et lancement de l'activity PresentationAsso
    //
    @SmallTest
    public void testLaunchPresentationAsso() {

        Instrumentation.ActivityMonitor activityMonitor = getInstrumentation().addMonitor(PresentationAsso.class.getName(), null, false);

        myActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                frag.e[0].bouton.performClick();
            }
        });

        getInstrumentation().waitForIdleSync();

        PresentationAsso nextActivity = (PresentationAsso) getInstrumentation().waitForMonitorWithTimeout(activityMonitor, 5);

        assertNotNull(nextActivity);

        nextActivity .finish();
    }


    //Test onclick et lancement de l'activity Billeterie
    //
    @SmallTest
    public void testLaunchBilleterie() {

        Instrumentation.ActivityMonitor activityMonitor = getInstrumentation().addMonitor(Billeterie.class.getName(), null, false);

        myActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                frag.e[1].bouton.performClick();
            }
        });

        getInstrumentation().waitForIdleSync();

        Billeterie nextActivity = (Billeterie) getInstrumentation().waitForMonitorWithTimeout(activityMonitor, 5);

        assertNotNull(nextActivity);

        nextActivity .finish();
    }

    //Test onclick et lancement de l'activity Reglement
    //
    @SmallTest
    public void testLaunchReglement() {

        Instrumentation.ActivityMonitor activityMonitor = getInstrumentation().addMonitor(Reglement.class.getName(), null, false);

        myActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                frag.e[2].bouton.performClick();
            }
        });

        getInstrumentation().waitForIdleSync();

        Reglement nextActivity = (Reglement) getInstrumentation().waitForMonitorWithTimeout(activityMonitor, 5);

        assertNotNull(nextActivity);

        nextActivity .finish();
    }

    //Test onclick et lancement de l'activity Partenaires
    //
    @SmallTest
    public void testLaunchPartenaires() {

        Instrumentation.ActivityMonitor activityMonitor = getInstrumentation().addMonitor(Partenaires.class.getName(), null, false);

        myActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                frag.e[3].bouton.performClick();
            }
        });

        getInstrumentation().waitForIdleSync();

        Partenaires nextActivity = (Partenaires) getInstrumentation().waitForMonitorWithTimeout(activityMonitor, 5);

        assertNotNull(nextActivity);

        nextActivity .finish();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

}
