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

    // Prende in input il path della mappa (preso dal db), cerca la mappa nella cartella drawable e la mette in TouchImageView
    void setMapOnView(String path);

    // Mette il marker posizione sulla mappa
    void setPosition(int x, int y);
}
