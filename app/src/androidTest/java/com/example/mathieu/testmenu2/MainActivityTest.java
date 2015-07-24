package com.example.mathieu.testmenu2;


import android.app.Fragment;
import android.content.res.Resources;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.SmallTest;
import android.widget.ListView;


public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {
    MainActivity myActivity;
    ListView listMenu;

    public MainActivityTest() {
        super(MainActivity.class);

    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        myActivity = getActivity();
        listMenu =
                (ListView) myActivity
                        .findViewById(R.id.left_drawer);
    }

    //Test récupération et création éléments menu
    //
    @SmallTest
    public void testContentMenuInit() {

        Resources res = myActivity.getResources();
        String[] menuElement = res.getStringArray(R.array.items_menu);

        //Vérification nombre d'éléments du menu
        //
        int nbElement = listMenu.getAdapter().getCount();
        assertEquals(menuElement.length, nbElement);


        //Vérification contenue de chaque élément du menu
        //
        for(int i=0; i<nbElement; i++){
            String element = (String) listMenu.getAdapter().getItem(i);
            assertEquals(menuElement[i], element);
        }
    }

    //Test du fragment présent dans le FrameLayout lors de l'initialisation (null pour l'instant)
    //
    @SmallTest
    public void testContentFrameInit() {
        Fragment frag = myActivity.getFragmentManager().findFragmentById(R.id.content_frame);
        assertNull(frag);
    }

    //Test du onclick et du lancement du fragment InfosPratique
    //
    @SmallTest
    public void testInfosPratiqueFragmentLaunch() {

        //Lancement du click sur l'item Infos Pratiques
        //
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

        //Récupération du fragment dans le FrameLayout
        //
        Fragment frag = myActivity.getFragmentManager().findFragmentById(R.id.content_frame);
        assertNotNull(frag);

        //Vérification du fragment
        //
        assertEquals(frag.getClass(), InfosPratiques.class);
    }


    //Test du onclick et du lancement du fragment Artistes
    //
    @SmallTest
    public void testArtistesFragmentLaunch() {

        myActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                listMenu.performItemClick(
                        listMenu.getAdapter().getView(2, null, null),
                        2,
                        listMenu.getAdapter().getItemId(2));
            }
        });

        getInstrumentation().waitForIdleSync();

        Fragment frag = myActivity.getFragmentManager().findFragmentById(R.id.content_frame);
        assertNotNull(frag);

        assertEquals(frag.getClass(), Artistes.class);
    }


    //Test du onclick et du lancement du fragment CameraActivity
    //
    @SmallTest
    public void testCameraFragmentLaunch() {

        myActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                listMenu.performItemClick(
                        listMenu.getAdapter().getView(1, null, null),
                        1,
                        listMenu.getAdapter().getItemId(1));
            }
        });

        getInstrumentation().waitForIdleSync();

        Fragment frag = myActivity.getFragmentManager().findFragmentById(R.id.content_frame);
        assertNotNull(frag);

        assertEquals(frag.getClass(), CameraActivity.class);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }
}