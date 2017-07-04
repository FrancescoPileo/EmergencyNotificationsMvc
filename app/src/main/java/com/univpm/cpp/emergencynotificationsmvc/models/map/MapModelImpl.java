package com.univpm.cpp.emergencynotificationsmvc.models.map;

import com.univpm.cpp.emergencynotificationsmvc.utils.Broadcaster;
import com.univpm.cpp.emergencynotificationsmvc.utils.HttpUtils;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

/**
 * Implementazione del modello delle mappe che fa riferimento al server REST
 */
public class MapModelImpl implements MapModel {

    private Broadcaster broadcaster;    //Classe capace di inviare messaggi broadcast
    private HttpUtils httpUtils;        //Classe che gestisce la connessione HTTP

    public MapModelImpl(Broadcaster broadcaster){
        this.broadcaster = broadcaster;
        this.httpUtils = new HttpUtils(this.broadcaster);
    }

    @Override
    public Map getMapById(int idMap) {

        Map map = null;
        String response = httpUtils.sendGet("map/" + idMap);

        if (response != null){
            map = new Map(response);
        }

        return map;
    }


    @Override
    public Map getMapByName (String mapName) {

        Map map = null;

        String response = httpUtils.sendGet("map/mapname/" + mapName);

        if (response != null){
            map = new Map(response);
        }

        return map;
    }

    @Override
    public ArrayList<Map> getAllMaps () {

        ArrayList<Map> maps = null;
        String response = httpUtils.sendGet("map");

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
