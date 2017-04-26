package com.univpm.cpp.emergencynotificationsmvc.models.map;


import java.util.ArrayList;

public interface MapModel {

    public Map getMapById(int idMap);

    public Map getMapByFloor (String building, String floor);

    public Map getMapByName (String name);

    public ArrayList<Map> getAllMaps();

    public ArrayList<String> getAllNames();

}
