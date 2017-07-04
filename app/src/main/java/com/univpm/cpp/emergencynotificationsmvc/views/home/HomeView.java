package com.univpm.cpp.emergencynotificationsmvc.views.home;

import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuInflater;

import com.univpm.cpp.emergencynotificationsmvc.models.beacon.Beacon;
import com.univpm.cpp.emergencynotificationsmvc.models.map.Map;
import com.univpm.cpp.emergencynotificationsmvc.views.ViewMvc;

import java.util.ArrayList;

/**
 * Intefaccia che rappresenta la schermata principale dell'applicazione che prmette la
 * visualizzazione delle mappe
 */
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

    interface BeaconTouchListener{

        void onBeaconClick(Beacon beacon);
    }

    interface InfoBtnListener {
        void onInfoClick();
    }

    void setMapSelectedListener(HomeView.MapSpnItemSelectedViewListener listener);
    void setLogoutListener(LogoutBtnViewListener listener);
    void setBeaconTouchListener (BeaconTouchListener listener);
    void setInfoBtnListener(InfoBtnListener listener);
    void populateSpinner(ArrayList<String> list);
    String getMap();
    void showProgress(boolean show);
    void setToolbar(Fragment fragment);
    void setToolbarItems(Menu menu, MenuInflater inflater);
    void executeToolbarItemAction(int itemId);

    // Prende in input l'oggetto che identifica la mappa (importato dal db), cerca la mappa nella cartella maps e la mette in TouchImageView
    void setMapOnView(Map map);

    void setBeaconsOnMap(ArrayList<Beacon> beacons, Map map);

    // Mette il marker posizione sulla mappa
    void setPosition(int x, int y);

    void setPositionText (String text);

    void setMapName (String mapName);
}
