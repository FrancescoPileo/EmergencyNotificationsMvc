package com.univpm.cpp.emergencynotificationsmvc.models.map;


import java.util.ArrayList;

public interface MapModel {

    public Map getMapById(int idMap);

    public Map getMapByFloor (String building, String floor);

    public ArrayList<Map> getAllMaps();

}
