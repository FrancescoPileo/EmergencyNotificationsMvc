package com.univpm.cpp.emergencynotificationsmvc.models.map;

import android.media.Image;

/**
 * Created by marcociotti on 24/04/17.
 **/


public class Map {

    private int idMap;
    private String building;
    private String floor;
    private Image image;


    public Map() {

        super();
        this.building = null;
        this.floor = null;
        this.image = null;
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

    public Image getImage() {
        return image;
    }

    public void setImage(String pathImg) {
        this.image = image;
    }
}

