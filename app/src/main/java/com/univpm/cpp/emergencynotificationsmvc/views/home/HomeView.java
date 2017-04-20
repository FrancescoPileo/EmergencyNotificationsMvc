package com.univpm.cpp.emergencynotificationsmvc.views.home;

import android.support.v4.app.Fragment;

import com.univpm.cpp.emergencynotificationsmvc.views.ViewMvc;

public interface HomeView extends ViewMvc{

    interface MapSpnItemSelectedViewListener {
        /**
         *  Questo callback è invocato quando è selezionata una mappa dallo Spinner
         */
        void onMapSpnItemSelected(String map);
    }

    void setMapSlectedListener(HomeView.MapSpnItemSelectedViewListener listener);

    String getMap();

    void showProgress(boolean show);
    void setToolbar(Fragment fragment);
}
