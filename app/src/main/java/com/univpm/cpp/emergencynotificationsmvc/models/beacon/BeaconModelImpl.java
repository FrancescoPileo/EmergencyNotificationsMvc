package com.univpm.cpp.emergencynotificationsmvc.models.beacon;

import com.univpm.cpp.emergencynotificationsmvc.utils.DbUtils;

import java.util.ArrayList;

/**
 * Created by matteo on 24/04/17.
 */

public class BeaconModelImpl implements BeaconModel {

    @Override
    public Beacon getBeaconById (String idBeacon) {

        Beacon beacon = null;
        beacon = DbUtils.getBeaconById(idBeacon);
        return beacon;
    }

}
