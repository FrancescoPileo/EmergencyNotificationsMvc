package com.univpm.cpp.emergencynotificationsmvc.utils;

import android.os.Environment;

import java.io.File;

public class Directories {

    public final static String MAIN = Environment.getExternalStorageDirectory() + File.separator + "EmergencyNotifications";

    public final static String MAPS = MAIN + File.separator + "maps";

    public final static String NOMEDIA  = MAIN + File.separator + ".nomedia";

}
