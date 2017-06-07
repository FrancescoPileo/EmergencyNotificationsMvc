package com.univpm.cpp.emergencynotificationsmvc.models.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.univpm.cpp.emergencynotificationsmvc.models.beacon.Beacon;
import com.univpm.cpp.emergencynotificationsmvc.models.beacon.BeaconModel;
import com.univpm.cpp.emergencynotificationsmvc.models.envValues.EnviromentalValues;
import com.univpm.cpp.emergencynotificationsmvc.models.local.LocalSQLiteContract.*;
import com.univpm.cpp.emergencynotificationsmvc.models.map.Map;
import com.univpm.cpp.emergencynotificationsmvc.models.node.Node;
import com.univpm.cpp.emergencynotificationsmvc.models.position.Position;
import com.univpm.cpp.emergencynotificationsmvc.models.user.User;
import com.univpm.cpp.emergencynotificationsmvc.utils.Directories;

import java.io.File;
import java.util.ArrayList;

public class LocalSQLiteDbHelper extends SQLiteOpenHelper {

    private static final String SQL_CREATE_TABLE_APPUSER =
            "CREATE TABLE " + AppuserTable.TABLE_NAME + " (" +
                    AppuserTable._ID + " INTEGER PRIMARY KEY," +
                    AppuserTable.COLUMN_NAME_NAME + " TEXT," +
                    AppuserTable.COLUMN_NAME_SURNAME + " TEXT," +
                    AppuserTable.COLUMN_NAME_AGE + " INTEGER," +
                    AppuserTable.COLUMN_NAME_MOBILEPHONE + " TEXT," +
                    AppuserTable.COLUMN_NAME_EMAIL + " TEXT, " +
                    AppuserTable.COLUMN_NAME_USERNAME + " TEXT, " +
                    AppuserTable.COLUMN_NAME_PASSWORD + " TEXT," +
                    AppuserTable.COLUMN_NAME_ISGUEST + " INTEGER )";

    private static final String SQL_DELETE_TABLE_APPUSER =  "DROP TABLE IF EXISTS " + AppuserTable.TABLE_NAME;

    private static final String SQL_CREATE_TABLE_USERPOSITION =
            "CREATE TABLE " + UserpositionTable.TABLE_NAME + " (" +
                    UserpositionTable._ID + " INTEGER PRIMARY KEY," +
                    UserpositionTable.COLUMN_NAME_IDUSER + " INTEGER," +
                    UserpositionTable.COLUMN_NAME_IDNODE + " INTEGER," +
                    UserpositionTable.COLUMN_NAME_DETECTIONTIME  + " TEXT )";

    private static final String SQL_DELETE_TABLE_USERPOSITION =  "DROP TABLE IF EXISTS " + UserpositionTable.TABLE_NAME;

    private static final String SQL_CREATE_TABLE_MAP =
            "CREATE TABLE " + MapTable.TABLE_NAME + " (" +
                    MapTable._ID + " INTEGER PRIMARY KEY," +
                    MapTable.COLUMN_NAME_BUILDING + " TEXT," +
                    MapTable.COLUMN_NAME_FLOOR + " TEXT," +
                    MapTable.COLUMN_NAME_MAPNAME + " TEXT," +
                    MapTable.COLUMN_NAME_MAPSCALE + " REAL," +
                    MapTable.COLUMN_NAME_XREF + " REAL, " +
                    MapTable.COLUMN_NAME_XREFPX + " REAL, " +
                    MapTable.COLUMN_NAME_YREF + " REAL," +
                    MapTable.COLUMN_NAME_YREFPX + " REAL )";

    private static final String SQL_DELETE_TABLE_MAP =  "DROP TABLE IF EXISTS " + MapTable.TABLE_NAME;

    private static final String SQL_CREATE_TABLE_NODE =
            "CREATE TABLE " + NodeTable.TABLE_NAME + " (" +
                    NodeTable._ID + " INTEGER PRIMARY KEY," +
                    NodeTable.COLUMN_NAME_IDMAP + " INTEGER," +
                    NodeTable.COLUMN_NAME_NODENAME + " TEXT," +
                    NodeTable.COLUMN_NAME_X + " INTEGER," +
                    NodeTable.COLUMN_NAME_Y + " INTEGER )";

    private static final String SQL_DELETE_TABLE_NODE =  "DROP TABLE IF EXISTS " + NodeTable.TABLE_NAME;

