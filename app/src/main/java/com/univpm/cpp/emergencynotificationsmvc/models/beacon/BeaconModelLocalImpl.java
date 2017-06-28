package com.univpm.cpp.emergencynotificationsmvc.models.beacon;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.univpm.cpp.emergencynotificationsmvc.models.local.LocalSQLiteDbHelper;
import com.univpm.cpp.emergencynotificationsmvc.models.local.LocalSQLiteContract.*;
import com.univpm.cpp.emergencynotificationsmvc.models.node.NodeModel;
import com.univpm.cpp.emergencynotificationsmvc.models.node.NodeModelLocalImpl;

import java.util.ArrayList;

public class BeaconModelLocalImpl implements BeaconModel{

    Context context;
    LocalSQLiteDbHelper mLocalSQLiteDbHelper = null;

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
                new String[] { BeaconTable._ID, BeaconTable.COLUMN_NAME_IDNODE }
                , BeaconTable._ID + "=?",
                new String[] { idBeacon }, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            beacon = new Beacon();
            beacon.setIdBeacon(cursor.getString(0));
            NodeModel nodeModel = new NodeModelLocalImpl(context);
            beacon.setNode(nodeModel.getNodeById(cursor.getInt(1)));
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
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                beacons = new ArrayList<>();
                do {
                    Beacon beacon = new Beacon();
                    beacon.setIdBeacon(cursor.getString(0));
                    beacon.setNode(nodeModel.getNodeById(cursor.getInt(1)));
                    beacons.add(beacon);
                } while (cursor.moveToNext());
            }
        }

        //db.close();
        return beacons;
    }
}
