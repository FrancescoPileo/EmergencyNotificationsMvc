package com.univpm.cpp.emergencynotificationsmvc.models.beacon;

import android.util.Log;

import com.univpm.cpp.emergencynotificationsmvc.models.Jsonable;
import com.univpm.cpp.emergencynotificationsmvc.models.node.Node;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by matteo on 24/04/17.
 */

public class Beacon implements Jsonable{

    private String idBeacon;
    private Node node;
    private String emergency;

    public Beacon() {

        super();
        this.node = null;
        this.emergency = null;
    }

    public Beacon(String idBeacon, Node node, String emergency) {
        this.idBeacon = idBeacon;
        this.node = node;
        this.emergency = emergency;
    }

    public Beacon(String JsonString){
        JSONObject jsonObject = null;
        try {
            Log.w("Beaconnnnn: ", JsonString);
            jsonObject = new JSONObject(JsonString);
            this.idBeacon = jsonObject.getString("idbeacon");
            this.node = new Node(jsonObject.getJSONObject("idnode").toString());
            this.emergency = jsonObject.optString("emergency", null);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public JSONObject toJson() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("idbeacon", getIdBeacon());
            jsonObject.put("idnode", getNode().toJson());
            jsonObject.put("emergency", getEmergency());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public String getIdBeacon() {
        return idBeacon;
    }

    public void setIdBeacon(String idBeacon) {
        this.idBeacon = idBeacon;
    }

    public Node getNode() {
        return node;
    }

    public void setNode(Node node) {
        this.node = node;
    }

    public String getEmergency() {
        return emergency;
    }

    public void setEmergency(String emergency) {
        this.emergency = emergency;
    }

    @Override
    public String toString() {
        return "Beacon{" +
                "idBeacon='" + idBeacon + '\'' +
                ", node=" + node +
                ", emergency='" + emergency + '\'' +
                '}';
    }
}
