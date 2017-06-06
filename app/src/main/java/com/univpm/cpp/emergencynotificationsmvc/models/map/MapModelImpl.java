package com.univpm.cpp.emergencynotificationsmvc.models.map;

import com.univpm.cpp.emergencynotificationsmvc.models.user.User;
import com.univpm.cpp.emergencynotificationsmvc.utils.DbUtils;
import com.univpm.cpp.emergencynotificationsmvc.utils.HttpUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class MapModelImpl implements MapModel {

    @Override
    public Map getMapById(int idMap) {

        Map map = null;
        String response = null;
        try {
            response = HttpUtils.sendGet("map/" + idMap);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (response != null){
            map = new Map(response);
        }

        return map;
    }

    //todo da fare sul server
    @Override
    public Map getMapByFloor(String building, String floor) {

        Map map = null;
        //map = DbUtils.getMapByFloor(building, floor);
        return map;
    }

    @Override
    public Map getMapByName (String mapName) {

        Map map = null;
        String response = null;
        try {
            response = HttpUtils.sendGet("map/mapname/" + mapName);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (response != null){
            map = new Map(response);
        }

        return map;
    }

    @Override
    public ArrayList<Map> getAllMaps () {

        ArrayList<Map> maps = null;
        String response = null;
        try {
            response = HttpUtils.sendGet("map");
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (response != null){
            try {
                maps = new ArrayList<>();
                JSONArray jsonArray = new JSONArray(response);
                for (int i = 0; i < jsonArray.length(); i++) {
                    maps.add(new Map(jsonArray.getJSONObject(i).toString()));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        return maps;
    }

    @Override
    public ArrayList<String> getAllNames () {

        ArrayList<String> mapNameList = null;
        ArrayList<Map> mapList = getAllMaps();
        if (mapList != null) {
            mapNameList = new ArrayList<>();
            for (Map map: mapList){
                mapNameList.add(map.getName());
            }
        }
        return mapNameList;
    }

}
