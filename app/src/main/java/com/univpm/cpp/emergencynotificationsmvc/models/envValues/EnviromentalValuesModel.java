package com.univpm.cpp.emergencynotificationsmvc.models.envValues;

import java.util.ArrayList;

/**
 * Interfaccia che rappresetna il modello dei valori ambientali
 */
public interface EnviromentalValuesModel {

    /**
     * Aggiunge dei nuovi valori ambientali
     * @param values nuovi valori ambientali
     * @return true: valori aggiunti, false: valori non aggiunti
     */
    public boolean newValues(EnviromentalValues values);

    /**
     * Ottiene tutti i valori ambientali presenti
     * @return
     */
    public ArrayList<EnviromentalValues> getAllValues();

    /**
     * Ottiene l'ultimo valore ambientale registrato per ogni beacon presente
     * @return
     */
    public ArrayList<EnviromentalValues> getLastValuesForEachBeacon();

    /**
     * Ottiene l'ultimo valore ambientale registrato nel beacon specificato
     * @param beaconid id del beacon specificato
     * @return
     */
    public EnviromentalValues getLastValuesForBeaconid(String beaconid);

}
