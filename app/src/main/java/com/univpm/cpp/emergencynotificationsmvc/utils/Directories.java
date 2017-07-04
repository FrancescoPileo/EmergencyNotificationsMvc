package com.univpm.cpp.emergencynotificationsmvc.utils;

import android.os.Environment;

import java.io.File;

/**
 * Classe statica che contiene le directory utili all'applicazione
 */
public class Directories {

    public final static String MAIN = Environment.getExternalStorageDirectory() + File.separator + "EmergencyNotifications";

    public final static String MAPS = MAIN + File.separator + "maps";

    public final static String DB = MAIN + File.separator + "db";

    public final static String[] DIRS = {
            MAIN,
            MAPS,
            DB
    };

    public final static String NOMEDIA  = MAIN + File.separator + ".nomedia";

}
