package com.univpm.cpp.emergencynotificationsmvc.models.node;

import com.univpm.cpp.emergencynotificationsmvc.models.Jsonable;
import com.univpm.cpp.emergencynotificationsmvc.models.map.Map;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by marcociotti on 09/05/17.
 */

public class Node implements Jsonable {

    private int idNode;
    private Map map;
    private int x;
    private int y;
    private String nodename;

    public Node() {

        this.idNode = -1;
        this.map = null;
        this.x = -1;
        this.y = -1;
        this.nodename = null;
    }

    public Node(int idNode, Map map, int x, int y, String nodename) {

        this.idNode = idNode;
        this.map = map;
        this.x = x;
        this.y = y;
        this.nodename = nodename;

    }

    public Node(String jsonString){
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            this.map = new Map(jsonObject.getJSONObject("idmap").toString());
            this.idNode = jsonObject.getInt("idnode");
            this.x = jsonObject.getInt("x");
            this.y = jsonObject.getInt("y");
            this.nodename = jsonObject.getString("nodename");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public int getIdNode() {
        return idNode;
    }

    public void setIdNode(int idNode) {
        this.idNode = idNode;
    }

    public Map getMap() {
        return map;
    }

    public void setMap(Map map) {
        this.map = map;
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

    public String getNodename() {
        return nodename;
    }

    public void setNodename(String nodename) {
        this.nodename = nodename;
    }

    @Override
    public JSONObject toJson() {
        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put("idnode", this.idNode);
            jsonObject.put("idmap", this.map.toJson());
            jsonObject.put("nodename", this.nodename);
            jsonObject.put("x", this.x);
            jsonObject.put("y", this.y);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
}
