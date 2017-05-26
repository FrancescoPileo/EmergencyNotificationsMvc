package com.univpm.cpp.emergencynotificationsmvc.models.position;

import com.univpm.cpp.emergencynotificationsmvc.models.Jsonable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by marcociotti on 11/05/17.
 */

public class Position implements Jsonable{

    private int idPosition;
    private int idNode;
    private int idUser;
    private String time;

    public Position() {

        super();
        this.idNode = -1;
        this.idPosition = -1;
        this.idUser = -1;
        this.time = null;
    }

    public Position(int idPosition, int idNode, int idUser, String time) {
        this.idPosition = idPosition;
        this.idNode = idNode;
        this.idUser = idUser;
        this.time = time;
    }


    public Position(String jsonString){
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            this.idPosition = jsonObject.getInt("idposition");
            this.idNode = jsonObject.getInt("idnode");
            this.idUser = jsonObject.getInt("iduser");
            this.time = jsonObject.getString("detectiontime");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public int getIdPosition() {
        return idPosition;
    }

    public void setIdPosition(int idPosition) {
        this.idPosition = idPosition;
    }

    public int getIdNode() {
        return idNode;
    }

    public void setIdNode(int idNode) {
        this.idNode = idNode;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public JSONObject toJson() {
        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put("idposition", this.idPosition);
            jsonObject.put("iduser", this.idUser);
            jsonObject.put("idnode", this.idNode);
            jsonObject.put("detectiontime", this.time);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
}
