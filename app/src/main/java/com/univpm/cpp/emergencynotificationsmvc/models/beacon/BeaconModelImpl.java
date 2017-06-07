package com.univpm.cpp.emergencynotificationsmvc.models.beacon;

import com.univpm.cpp.emergencynotificationsmvc.models.envValues.EnviromentalValues;
import com.univpm.cpp.emergencynotificationsmvc.models.user.User;
import com.univpm.cpp.emergencynotificationsmvc.utils.DbUtils;
import com.univpm.cpp.emergencynotificationsmvc.utils.HttpUtils;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

/**
 * Created by matteo on 24/04/17.
 */

public class BeaconModelImpl implements BeaconModel {

    @Override
    public Beacon getBeaconById (String idBeacon) {

        Beacon beacon = null;
        String response = null;
        try {
            response = HttpUtils.sendGet("beacon/" + idBeacon);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (response != null){
            beacon = new Beacon(response);
        }
        return beacon;
    }

    @Override
    public ArrayList<Beacon> getAllBeacons() {
        ArrayList<Beacon> values = null;
        String response = null;
        try {
            response = HttpUtils.sendGet("beacon");
        } catch (Exception e) {
            e.printStackTrace();
        }

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
