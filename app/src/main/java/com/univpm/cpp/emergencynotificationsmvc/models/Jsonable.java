package com.univpm.cpp.emergencynotificationsmvc.models;

import org.json.JSONObject;


/**
 * Interfaccia che rappresenta una classe che può essere rappresentata da un oggetto di ripo
 * JSONObject mediante il metodo toJson()
 */
public interface Jsonable {

    public JSONObject toJson();

}
