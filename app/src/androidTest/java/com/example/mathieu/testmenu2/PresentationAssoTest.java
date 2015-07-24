package com.example.mathieu.testmenu2;

import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.SmallTest;
import android.widget.Button;

/**
 * Created by mathieu on 13/06/15.
 */
public class PresentationAssoTest extends ActivityInstrumentationTestCase2<PresentationAsso> {
    PresentationAsso myActivity;
    Button retour;

    public PresentationAssoTest() {
        super(PresentationAsso.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        myActivity = getActivity();
        retour = myActivity.retour;
    }

    //Test de la fermeture de l'activity lors du click sur le bouton retour
    //
    @SmallTest
    public void testButtonRetour() {
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
