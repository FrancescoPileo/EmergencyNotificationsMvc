package com.univpm.cpp.emergencynotificationsmvc.models.local;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.univpm.cpp.emergencynotificationsmvc.MainActivity;
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
    MainActivity activity = null;
    Runnable onPostExecuteRun = null;

    public LocalSQLiteUpdateTask(MainActivity activity, Runnable onPostExecuteRun){
        this.activity = activity;
        this.context = activity.getApplicationContext();
        this.onPostExecuteRun = onPostExecuteRun;
        helper = LocalSQLiteDbHelper.getInstance(context);
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        Log.w("UpdateTask", "start");
        User user = null;
        Position lastPosition = null;
        ArrayList<Map> maps = null;
        ArrayList<Node> nodes = null;
        ArrayList<Beacon> beacons = null;
        ArrayList<EnviromentalValues> enviromentalValues = null;
        user = activity.getLocalPreferences().getUser();

        if (activity.getmConnectionStatus() == MainActivity.CONNECTION_ONLINE) {
            lastPosition = activity.getPositionModel().getLastPositionByUser(user);
            maps = activity.getMapModel().getAllMaps();
            nodes = activity.getNodeModel().getAllNodes();
            beacons = activity.getBeaconModel().getAllBeacons();
            enviromentalValues = activity.getEnviromentalValuesModel().getLastValuesForEachBeacon();
        } else if (user.isGuest() /*&& helper.isDbEmpty()*/){
            Log.w("ImportDaResources", "ok");
            //se l'utente è guest e il db è vuoto
            lastPosition = null;
            enviromentalValues = null;
            maps = new ArrayList<>();
            nodes = new ArrayList<>();
            beacons = new ArrayList<>();
            JSONArray jsonArray = null;
            try {
                jsonArray = new JSONArray(activity.getResources().getString(R.string.DB_maps));
                for (int i = 0; i < jsonArray.length(); i++) {
                    maps.add(new Map(jsonArray.getJSONObject(i).toString()));
                }

                jsonArray = new JSONArray(activity.getResources().getString(R.string.DB_nodes));
                for (int i = 0; i < jsonArray.length(); i++) {
                    nodes.add(new Node(jsonArray.getJSONObject(i).toString()));
                }

                jsonArray = new JSONArray(activity.getResources().getString(R.string.DB_beacons));
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
        Log.w("LocalSQLiteUpdated", "true");
        if (onPostExecuteRun != null){
            onPostExecuteRun.run();
        }
    }

    public Context getContext() {
        return context;
    }
}
