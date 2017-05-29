package com.univpm.cpp.emergencynotificationsmvc.models.map;

import android.media.Image;

import com.univpm.cpp.emergencynotificationsmvc.models.Jsonable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Le mappe vengono caricate nella cartella maps e nel db c'è solo il nome dell'immagine
 **/



public class Map implements Jsonable {

    private int idMap;
    private String building;
    private String floor;
    private String name;
    private double scale;
    private int xRef;
    private int yRef;
    private int xRefpx;
    private int yRefpx;


    public Map() {

        this.idMap = -1;
        this.building = null;
        this.floor = null;
        this.name = null;
        this.xRef = 0;
        this.xRefpx = 0;
        this.yRef = 0;
        this.yRefpx = 0;
    }

    public Map(String jsonString){
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            this.idMap = jsonObject.getInt("idmap");
            this.building = jsonObject.optString("building", null);
            this.floor = jsonObject.optString("floor", null);
            this.name = jsonObject.getString("mapname");
            this.scale = jsonObject.getDouble("mapscale");
            this.xRef = jsonObject.getInt("xref");
            this.xRefpx = jsonObject.getInt("xrefpx");
            this.yRef = jsonObject.getInt("yref");
            this.yRefpx = jsonObject.getInt("yrefpx");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public JSONObject toJson() {
        JSONObject jsonObject = new JSONObject();
        try {
            if (this.getIdMap() != -1)
                jsonObject.put("idmap", this.getIdMap());
            jsonObject.accumulate("building", this.getBuilding());
            jsonObject.accumulate("floor", this.getFloor());
            jsonObject.accumulate("mapname", this.getName());
            jsonObject.accumulate("mapscale", this.getScale());
            jsonObject.accumulate("xref", this.getxRef());
            jsonObject.accumulate("xrefpx", this.getxRefpx());
            jsonObject.accumulate("yref", this.getyRef());
            jsonObject.accumulate("yrefpx", this.getyRefpx());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
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

    public int getxRef() {
        return xRef;
    }

    public double getScale() {
        return scale;
    }

    public void setScale(double scale) {
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

