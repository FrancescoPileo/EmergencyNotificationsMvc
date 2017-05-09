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


    public Map() {

        super();
        this.building = null;
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
}

