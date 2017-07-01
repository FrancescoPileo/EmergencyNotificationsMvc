package com.univpm.cpp.emergencynotificationsmvc.models.beacon;

import android.util.Log;

import com.univpm.cpp.emergencynotificationsmvc.utils.Broadcaster;
import com.univpm.cpp.emergencynotificationsmvc.utils.HttpUtils;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

/**
 * Created by matteo on 24/04/17.
 */

public class BeaconModelImpl implements BeaconModel {

    Broadcaster broadcaster;
    HttpUtils httpUtils;

    public BeaconModelImpl(Broadcaster broadcaster){

        this.broadcaster = broadcaster;
        this.httpUtils = new HttpUtils(this.broadcaster);

    }

    @Override
    public Beacon getBeaconById (String idBeacon) {

        Beacon beacon = null;

        String response = httpUtils.sendGet("beacon/" + idBeacon);

        if (response != null){
            beacon = new Beacon(response);
        }
        return beacon;
    }

    @Override
    public ArrayList<Beacon> getAllBeacons() {
        ArrayList<Beacon> values = null;

        String response = httpUtils.sendGet("beacon/getAll");

        if (response != null){
            try {
                values = new ArrayList<>();
                JSONArray jsonArray = new JSONArray(response);
                for (int i = 0; i < jsonArray.length(); i++) {
                    values.add(new Beacon(jsonArray.getJSONObject(i).toString()));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return values;
    }

}
