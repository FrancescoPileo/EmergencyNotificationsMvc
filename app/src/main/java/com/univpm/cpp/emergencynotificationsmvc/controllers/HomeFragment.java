package com.univpm.cpp.emergencynotificationsmvc.controllers;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;

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
    private SpinnerTask mSpinnerTask;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mHomeView = new HomeViewImpl(inflater, container, getContext());
        mMapModel = new MapModelImpl();
        mSpinnerTask = new SpinnerTask();

        mSpinnerTask.execute((Void) null);
        mHomeView.setMapSlectedListener(this);
        mHomeView.setToolbar(this);

        return mHomeView.getRootView();
    }

    private ArrayList<String> getMapsData(ArrayList<Map> list) {
        ArrayList<Map> maplist = new ArrayList<Map>();
        ArrayList<String> stringArrayList = new ArrayList<String>();
        maplist = list;
        int itemCount = maplist.size();
        int i = 0;
        Map map = new Map();
        while (i < itemCount) {
            map = maplist.get(i);
            stringArrayList.add(map.getBuilding() + " " + map.getFloor());
            i++;
        }
        return stringArrayList;
    }

    @Override
    public void onMapSpnItemSelected(String map) {
        Log.w("Spinner", "map");
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }


    public class SpinnerTask extends AsyncTask<Void, Void, Boolean> {

        private ArrayList<String> stringArrayList;

        SpinnerTask() {

            this.stringArrayList = new ArrayList<String>();
            this.stringArrayList = null;
        }


        @Override
        protected Boolean doInBackground(Void... params) {

            this.stringArrayList = getMapsData(mMapModel.getAllMaps());
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
}
