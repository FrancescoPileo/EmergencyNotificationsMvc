package com.univpm.cpp.emergencynotificationsmvc.models.beacon;

import java.util.ArrayList;

/**
 * Interfaccia che rappresenta il modello dei beacon
 */
public interface BeaconModel {

    /**
     * Ottiene il beacon con l'idbeacon specificato
     * @param idBeacon
     * @return
     */
    public Beacon getBeaconById(String idBeacon);

    /**
     * Ottiene un'array contenente tutti i beacon presenti
     * @return
     */
    public ArrayList<Beacon> getAllBeacons();

}
