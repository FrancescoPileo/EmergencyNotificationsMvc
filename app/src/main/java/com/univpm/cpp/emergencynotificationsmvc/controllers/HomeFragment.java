package com.univpm.cpp.emergencynotificationsmvc.controllers;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.text.SimpleDateFormat;
import java.text.ParseException;


import com.univpm.cpp.emergencynotificationsmvc.controllers.bluetooth.BluetoothManager;
import com.univpm.cpp.emergencynotificationsmvc.models.beacon.BeaconModel;
import com.univpm.cpp.emergencynotificationsmvc.models.beacon.BeaconModelImpl;
import com.univpm.cpp.emergencynotificationsmvc.models.local.LocalPreferences;
import com.univpm.cpp.emergencynotificationsmvc.models.local.LocalPreferencesImpl;
import com.univpm.cpp.emergencynotificationsmvc.models.map.Map;
import com.univpm.cpp.emergencynotificationsmvc.models.map.MapModel;
import com.univpm.cpp.emergencynotificationsmvc.models.map.MapModelImpl;
import com.univpm.cpp.emergencynotificationsmvc.models.node.Node;
import com.univpm.cpp.emergencynotificationsmvc.models.node.NodeModel;
import com.univpm.cpp.emergencynotificationsmvc.models.node.NodeModelImpl;
import com.univpm.cpp.emergencynotificationsmvc.models.position.Position;
import com.univpm.cpp.emergencynotificationsmvc.models.position.PositionModel;
import com.univpm.cpp.emergencynotificationsmvc.models.position.PositionModelImpl;
import com.univpm.cpp.emergencynotificationsmvc.models.user.User;
import com.univpm.cpp.emergencynotificationsmvc.models.user.UserModel;
import com.univpm.cpp.emergencynotificationsmvc.models.user.UserModelImpl;
import com.univpm.cpp.emergencynotificationsmvc.views.home.HomeView;
import com.univpm.cpp.emergencynotificationsmvc.views.home.HomeViewImpl;

import java.util.ArrayList;
import java.util.Date;

