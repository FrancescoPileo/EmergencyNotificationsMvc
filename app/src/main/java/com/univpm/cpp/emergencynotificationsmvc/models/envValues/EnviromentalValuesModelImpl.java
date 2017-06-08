package com.univpm.cpp.emergencynotificationsmvc.models.envValues;

import com.univpm.cpp.emergencynotificationsmvc.models.node.Node;
import com.univpm.cpp.emergencynotificationsmvc.utils.DbUtils;
import com.univpm.cpp.emergencynotificationsmvc.utils.HttpUtils;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class EnviromentalValuesModelImpl implements EnviromentalValuesModel {

    @Override
    public boolean newValues(EnviromentalValues values) {

        Boolean success = false;
        try {
            success = HttpUtils.sendPost("enviromentalvalues", values);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return success;
    }

    @Override
    public ArrayList<EnviromentalValues> getAllValues() {
        ArrayList<EnviromentalValues> values = null;
        String response = null;
        try {
            response = HttpUtils.sendGet("enviromentalvalues");
        } catch (Exception e) {
            e.printStackTrace();
        }

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
        String response = null;
        try {
            response = HttpUtils.sendGet("enviromentalvalues/lasts");
        } catch (Exception e) {
            e.printStackTrace();
        }

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
}
