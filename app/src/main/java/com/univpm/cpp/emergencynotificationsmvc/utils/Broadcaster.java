package com.univpm.cpp.emergencynotificationsmvc.utils;

import android.content.Intent;

/**
 * Interfaccia che rappresenta un oggetto capace di inviare broadcast mediante il metodo
 * sendBroadcast(...)
 */
public interface Broadcaster {

    public void sendBroadcast(Intent intent);

}
