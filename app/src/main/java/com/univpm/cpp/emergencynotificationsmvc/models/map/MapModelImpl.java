package com.univpm.cpp.emergencynotificationsmvc.models.map;

import com.univpm.cpp.emergencynotificationsmvc.utils.DbUtils;

import java.util.ArrayList;
import java.util.List;


public class MapModelImpl implements MapModel {

    @Override
    public Map getMapById(int idMap) {

        Map map = null;
        map = DbUtils.getMapById(idMap);
        return map;
    }

    @Override
    public Map getMapByFloor(String building, String floor) {

        Map map = null;
        map = DbUtils.getMapByFloor(building, floor);
        return map;
    }

    @Override
    public ArrayList<Map> getAllMaps () {

        ArrayList<Map> list = new ArrayList<Map>();
        list = DbUtils.getAllMaps();
        return list;
    }
}
