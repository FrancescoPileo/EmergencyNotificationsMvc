package com.univpm.cpp.emergencynotificationsmvc.models.map;


import java.util.ArrayList;

/**
 * Interfaccia che rappresesnta il modello delle mappe
 */
public interface MapModel {

    /**
     * Ottiene la mappa con l'id specificato
     * @param idMap id della mappa
     * @return
     */
    public Map getMapById(int idMap);

    /**
     * Ottiene la mappa con il nome specificato
     * @param name nome della mappa
     * @return
     */
    public Map getMapByName (String name);

    /**
     * Ottiene tutte le mappe presenti
     * @return
     */
    public ArrayList<Map> getAllMaps();

    /**
     * Ottiene il nome di tutte le mappe presenti
     * @return
     */
    public ArrayList<String> getAllNames();

}
