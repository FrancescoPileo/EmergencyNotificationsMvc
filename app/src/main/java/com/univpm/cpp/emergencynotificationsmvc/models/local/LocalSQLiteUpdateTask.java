package com.univpm.cpp.emergencynotificationsmvc.models.local;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.univpm.cpp.emergencynotificationsmvc.EmergencyNotificationsMvc;
import com.univpm.cpp.emergencynotificationsmvc.R;
import com.univpm.cpp.emergencynotificationsmvc.models.beacon.Beacon;
import com.univpm.cpp.emergencynotificationsmvc.models.envValues.EnviromentalValues;
import com.univpm.cpp.emergencynotificationsmvc.models.map.Map;
import com.univpm.cpp.emergencynotificationsmvc.models.node.Node;
import com.univpm.cpp.emergencynotificationsmvc.models.position.Position;
import com.univpm.cpp.emergencynotificationsmvc.models.user.User;


import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class LocalSQLiteUpdateTask extends AsyncTask<Void, Void, Boolean> {

    private Context context;

    LocalSQLiteDbHelper helper = null;
    EmergencyNotificationsMvc application = null;

    public LocalSQLiteUpdateTask(EmergencyNotificationsMvc application){
        this.application = application;
        this.context = application.getApplicationContext();
        helper = LocalSQLiteDbHelper.getInstance(context);
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        User user = null;
        Position lastPosition = null;
        ArrayList<Map> maps = null;
        ArrayList<Node> nodes = null;
        ArrayList<Beacon> beacons = null;
        ArrayList<EnviromentalValues> enviromentalValues = null;
        user = application.getLocalPreferences().getUser();

        if (application.isConnectionEnabled()) {
            lastPosition = application.getPositionModel().getLastPositionByUser(user);
            maps = application.getMapModel().getAllMaps();
            nodes = application.getNodeModel().getAllNodes();
            beacons = application.getBeaconModel().getAllBeacons();
            enviromentalValues = application.getEnviromentalValuesModel().getLastValuesForEachBeacon();
        } else if (user.isGuest() && helper.isDbEmpty()){
            Log.w("ImportDaResources", "ok");
            //se l'utente è guest e il db è vuoto
            lastPosition = null;
            enviromentalValues = null;
            maps = new ArrayList<>();
            nodes = new ArrayList<>();
            beacons = new ArrayList<>();
            JSONArray jsonArray = null;
            try {
                jsonArray = new JSONArray(application.getResources().getString(R.string.DB_maps));
                for (int i = 0; i < jsonArray.length(); i++) {
                    maps.add(new Map(jsonArray.getJSONObject(i).toString()));
                }

                jsonArray = new JSONArray(application.getResources().getString(R.string.DB_nodes));
                for (int i = 0; i < jsonArray.length(); i++) {
                    nodes.add(new Node(jsonArray.getJSONObject(i).toString()));
                }

                jsonArray = new JSONArray(application.getResources().getString(R.string.DB_beacons));
                for (int i = 0; i < jsonArray.length(); i++) {
                    beacons.add(new Beacon(jsonArray.getJSONObject(i).toString()));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        helper.importAppuser(user);
        helper.importMaps(maps);
        helper.importNodes(nodes);
        helper.importBeacons(beacons);
        if (lastPosition != null) helper.importUserposition(lastPosition);
        if (enviromentalValues != null) helper.importEnviromentalValues(enviromentalValues);
        return null;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        helper.close();
        Log.w("LocalSQLiteUpdated", "true");
        super.onPostExecute(aBoolean);
    }

    public Context getContext() {
        return context;
    }
}
