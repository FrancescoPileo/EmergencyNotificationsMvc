package com.univpm.cpp.emergencynotificationsmvc.views.home;

import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuInflater;

import com.univpm.cpp.emergencynotificationsmvc.models.map.Map;
import com.univpm.cpp.emergencynotificationsmvc.views.ViewMvc;

import java.util.ArrayList;
import java.util.zip.Inflater;

public interface HomeView extends ViewMvc{

    interface MapSpnItemSelectedViewListener {
        /**
         *  Questo callback è invocato quando è selezionata una mappa dallo Spinner
         */
        void onMapSpnItemSelected(String map);
    }

    interface LogoutBtnViewListener{

        void onLogoutClick();
    }

    void setMapSelectedListener(HomeView.MapSpnItemSelectedViewListener listener);
    void setLogoutListener(LogoutBtnViewListener listener);
    void populateSpinner(ArrayList<String> list);
    String getMap();
    void showProgress(boolean show);
    void setToolbar(Fragment fragment);
    void setToolbarItems(Menu menu, MenuInflater inflater);
    void executeToolbarItemAction(int itemId);

    // Prende in input l'oggetto che identifica la mappa (importato dal db), cerca la mappa nella cartella maps e la mette in TouchImageView
    void setMapOnView(Map map);

    // Mette il marker posizione sulla mappa
    void setPosition(int x, int y);

    void setPositionText (String text);

    void setMapName (String mapName);
}
