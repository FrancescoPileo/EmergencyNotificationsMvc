package com.univpm.cpp.emergencynotificationsmvc.models.local;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.univpm.cpp.emergencynotificationsmvc.models.beacon.Beacon;
import com.univpm.cpp.emergencynotificationsmvc.models.beacon.BeaconModel;
import com.univpm.cpp.emergencynotificationsmvc.models.beacon.BeaconModelImpl;
import com.univpm.cpp.emergencynotificationsmvc.models.envValues.EnviromentalValues;
import com.univpm.cpp.emergencynotificationsmvc.models.envValues.EnviromentalValuesModel;
import com.univpm.cpp.emergencynotificationsmvc.models.envValues.EnviromentalValuesModelImpl;
import com.univpm.cpp.emergencynotificationsmvc.models.map.Map;
import com.univpm.cpp.emergencynotificationsmvc.models.map.MapModel;
import com.univpm.cpp.emergencynotificationsmvc.models.map.MapModelImpl;
import com.univpm.cpp.emergencynotificationsmvc.models.node.Node;
import com.univpm.cpp.emergencynotificationsmvc.models.node.NodeModel;
import com.univpm.cpp.emergencynotificationsmvc.models.node.NodeModelImpl;
import com.univpm.cpp.emergencynotificationsmvc.models.position.Position;
import com.univpm.cpp.emergencynotificationsmvc.models.position.PositionModel;
import com.univpm.cpp.emergencynotificationsmvc.models.position.PositionModelImpl;
import com.univpm.cpp.emergencynotificationsmvc.models.user.User;
import com.univpm.cpp.emergencynotificationsmvc.models.user.UserModel;
import com.univpm.cpp.emergencynotificationsmvc.models.user.UserModelImpl;

import java.util.ArrayList;

public class LocalSQLiteUpdateTask extends AsyncTask<Void, Void, Boolean> {

    private Context context;

    private UserModel mUserModel = null;
    private LocalPreferences mLocalPreferences = null;
    private PositionModel mPositionModel = null;
    private MapModel mMapModel = null;
    private NodeModel mNodeModel = null;
    private BeaconModel mBeaconModel = null;
    private EnviromentalValuesModel mEnviromentalValuesModel = null;

    LocalSQLiteDbHelper helper = null;

    public LocalSQLiteUpdateTask(Context context){
        this.context = context;

        mUserModel = new UserModelImpl();
        mLocalPreferences = new LocalPreferencesImpl(getContext());
        mPositionModel = new PositionModelImpl();
        mMapModel = new MapModelImpl();
        mNodeModel = new NodeModelImpl();
        mBeaconModel = new BeaconModelImpl();
        mEnviromentalValuesModel = new EnviromentalValuesModelImpl();

        helper = new LocalSQLiteDbHelper(getContext());
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        User user = mUserModel.getUser(mLocalPreferences.getUsername());
        Position lastPosition = mPositionModel.getLastPositionByUser(user);
        ArrayList<Map> maps = mMapModel.getAllMaps();
        ArrayList<Node> nodes = mNodeModel.getAllNodes();
        ArrayList<Beacon> beacons = mBeaconModel.getAllBeacons();
        ArrayList<EnviromentalValues> enviromentalValues = mEnviromentalValuesModel.getLastValuesForEachBeacon();
        helper.importAppuser(user);
        helper.importMaps(maps);
        helper.importNodes(nodes);
        helper.importBeacons(beacons);
        helper.importUserposition(lastPosition);
        helper.importEnviromentalValues(enviromentalValues);
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