    private static final String SQL_CREATE_TABLE_BEACON =
            "CREATE TABLE " + BeaconTable.TABLE_NAME + " (" +
                    BeaconTable._ID + " TEXT PRIMARY KEY," +
                    BeaconTable.COLUMN_NAME_IDNODE + " INTEGER )";

    private static final String SQL_DELETE_TABLE_BEACON =  "DROP TABLE IF EXISTS " + BeaconTable.TABLE_NAME;


    private static final String SQL_CREATE_TABLE_ENVVALUES =
            "CREATE TABLE " + EnviromentalvaluesTable.TABLE_NAME + " (" +
                    EnviromentalvaluesTable._ID + " INTEGER PRIMARY KEY," +
                    EnviromentalvaluesTable.COLUMN_NAME_IDBEACON + " TEXT," +
                    EnviromentalvaluesTable.COLUMN_NAME_DETECTIONTIME + " TEXT," +
                    EnviromentalvaluesTable.COLUMN_NAME_TEMPERATURE + " REAL," +
                    EnviromentalvaluesTable.COLUMN_NAME_HUMIDITY + " REAL," +
                    EnviromentalvaluesTable.COLUMN_NAME_ACCX + " REAL," +
                    EnviromentalvaluesTable.COLUMN_NAME_ACCY + " REAL," +
                    EnviromentalvaluesTable.COLUMN_NAME_ACCZ + " REAL," +
                    EnviromentalvaluesTable.COLUMN_NAME_GYRX + " REAL," +
                    EnviromentalvaluesTable.COLUMN_NAME_GYRY + " REAL," +
                    EnviromentalvaluesTable.COLUMN_NAME_GYRZ + " REAL," +
                    EnviromentalvaluesTable.COLUMN_NAME_MAGX + " REAL," +
                    EnviromentalvaluesTable.COLUMN_NAME_MAGY + " REAL," +
                    EnviromentalvaluesTable.COLUMN_NAME_MAGZ + " REAL )";

    private static final String SQL_DELETE_TABLE_ENVVALUES =  "DROP TABLE IF EXISTS " + EnviromentalvaluesTable.TABLE_NAME;


    public static final String[] SQL_CREATE_TABLES = {
            SQL_CREATE_TABLE_APPUSER,
            SQL_CREATE_TABLE_USERPOSITION,
            SQL_CREATE_TABLE_MAP,
            SQL_CREATE_TABLE_NODE,
            SQL_CREATE_TABLE_BEACON,
            SQL_CREATE_TABLE_ENVVALUES
    };

    public static final String[] SQL_DELETE_TABLES = {
            SQL_DELETE_TABLE_APPUSER,
            SQL_DELETE_TABLE_USERPOSITION,
            SQL_DELETE_TABLE_MAP,
            SQL_DELETE_TABLE_NODE,
            SQL_DELETE_TABLE_BEACON,
            SQL_DELETE_TABLE_ENVVALUES
    };


    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Local.db";

