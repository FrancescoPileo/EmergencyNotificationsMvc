package com.univpm.cpp.emergencynotificationsmvc.models.map;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.univpm.cpp.emergencynotificationsmvc.models.local.LocalSQLiteContract.*;
import com.univpm.cpp.emergencynotificationsmvc.models.local.LocalSQLiteDbHelper;

import java.util.ArrayList;

public class MapModelLocalImpl implements MapModel {

    LocalSQLiteDbHelper mLocalSQLiteDbHelper = null;

    public MapModelLocalImpl(Context context){
        mLocalSQLiteDbHelper = LocalSQLiteDbHelper.getInstance(context);
        Log.w("LocalMapModel", "ok");
    }

    @Override
    public Map getMapById(int idMap) {
        Map map = null;
        SQLiteDatabase db = mLocalSQLiteDbHelper.getReadableDatabase();

        Cursor cursor = db.query( MapTable.TABLE_NAME,
                new String[] { MapTable._ID, MapTable.COLUMN_NAME_MAPNAME, MapTable.COLUMN_NAME_BUILDING,
                MapTable.COLUMN_NAME_FLOOR, MapTable.COLUMN_NAME_MAPSCALE,
                MapTable.COLUMN_NAME_XREF, MapTable.COLUMN_NAME_XREFPX,
                MapTable.COLUMN_NAME_YREF, MapTable.COLUMN_NAME_YREFPX}
                , MapTable._ID + "=?",
                new String[] { String.valueOf(idMap) }, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            map = new Map();
            map.setIdMap(cursor.getInt(0));
            map.setName(cursor.getString(1));
            map.setBuilding(cursor.getString(2));
            map.setFloor(cursor.getString(3));
            map.setScale(cursor.getDouble(4));
            map.setxRef(cursor.getInt(5));
            map.setxRefpx(cursor.getInt(6));
            map.setyRef(cursor.getInt(7));
            map.setyRefpx(cursor.getInt(8));
        }
        //db.close();
        return map;
    }

    @Override
    public Map getMapByName(String name) {
        Map map = null;
        SQLiteDatabase db = mLocalSQLiteDbHelper.getReadableDatabase();

        Cursor cursor = db.query( MapTable.TABLE_NAME,
                new String[] { MapTable._ID, MapTable.COLUMN_NAME_MAPNAME, MapTable.COLUMN_NAME_BUILDING,
                        MapTable.COLUMN_NAME_FLOOR, MapTable.COLUMN_NAME_MAPSCALE,
                        MapTable.COLUMN_NAME_XREF, MapTable.COLUMN_NAME_XREFPX,
                        MapTable.COLUMN_NAME_YREF, MapTable.COLUMN_NAME_YREFPX}
                , MapTable.COLUMN_NAME_MAPNAME+ "=?",
                new String[] { name }, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            map = new Map();
            map.setIdMap(cursor.getInt(0));
            map.setName(cursor.getString(1));
            map.setBuilding(cursor.getString(2));
            map.setFloor(cursor.getString(3));
            map.setScale(cursor.getDouble(4));
            map.setxRef(cursor.getInt(5));
            map.setxRefpx(cursor.getInt(6));
            map.setyRef(cursor.getInt(7));
            map.setyRefpx(cursor.getInt(8));
            Log.w("offlineMap", map.toString());
        }
        //db.close();
        return map;
    }

    @Override
    public Map getMapByFloor(String building, String floor) {
        return null;
    }

    @Override
    public ArrayList<Map> getAllMaps() {
        ArrayList<Map> maps = null;
        String selectQuery = "SELECT * FROM " + MapTable.TABLE_NAME;
        SQLiteDatabase db = mLocalSQLiteDbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                maps = new ArrayList<>();
                do {
                    Map map = new Map();
                    map.setIdMap(cursor.getInt(0));
                    map.setBuilding(cursor.getString(1));
                    map.setFloor(cursor.getString(2));
                    map.setName(cursor.getString(3));
                    map.setScale(cursor.getDouble(4));
                    map.setxRef(cursor.getInt(5));
                    map.setxRefpx(cursor.getInt(6));
                    map.setyRef(cursor.getInt(7));
                    map.setyRefpx(cursor.getInt(8));
                    maps.add(map);
                } while (cursor.moveToNext());
            }
        }
        //db.close();
        return maps;
    }

    @Override
    public ArrayList<String> getAllNames() {
        ArrayList <String> names = null;
        String selectQuery = "SELECT " + MapTable.COLUMN_NAME_MAPNAME + " FROM " + MapTable.TABLE_NAME;
        SQLiteDatabase db = mLocalSQLiteDbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                names = new ArrayList<>();
                do {
                    names.add(cursor.getString(0));
                } while (cursor.moveToNext());
            }
        }
        //db.close();
        return names;
    }
}
