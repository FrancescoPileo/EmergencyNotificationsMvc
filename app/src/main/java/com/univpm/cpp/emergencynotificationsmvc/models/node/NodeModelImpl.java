package com.univpm.cpp.emergencynotificationsmvc.models.node;

import com.univpm.cpp.emergencynotificationsmvc.models.map.Map;
import com.univpm.cpp.emergencynotificationsmvc.utils.DbUtils;
import com.univpm.cpp.emergencynotificationsmvc.utils.HttpUtils;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

/**
 * Created by marcociotti on 09/05/17.
 */

public class NodeModelImpl implements NodeModel {

    @Override
    public Node getNodeById(int idNode) {

        Node node = null;
        String response = null;
        try {
            response = HttpUtils.sendGet("node/id/" + idNode);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //user = DbUtils.getUser(username);
        if (response != null){
            node = new Node(response);
        }
        return node;    }

    @Override
    public ArrayList<Node> getAllNodes() {
        ArrayList<Node> nodes = null;
        String response = null;
        try {
            response = HttpUtils.sendGet("node");
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (response != null){
            try {
                nodes = new ArrayList<>();
                JSONArray jsonArray = new JSONArray(response);
                for (int i = 0; i < jsonArray.length(); i++) {
                    nodes.add(new Node(jsonArray.getJSONObject(i).toString()));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return nodes;
    }

    @Override
    public ArrayList<Node> getNodeByMap(int idMap) {


        return null;//DbUtils.getNodeByMap(idMap);
    }
}