    public LocalSQLiteDbHelper(Context context){
        super(context, Directories.DB + File.separator + DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        for (final String SQL_QUERY: SQL_CREATE_TABLES){
            sqLiteDatabase.execSQL(SQL_QUERY);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        for (final String SQL_QUERY: SQL_DELETE_TABLES){
            db.execSQL(SQL_QUERY);
        }
        onCreate(db);
    }

    public boolean importAppuser(User user){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(AppuserTable.TABLE_NAME, null, null);
        ContentValues values = new ContentValues();
        values.put(AppuserTable._ID, user.getId());
        values.put(AppuserTable.COLUMN_NAME_NAME, user.getName());
        values.put(AppuserTable.COLUMN_NAME_SURNAME, user.getSurname());
        values.put(AppuserTable.COLUMN_NAME_AGE, user.getAge());
        values.put(AppuserTable.COLUMN_NAME_MOBILEPHONE, user.getMobilephone());
        values.put(AppuserTable.COLUMN_NAME_EMAIL, user.getEmail());
        values.put(AppuserTable.COLUMN_NAME_USERNAME, user.getUsername());
        values.put(AppuserTable.COLUMN_NAME_PASSWORD, user.getPassword());
        values.put(AppuserTable.COLUMN_NAME_ISGUEST, user.isGuest());
        long newRowId = db.insert(AppuserTable.TABLE_NAME, null, values);
        return newRowId != -1 ;
    }

    public boolean importUserposition(Position position){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(UserpositionTable.TABLE_NAME, null, null);
        ContentValues values = new ContentValues();
        values.put(UserpositionTable._ID, position.getIdPosition());
        values.put(UserpositionTable.COLUMN_NAME_IDNODE, position.getNode().getIdNode());
        values.put(UserpositionTable.COLUMN_NAME_IDUSER, position.getUser().getId());
        values.put(UserpositionTable.COLUMN_NAME_DETECTIONTIME, position.getTime());
        long newRowId = db.insert(UserpositionTable.TABLE_NAME, null, values);
        return newRowId != -1 ;
    }

    public boolean importBeacons(ArrayList<Beacon> beacons){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(BeaconTable.TABLE_NAME, null, null);
        for (Beacon beacon: beacons) {
            ContentValues values = new ContentValues();
            values.put(BeaconTable._ID, beacon.getIdBeacon());
            values.put(BeaconTable.COLUMN_NAME_IDNODE, beacon.getNode().getIdNode());
            db.insert(BeaconTable.TABLE_NAME, null, values);
        }
        return true;

    }

    public boolean importMaps(ArrayList<Map> maps){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(MapTable.TABLE_NAME, null, null);
        for (Map map: maps) {
            ContentValues values = new ContentValues();
            values.put(MapTable._ID, map.getIdMap());
            values.put(MapTable.COLUMN_NAME_MAPNAME, map.getName());
            values.put(MapTable.COLUMN_NAME_BUILDING, map.getBuilding());
            values.put(MapTable.COLUMN_NAME_FLOOR, map.getFloor());
            values.put(MapTable.COLUMN_NAME_MAPSCALE, map.getScale());
            values.put(MapTable.COLUMN_NAME_XREF, map.getxRef());
            values.put(MapTable.COLUMN_NAME_XREFPX, map.getxRefpx());
            values.put(MapTable.COLUMN_NAME_YREF, map.getyRef());
            values.put(MapTable.COLUMN_NAME_YREFPX, map.getyRefpx());
            db.insert(MapTable.TABLE_NAME, null, values);
        }
        return true;
    }

    public boolean importNodes(ArrayList<Node> nodes){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(NodeTable.TABLE_NAME, null, null);
        for (Node node: nodes) {
            ContentValues values = new ContentValues();
            values.put(NodeTable._ID, node.getIdNode());
            values.put(NodeTable.COLUMN_NAME_IDMAP, node.getMap().getIdMap());
            values.put(NodeTable.COLUMN_NAME_NODENAME, node.getNodename());
            values.put(NodeTable.COLUMN_NAME_X, node.getX());
            values.put(NodeTable.COLUMN_NAME_Y, node.getY());
            db.insert(NodeTable.TABLE_NAME, null, values);
        }
        return true;
    }

    public boolean importEnviromentalValues(ArrayList<EnviromentalValues> envValues){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(EnviromentalvaluesTable.TABLE_NAME, null, null);
        for (EnviromentalValues envValue: envValues) {
            ContentValues values = new ContentValues();
            values.put(EnviromentalvaluesTable._ID, envValue.getIdEnv());
            values.put(EnviromentalvaluesTable.COLUMN_NAME_IDBEACON, envValue.getBeacon().getIdBeacon());
            values.put(EnviromentalvaluesTable.COLUMN_NAME_DETECTIONTIME, envValue.getTime());
            values.put(EnviromentalvaluesTable.COLUMN_NAME_TEMPERATURE, envValue.getTemperature());
            values.put(EnviromentalvaluesTable.COLUMN_NAME_HUMIDITY, envValue.getHumidity());
            values.put(EnviromentalvaluesTable.COLUMN_NAME_ACCX, envValue.getAccX());
            values.put(EnviromentalvaluesTable.COLUMN_NAME_ACCY, envValue.getAccY());
            values.put(EnviromentalvaluesTable.COLUMN_NAME_ACCZ, envValue.getAccZ());
            values.put(EnviromentalvaluesTable.COLUMN_NAME_GYRX, envValue.getGyrX());
            values.put(EnviromentalvaluesTable.COLUMN_NAME_GYRY, envValue.getGyrY());
            values.put(EnviromentalvaluesTable.COLUMN_NAME_GYRZ, envValue.getGyrZ());
            values.put(EnviromentalvaluesTable.COLUMN_NAME_MAGX, envValue.getMagX());
            values.put(EnviromentalvaluesTable.COLUMN_NAME_MAGY, envValue.getMagY());
            values.put(EnviromentalvaluesTable.COLUMN_NAME_MAGZ, envValue.getMagZ());
            db.insert(EnviromentalvaluesTable.TABLE_NAME, null, values);
        }
        return true;
    }


}
