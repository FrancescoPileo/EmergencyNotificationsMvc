package com.univpm.cpp.emergencynotificationsmvc.models.beacon;

/**
 * Created by matteo on 24/04/17.
 */

public class Beacon {

    private int idBeacon;
    private int idMap;
    private int x;
    private int y;

    public Beacon() {

        super();
        this.idMap = -1;
        this.x = -1;
        this.y = -1;
    }

    public int getIdBeacon() {
        return idBeacon;
    }

    public void setIdBeacon(int idBeacon) {
        this.idBeacon = idBeacon;
    }

    public int getIdMap() {
        return idMap;
    }

    public void setIdMap(int idMap) {
        this.idMap = idMap;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}
