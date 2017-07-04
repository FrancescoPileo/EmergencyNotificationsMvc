package com.univpm.cpp.emergencynotificationsmvc.models.envValues;


import com.univpm.cpp.emergencynotificationsmvc.utils.Broadcaster;
import com.univpm.cpp.emergencynotificationsmvc.utils.HttpUtils;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

/**
 * Implementazione del modello dei dati ambientali che fa riferimento al server REST
 */
public class EnviromentalValuesModelImpl implements EnviromentalValuesModel {

    private Broadcaster broadcaster;    //Classe capace di inviare messaggi broadcast
    private HttpUtils httpUtils;        //Classe che gestisce la connessione HTTP

    public EnviromentalValuesModelImpl(Broadcaster broadcaster){
        this.broadcaster = broadcaster;
        this.httpUtils = new HttpUtils(this.broadcaster);
    }

    @Override
    public boolean newValues(EnviromentalValues values) {

        return httpUtils.sendPost("enviromentalvalues", values);

    }

    @Override
    public ArrayList<EnviromentalValues> getAllValues() {
        ArrayList<EnviromentalValues> values = null;

        String response = httpUtils.sendGet("enviromentalvalues");

        if (response != null){
            try {
                values = new ArrayList<>();
                JSONArray jsonArray = new JSONArray(response);
                for (int i = 0; i < jsonArray.length(); i++) {
                    values.add(new EnviromentalValues(jsonArray.getJSONObject(i).toString()));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return values;
    }

    @Override
    public ArrayList<EnviromentalValues> getLastValuesForEachBeacon() {
        ArrayList<EnviromentalValues> values = null;

        String response = httpUtils.sendGet("enviromentalvalues/lasts");

        if (response != null){
            try {
                values = new ArrayList<>();
                JSONArray jsonArray = new JSONArray(response);
                for (int i = 0; i < jsonArray.length(); i++) {
                    values.add(new EnviromentalValues(jsonArray.getJSONObject(i).toString()));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return values;
    }

    @Override
    public EnviromentalValues getLastValuesForBeaconid(String beaconid) {

        EnviromentalValues enviromentalValues = null;
        String response = httpUtils.sendGet("enviromentalvalues/beacon/" + beaconid + "/last");

        if (response != null){
            enviromentalValues = new EnviromentalValues(response);
        }

        return enviromentalValues;
    }
}
