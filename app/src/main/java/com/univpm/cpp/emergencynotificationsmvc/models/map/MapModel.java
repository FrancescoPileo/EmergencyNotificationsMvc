package com.univpm.cpp.emergencynotificationsmvc.models.map;


/**
 * Created by marcociotti on 24/04/17.
 */

public interface MapModel {

    public Map getMapById(int idMap);

    public Map getMapByFloor (String building, String floor);

    public boolean newMap(Map map);

    public boolean updateMap(Map map);

    public boolean deleteMap(int idMap);
}