public class HomeFragment extends Fragment implements
        HomeView.MapSpnItemSelectedViewListener {

    private HomeView mHomeView;
    private MapModel mMapModel;
    private BeaconModel mBeaconModel;
    private UserModel mUserModel;
    private NodeModel mNodeModel;
    private PositionModel mPositionModel;
    private SpinnerTask mSpinnerTask;
    private FirstMapTask mFirstMapTask;
    private MapTask mMapTask;
    private BluetoothManager mBluetoothManager;
    private LocalPreferences mLocalPreferences;
    private Map map;
    private User user;
    private Node positionNode;              //è il nodo relativo alla posizione dell'utente
    private boolean firstTime;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mHomeView = new HomeViewImpl(inflater, container, getContext());
        mMapModel = new MapModelImpl();
        mUserModel = new UserModelImpl();
        mNodeModel = new NodeModelImpl();
        mLocalPreferences = new LocalPreferencesImpl(getContext());
        mBeaconModel = new BeaconModelImpl();
        mPositionModel = new PositionModelImpl();
        mSpinnerTask = new SpinnerTask();
        map = new Map();
        positionNode = new Node();
        firstTime = true;

        mSpinnerTask.execute((Void) null);
        mHomeView.setMapSelectedListener(this);
        mHomeView.setToolbar(this);

        mBluetoothManager = new BluetoothManager(getContext(), getActivity());
        mBluetoothManager.scanning();
        start();

        return mHomeView.getRootView();
    }

    private boolean started = false;
    private Handler handler = new Handler();

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            mBluetoothManager.scanning();
            if(started) {
                start();
            }
        }
    };

    public void stop() {
        started = false;
        handler.removeCallbacks(runnable);
    }

    public void start() {
        started = true;
        handler.postDelayed(runnable, 60000);
    }


    /* Al caricamento della home non è stato selezionato niente dallo spinner, parte FirstMapTask
     * Se si seleziona una mappa dallo spinner parte MapTask */
    @Override
    public void onMapSpnItemSelected(String nameMap) {

        if (firstTime == true) {

            mFirstMapTask = new FirstMapTask(nameMap);
            mFirstMapTask.execute((Void) null);
        }

        else {

            mMapTask = new MapTask(nameMap);
            mMapTask.execute((Void) null);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onPause() {
        //stop();
        super.onPause();
    }

    @Override
    public void onStop() {
        //stop();
        super.onStop();
    }


    public class SpinnerTask extends AsyncTask<Void, Void, Boolean> {

        private ArrayList<String> stringArrayList;

        SpinnerTask() {

            this.stringArrayList = new ArrayList<>();
            this.stringArrayList = null;
        }


        @Override
        protected Boolean doInBackground(Void... params) {

            this.stringArrayList = mMapModel.getAllNames();
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mSpinnerTask = null;

            if (success) {
                mHomeView.populateSpinner(stringArrayList);
            }
            else {
                Log.w("Spinner", "error");
            }
        }

    }


    public class FirstMapTask extends AsyncTask<Void, Void, Boolean> {

        private String nameMap;
        private String path;

        FirstMapTask(String nameMap) {

            this.nameMap = nameMap;
            this.path = null;
        }


        /* Se l'utente ha una o più posizioni, prende la più recente e la relativa mappa (in map)
         * Altrimenti in map si trova la prima mappa dello spinner */
        @Override
        protected Boolean doInBackground(Void... params) {


            user = mUserModel.getUser(mLocalPreferences.getUsername());
            Position lastPosition = findLastPosition(user);

            if (lastPosition.getIdPosition() == -1) {
                map = mMapModel.getMapByName(nameMap);
                path = map.getImagePath();
            }

            else {
                positionNode = mNodeModel.getNodeById(lastPosition.getIdNode());
                map = mMapModel.getMapById(positionNode.getIdMap());
                path = map.getImagePath();
            }

            return true;

        }

        /* Se la posizione è stata trovata, stampa la mappa con il marker sulle coordinate
         * Altrimenti stampa la prima mappa dello spinner */
        @Override
        protected void onPostExecute(final Boolean success) {
            mSpinnerTask = null;

            if (success) {

                mHomeView.setMapOnView(path);

                if (positionNode.getIdNode() != -1) {
                    mHomeView.setPosition(getPixelsXFromMetres(positionNode.getX(), map), getPixelsYFromMetres(positionNode.getY(), map)); //qua gli si deve passare la x e la y del nodo posizione
                    mHomeView.setPositionText("La tua posizione è in "+ map.getName() + ".");
                    mHomeView.setMapName("La mappa visualizzata è " + map.getName() + ".");
                }

                else mHomeView.setMapName("La mappa visualizzata è " + map.getName() + ".");

                firstTime = false;

            }

            else {
                Log.w("Map", "error");
                firstTime = false;
            }

        }

    }

    public class MapTask extends AsyncTask<Void, Void, Boolean> {

        private String nameMap;
        private String path;
        private Map positionMap;

        MapTask(String nameMap) {

            this.nameMap = nameMap;
            this.path = null;
        }


        /* Prende la mappa selezionata dallo spinner e, se esiste la posizione, salva la relativa mappa in positionMap */
        @Override
        protected Boolean doInBackground(Void... params) {

            map = mMapModel.getMapByName(nameMap);
            path = map.getImagePath();

            if (positionNode.getIdNode() != -1) positionMap = mMapModel.getMapById(positionNode.getIdMap());

            return true;

        }

        /* Se la posizione esiste e la mappa scelta dallo spinner è quella relativa alla propria posizione, stampa mappa con marker
         * Se la posizione esiste e la mappa scelta dallo spinner è un'altra, stampa quella scelta senza marker
         * Se la posizione non esiste viene ovviamente stampata la mappa scelta dallo spinner */
        @Override
        protected void onPostExecute(final Boolean success) {
            mSpinnerTask = null;

            if (success) {

                mHomeView.setMapOnView(path);

                if (positionNode.getIdNode() != -1) {

                    if (nameMap.equals(positionMap.getName())) {
                        mHomeView.setPositionText("La tua posizione è in "+ positionMap.getName() + ".");
                        mHomeView.setMapName("La mappa visualizzata è " + map.getName() + ".");
                        mHomeView.setPosition(getPixelsXFromMetres(positionNode.getX(), positionMap), getPixelsYFromMetres(positionNode.getY(), positionMap)); //qua gli si deve passare la x e la y del nodo posizione
                    }

                    else {
                        mHomeView.setPositionText("La tua posizione è in "+ positionMap.getName() + ".");
                        mHomeView.setMapName("La mappa visualizzata è " + map.getName() + ".");
                    }

                }

                else mHomeView.setMapName("La mappa visualizzata è " + map.getName() + ".");

            }

            else {
                Log.w("Map", "error");
            }

        }

    }


    private int getPixelsXFromMetres(int x, Map map) {

        float scale = map.getScale();
        int xRef = map.getxRef();
        int xRefpx = map.getxRefpx();

        return (int) (xRefpx-((xRef - x)*scale));
    }

    private int getPixelsYFromMetres(int y, Map map) {

        float scale = map.getScale();
        int yRef = map.getyRef();
        int yRefpx = map.getyRefpx();

        return (int) (yRefpx-((yRef - y)*scale));
    }


    /* @param user di cui si vuole trovare l'ultima posizione
     * controlla tutte le posizioni dell'utente
     * @return la posizione più recente */
    private Position findLastPosition(User user) {

        int cont;
        int size;
        Date time1 = new Date();
        Date time2 = new Date();
        Position lastPosition = new Position();
        lastPosition.setTime("0000-00-00 00:00:00");
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        ArrayList<Position> positions = mPositionModel.getPositionByIdUser(user.getId());

        if (positions == null) size = 0;
        else {
            size = positions.size();

            for (cont=0; cont < size; cont++) {

                try {
                    time1 = formatter.parse(lastPosition.getTime());
                    time2 = formatter.parse(positions.get(cont).getTime());
                }
                catch (ParseException e) {
                    e.printStackTrace();
                }

                if (time2.after(time1)) lastPosition = positions.get(cont); //timeaftertime is a famous song by Cyndi Lauper

            }
        }

        Log.w("Size", String.valueOf(size));
        Log.w("Lastpositionid", String.valueOf(lastPosition.getIdPosition()));



        return lastPosition;
    }



}