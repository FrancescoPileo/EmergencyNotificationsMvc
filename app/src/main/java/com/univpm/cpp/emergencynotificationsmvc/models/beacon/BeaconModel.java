package com.univpm.cpp.emergencynotificationsmvc.models.beacon;

import java.util.ArrayList;

/**
 * Created by matteo on 24/04/17.
 */

public interface BeaconModel {

    public Beacon getBeaconById(int idBeacon);

    public ArrayList<Beacon> getBeaconsByMap(int idMap);

}
