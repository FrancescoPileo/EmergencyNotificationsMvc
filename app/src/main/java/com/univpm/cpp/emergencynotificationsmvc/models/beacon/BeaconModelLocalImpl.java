package com.univpm.cpp.emergencynotificationsmvc.models.beacon;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.univpm.cpp.emergencynotificationsmvc.models.local.LocalSQLiteContract.BeaconTable;
import com.univpm.cpp.emergencynotificationsmvc.models.local.LocalSQLiteDbHelper;
import com.univpm.cpp.emergencynotificationsmvc.models.node.NodeModel;
import com.univpm.cpp.emergencynotificationsmvc.models.node.NodeModelLocalImpl;

import java.util.ArrayList;

/**
 * Implementazione del modello del beacon che fa riferimento al database interno
 */
public class BeaconModelLocalImpl implements BeaconModel{

    private Context context;    //Constesto dell'applicazione
    private LocalSQLiteDbHelper mLocalSQLiteDbHelper = null; //Helper che gestisce al connessione al Db interno

    public BeaconModelLocalImpl(Context context){
        this.context = context;
        mLocalSQLiteDbHelper = LocalSQLiteDbHelper.getInstance(context);
        Log.w("LocalMapModel", "ok");
    }

    @Override
    public Beacon getBeaconById(String idBeacon) {
        Beacon beacon = null;
        SQLiteDatabase db = mLocalSQLiteDbHelper.getReadableDatabase();

        Cursor cursor = db.query( BeaconTable.TABLE_NAME,
                new String[] { BeaconTable._ID, BeaconTable.COLUMN_NAME_IDNODE, BeaconTable.COLUMN_NAME_EMERGENCY }
                , BeaconTable._ID + "=?",
                new String[] { idBeacon }, null, null, null, null);
        if (cursor != null  && cursor.getCount() != 0) {
            cursor.moveToFirst();
            beacon = new Beacon();
            beacon.setIdBeacon(cursor.getString(0));
            NodeModel nodeModel = new NodeModelLocalImpl(context);
            beacon.setNode(nodeModel.getNodeById(cursor.getInt(1)));
            beacon.setEmergency(cursor.getString(2));
        }
        //db.close();
        return beacon;
    }

    @Override
    public ArrayList<Beacon> getAllBeacons() {
        ArrayList<Beacon> beacons = null;
        String selectQuery = "SELECT * FROM " + BeaconTable.TABLE_NAME;
        SQLiteDatabase db = mLocalSQLiteDbHelper.getReadableDatabase();

        NodeModel nodeModel = new NodeModelLocalImpl(context);

        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor != null && cursor.getCount() != 0) {
            if (cursor.moveToFirst()) {
                beacons = new ArrayList<>();
                do {
                    Beacon beacon = new Beacon();
                    beacon.setIdBeacon(cursor.getString(0));
                    beacon.setNode(nodeModel.getNodeById(cursor.getInt(1)));
                    beacon.setEmergency(cursor.getString(2));
                    beacons.add(beacon);
                } while (cursor.moveToNext());
            }
        }

        //db.close();
        return beacons;
    }
}
