package com.univpm.cpp.emergencynotificationsmvc.controllers;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.univpm.cpp.emergencynotificationsmvc.views.home.HomeView;
import com.univpm.cpp.emergencynotificationsmvc.views.home.HomeViewImpl;

public class HomeFragment extends Fragment implements
        HomeView.MapSpnItemSelectedViewListener{

    private HomeView mHomeView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mHomeView = new HomeViewImpl(inflater, container, getContext());
        mHomeView.setMapSlectedListener(this);
        mHomeView.setToolbar(this);

        return mHomeView.getRootView();
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


    


}
