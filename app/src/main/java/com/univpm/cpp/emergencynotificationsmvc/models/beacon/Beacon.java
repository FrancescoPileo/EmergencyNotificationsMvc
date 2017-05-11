package com.univpm.cpp.emergencynotificationsmvc.models.beacon;

/**
 * Created by matteo on 24/04/17.
 */

public class Beacon {

    private String idBeacon;
    private int idNode;

    public Beacon() {

        super();
        this.idNode = -1;
    }

    public Beacon(String idBeacon, int idNode) {
        this.idBeacon = idBeacon;
        this.idNode = idNode;
    }

    public String getIdBeacon() {
        return idBeacon;
    }

    public void setIdBeacon(String idBeacon) {
        this.idBeacon = idBeacon;
    }

    public int getIdNode() {
        return idNode;
    }

    public void setIdNode(int idNode) {
        this.idNode = idNode;
    }
}
