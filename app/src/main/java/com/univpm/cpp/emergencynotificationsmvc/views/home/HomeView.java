package com.univpm.cpp.emergencynotificationsmvc.views.home;

import android.support.v4.app.Fragment;

import com.univpm.cpp.emergencynotificationsmvc.models.map.Map;
import com.univpm.cpp.emergencynotificationsmvc.views.ViewMvc;

import java.util.ArrayList;

public interface HomeView extends ViewMvc{

    interface MapSpnItemSelectedViewListener {
        /**
         *  Questo callback è invocato quando è selezionata una mappa dallo Spinner
         */
        void onMapSpnItemSelected(String map);
    }

    void setMapSelectedListener(HomeView.MapSpnItemSelectedViewListener listener);
    void populateSpinner(ArrayList<String> list);
    String getMap();
    void showProgress(boolean show);
    void setToolbar(Fragment fragment);
    void setMapOnView(String path);
}
