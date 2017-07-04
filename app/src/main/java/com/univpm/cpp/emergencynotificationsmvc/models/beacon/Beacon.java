package com.univpm.cpp.emergencynotificationsmvc.models.beacon;

import android.util.Log;

import com.univpm.cpp.emergencynotificationsmvc.models.Jsonable;
import com.univpm.cpp.emergencynotificationsmvc.models.node.Node;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Classe che modella un sernsore Beacon
 */
public class Beacon implements Jsonable{

    private String idBeacon; //identificativo univoco del sensore
    private Node node;       //node in cui è installato il sensore
    private String emergency;   //status di emergenza rilevato dal beacon

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

    /**
     * Costruttore della classe Beacon
     * @param jsonString Stringa che contiente la rappresentazione JSON dell'oggetto da istanziare
     */
    public Beacon(String jsonString){
        JSONObject jsonObject = null;
        try {
            Log.w("Beaconnnnn: ", jsonString);
            jsonObject = new JSONObject(jsonString);
            this.idBeacon = jsonObject.getString("idbeacon");
            this.node = new Node(jsonObject.getJSONObject("idnode").toString());
            this.emergency = jsonObject.optString("emergency", null);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Metodo che produce il JSONObject che rappresenta l'oggetto
     * @return JSONOBject che rappresenta l'oggetto
     */
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
