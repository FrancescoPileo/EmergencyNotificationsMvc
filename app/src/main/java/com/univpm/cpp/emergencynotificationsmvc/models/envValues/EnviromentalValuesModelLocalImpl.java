package com.univpm.cpp.emergencynotificationsmvc.models.envValues;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.univpm.cpp.emergencynotificationsmvc.models.beacon.BeaconModel;
import com.univpm.cpp.emergencynotificationsmvc.models.beacon.BeaconModelLocalImpl;
import com.univpm.cpp.emergencynotificationsmvc.models.local.LocalSQLiteContract.EnviromentalvaluesTable;
import com.univpm.cpp.emergencynotificationsmvc.models.local.LocalSQLiteDbHelper;

import java.util.ArrayList;

/**
 * Implementazione del modello dei valori ambientali che fa riferimento al database interno
 */
public class EnviromentalValuesModelLocalImpl implements EnviromentalValuesModel {


    Context context;        //Contesto dell'applicazione
    LocalSQLiteDbHelper mLocalSQLiteDbHelper = null;    //Helper che gestisce al connessione al Db interno

    public EnviromentalValuesModelLocalImpl(Context context){
        this.context = context;
        mLocalSQLiteDbHelper = LocalSQLiteDbHelper.getInstance(context);
        Log.w("LocalEnvValuesModel", "ok");
    }

    @Override
    public ArrayList<EnviromentalValues> getAllValues() {
        ArrayList<EnviromentalValues> values = null;
        String selectQuery = "SELECT * FROM " + EnviromentalvaluesTable.TABLE_NAME;
        SQLiteDatabase db = mLocalSQLiteDbHelper.getReadableDatabase();

        BeaconModel beaconModel = new BeaconModelLocalImpl(context);

        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor != null && cursor.getCount() != 0) {
            if (cursor.moveToFirst()) {
                values = new ArrayList<>();
                do {
                    EnviromentalValues value = new EnviromentalValues();
                    value.setIdEnv(cursor.getInt(0));
                    value.setBeacon(beaconModel.getBeaconById(cursor.getString(1)));
                    value.setTime(cursor.getString(2));
                    value.setTemperature(cursor.getDouble(3));
                    value.setHumidity(cursor.getDouble(4));
                    value.setAccX(cursor.getDouble(5));
                    value.setAccY(cursor.getDouble(6));
                    value.setAccZ(cursor.getDouble(7));
                    value.setGyrX(cursor.getDouble(8));
                    value.setGyrY(cursor.getDouble(9));
                    value.setGyrZ(cursor.getDouble(10));
                    value.setMagX(cursor.getDouble(11));
                    value.setMagY(cursor.getDouble(12));
                    value.setMagZ(cursor.getDouble(13));
                } while (cursor.moveToNext());
            }
        }
        //db.close();
        return values;
    }

    @Override
    public ArrayList<EnviromentalValues> getLastValuesForEachBeacon() {
        return null;
    }

    @Override
    public boolean newValues(EnviromentalValues values) {
        return false;
    }

    @Override
    public EnviromentalValues getLastValuesForBeaconid(String beaconid) {
        EnviromentalValues value = null;
        SQLiteDatabase db = mLocalSQLiteDbHelper.getReadableDatabase();

        Cursor cursor = db.query( EnviromentalvaluesTable.TABLE_NAME,
                new String[] { EnviromentalvaluesTable._ID,
                        EnviromentalvaluesTable.COLUMN_NAME_IDBEACON,
                        EnviromentalvaluesTable.COLUMN_NAME_DETECTIONTIME,
                        EnviromentalvaluesTable.COLUMN_NAME_TEMPERATURE,
                        EnviromentalvaluesTable.COLUMN_NAME_HUMIDITY,
                        EnviromentalvaluesTable.COLUMN_NAME_ACCX,
                        EnviromentalvaluesTable.COLUMN_NAME_ACCY,
                        EnviromentalvaluesTable.COLUMN_NAME_ACCZ,
                        EnviromentalvaluesTable.COLUMN_NAME_GYRX,
                        EnviromentalvaluesTable.COLUMN_NAME_GYRY,
                        EnviromentalvaluesTable.COLUMN_NAME_GYRZ,
                        EnviromentalvaluesTable.COLUMN_NAME_MAGX,
                        EnviromentalvaluesTable.COLUMN_NAME_MAGY,
                        EnviromentalvaluesTable.COLUMN_NAME_MAGZ}
                , EnviromentalvaluesTable.COLUMN_NAME_IDBEACON + "=?",
                new String[] { beaconid }, null, null, null, null);

        BeaconModel beaconModel = new BeaconModelLocalImpl(context);

        if (cursor != null && cursor.getCount() != 0) {
            Log.w("value", "ok");
            cursor.moveToFirst();
            value = new EnviromentalValues();
            value.setIdEnv(cursor.getInt(0));
            value.setBeacon(beaconModel.getBeaconById(cursor.getString(1)));
            value.setTime(cursor.getString(2));
            value.setTemperature(cursor.getDouble(3));
            value.setHumidity(cursor.getDouble(4));
            value.setAccX(cursor.getDouble(5));
            value.setAccY(cursor.getDouble(6));
            value.setAccZ(cursor.getDouble(7));
            value.setGyrX(cursor.getDouble(8));
            value.setGyrY(cursor.getDouble(9));
            value.setGyrZ(cursor.getDouble(10));
            value.setMagX(cursor.getDouble(11));
            value.setMagY(cursor.getDouble(12));
            value.setMagZ(cursor.getDouble(13));
        }
        return value;
    }
}
