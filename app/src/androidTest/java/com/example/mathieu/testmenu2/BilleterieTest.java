package com.example.mathieu.testmenu2;

import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.SmallTest;
import android.widget.Button;

/**
 * Created by mathieu on 13/06/15.
 */
public class BilleterieTest extends ActivityInstrumentationTestCase2<Billeterie> {
    Billeterie myActivity;
    Button retour;
    Button boutique;

    public BilleterieTest() {
        super(Billeterie.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        myActivity = getActivity();
        retour = myActivity.retour;
        boutique = myActivity.boutique;
    }

    //Test de l'ouverture du navigateur vers la boutique lors du click sur le bouton boutique
    //
    @SmallTest
    public void testAccesBilleterie() {
        myActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                boutique.performClick();
            }
        });
        getInstrumentation().waitForIdleSync();

        //AJout la vérification
        //
    }

    //Test de la fermeture de l'activity lors du click sur le bouton retour
    //
    @SmallTest
    public void testRetour() {
        myActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                retour.performClick();
            }
        });
        getInstrumentation().waitForIdleSync();

        assertTrue(myActivity.isFinishing());
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }
}
