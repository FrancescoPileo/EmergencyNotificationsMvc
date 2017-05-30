package com.univpm.cpp.emergencynotificationsmvc.models.beacon;

import com.univpm.cpp.emergencynotificationsmvc.models.user.User;
import com.univpm.cpp.emergencynotificationsmvc.utils.DbUtils;
import com.univpm.cpp.emergencynotificationsmvc.utils.HttpUtils;

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

}
