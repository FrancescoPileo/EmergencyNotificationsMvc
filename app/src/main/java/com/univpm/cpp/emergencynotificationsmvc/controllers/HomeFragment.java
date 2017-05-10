package com.univpm.cpp.emergencynotificationsmvc.controllers;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;

import com.univpm.cpp.emergencynotificationsmvc.controllers.bluetooth.BluetoothManager;
import com.univpm.cpp.emergencynotificationsmvc.models.beacon.Beacon;
import com.univpm.cpp.emergencynotificationsmvc.models.beacon.BeaconModel;
import com.univpm.cpp.emergencynotificationsmvc.models.beacon.BeaconModelImpl;
import com.univpm.cpp.emergencynotificationsmvc.models.map.Map;
import com.univpm.cpp.emergencynotificationsmvc.models.map.MapModel;
import com.univpm.cpp.emergencynotificationsmvc.models.map.MapModelImpl;
import com.univpm.cpp.emergencynotificationsmvc.views.home.HomeView;
import com.univpm.cpp.emergencynotificationsmvc.views.home.HomeViewImpl;

import java.util.ArrayList;

public class HomeFragment extends Fragment implements
        HomeView.MapSpnItemSelectedViewListener {

    private HomeView mHomeView;
    private MapModel mMapModel;
    private BeaconModel mBeaconModel;
    private SpinnerTask mSpinnerTask;
    private MapTask mMapTask;
    private BluetoothManager mBluetoothManager;
    private Map map;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mHomeView = new HomeViewImpl(inflater, container, getContext());
        mMapModel = new MapModelImpl();
        mBeaconModel = new BeaconModelImpl();
        mSpinnerTask = new SpinnerTask();
        map = new Map();

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

    @Override
    public void onMapSpnItemSelected(String nameMap) {

        mMapTask = new MapTask(nameMap);
        mMapTask.execute((Void) null);
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


    // Dato il nome della mappa preso dallo Spinner, invoca setMapOnView passandogli il path preso dal db
    public class MapTask extends AsyncTask<Void, Void, Boolean> {

        private String nameMap;
        private String path;

        MapTask(String nameMap) {

            this.nameMap = nameMap;
            this.path = null;
        }


        @Override
        protected Boolean doInBackground(Void... params) {


                map = mMapModel.getMapByName(nameMap);

                path = map.getImagePath();
                return true;

        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mSpinnerTask = null;

            if (success) {
                mHomeView.setMapOnView(path);
                mHomeView.setPosition(getPixelsXFromMetres(91), getPixelsYFromMetres(467));   //qua gli si deve passare la x e la y del nodo posizione
            } else {
                Log.w("Map", "error");
            }
        }

    }

    private int getPixelsXFromMetres(int x) {

        float scale = map.getScale();
        int xRef = map.getxRef();
        int xRefpx = map.getxRefpx();

        return (int) (xRefpx-((xRef - x)*scale));
    }

    private int getPixelsYFromMetres(int y) {

        float scale = map.getScale();
        int yRef = map.getyRef();
        int yRefpx = map.getyRefpx();

        return (int) (yRefpx-((yRef - y)*scale));
    }



}
