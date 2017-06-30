package com.univpm.cpp.emergencynotificationsmvc.models.local;

import android.provider.BaseColumns;

public class LocalSQLiteContract {

    private LocalSQLiteContract(){}

    public static class AppuserTable implements BaseColumns {
        public static final String TABLE_NAME = "appuser";
        public static final String _ID = "iduser";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_SURNAME = "surname";
        public static final String COLUMN_NAME_USERNAME = "username";
        public static final String COLUMN_NAME_AGE = "age";
        public static final String COLUMN_NAME_MOBILEPHONE = "mobilephone";
        public static final String COLUMN_NAME_EMAIL = "email";
        public static final String COLUMN_NAME_PASSWORD = "password";
        public static final String COLUMN_NAME_ISGUEST = "isguest";
    }

    public static class UserpositionTable implements BaseColumns {
        public static final String TABLE_NAME = "userposition";
        public static final String _ID = "idposition";
        public static final String COLUMN_NAME_IDUSER = "iduser";
        public static final String COLUMN_NAME_IDNODE = "idnode";
        public static final String COLUMN_NAME_DETECTIONTIME = "detectiontime";
    }

    public static class MapTable implements BaseColumns {
        public static final String TABLE_NAME = "map";
        public static final String _ID = "idmap";
        public static final String COLUMN_NAME_BUILDING = "building";
        public static final String COLUMN_NAME_FLOOR = "floor";
        public static final String COLUMN_NAME_MAPNAME = "mapname";
        public static final String COLUMN_NAME_MAPSCALE = "mapscale";
        public static final String COLUMN_NAME_XREF = "xref";
        public static final String COLUMN_NAME_XREFPX = "xrefpx";
        public static final String COLUMN_NAME_YREF = "yref";
        public static final String COLUMN_NAME_YREFPX = "yrefpx";
    }

    public static class NodeTable implements BaseColumns {
        public static final String TABLE_NAME = "node";
        public static final String _ID = "idnode";
        public static final String COLUMN_NAME_IDMAP = "idmap";
        public static final String COLUMN_NAME_NODENAME = "nodename";
        public static final String COLUMN_NAME_X = "x";
        public static final String COLUMN_NAME_Y = "y";
    }

    public static class BeaconTable implements BaseColumns {
        public static final String TABLE_NAME = "beacon";
        public static final String _ID = "idbeacon";
        public static final String COLUMN_NAME_IDNODE = "idnode";
        public static final String COLUMN_NAME_EMERGENCY = "emergency";
    }

    public static class EnviromentalvaluesTable implements BaseColumns {
        public static final String TABLE_NAME = "enviromentalvalues";
        public static final String _ID = "idenv";
        public static final String COLUMN_NAME_IDBEACON = "idbeacon";
        public static final String COLUMN_NAME_DETECTIONTIME = "detectiontime";
        public static final String COLUMN_NAME_TEMPERATURE = "temperature";
        public static final String COLUMN_NAME_HUMIDITY = "humidity";
        public static final String COLUMN_NAME_ACCX = "accx";
        public static final String COLUMN_NAME_ACCY = "accy";
        public static final String COLUMN_NAME_ACCZ = "accz";
        public static final String COLUMN_NAME_GYRX = "gyrx";
        public static final String COLUMN_NAME_GYRY = "gyry";
        public static final String COLUMN_NAME_GYRZ = "gyrz";
        public static final String COLUMN_NAME_MAGX = "magx";
        public static final String COLUMN_NAME_MAGY = "magy";
        public static final String COLUMN_NAME_MAGZ = "magz";

    }





}
