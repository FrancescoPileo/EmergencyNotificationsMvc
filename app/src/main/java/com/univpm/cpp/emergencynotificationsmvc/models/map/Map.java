package com.univpm.cpp.emergencynotificationsmvc.models.map;

import android.media.Image;

/**
 * Le mappe vengono caricate nella cartella drawable e nel db c'è solo il nome dell'immagine
 **/



public class Map {

    private int idMap;
    private String building;
    private String floor;
    private String name;
    private String imagePath;
    private float scale;
    private int xRef;
    private int yRef;
    private int xRefpx;
    private int yRefpx;


    public Map() {

        super();
        this.building = null;
        this.scale = 1;
        this.floor = null;
        this.name = null;
        this.imagePath = null;
    }

    public int getIdMap() {
        return idMap;
    }

    public void setIdMap(int idMap) {
        this.idMap = idMap;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImagePath() { return imagePath; }

    public void setImagePath(String imagePath) { this.imagePath = imagePath; }

    public int getxRef() {
        return xRef;
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public void setxRef(int xRef) {
        this.xRef = xRef;
    }

    public int getyRef() {
        return yRef;
    }

    public void setyRef(int yRef) {
        this.yRef = yRef;
    }

    public int getxRefpx() {
        return xRefpx;
    }

    public void setxRefpx(int xRefpx) {
        this.xRefpx = xRefpx;
    }

    public int getyRefpx() {
        return yRefpx;
    }

    public void setyRefpx(int yRefpx) {
        this.yRefpx = yRefpx;
    }
}

