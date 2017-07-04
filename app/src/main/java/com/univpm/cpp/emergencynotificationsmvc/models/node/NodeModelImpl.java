package com.univpm.cpp.emergencynotificationsmvc.models.node;

import com.univpm.cpp.emergencynotificationsmvc.utils.Broadcaster;
import com.univpm.cpp.emergencynotificationsmvc.utils.HttpUtils;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

/**
 * Implementazione del modello dei nodi che fa riferimento al server REST
 */
public class NodeModelImpl implements NodeModel {

    private Broadcaster broadcaster;        //Classe capace di inviare messaggi broadcast
    private HttpUtils httpUtils;            //Classe che gestisce la connessione HTTP

    public NodeModelImpl(Broadcaster broadcaster){
        this.broadcaster = broadcaster;
        this.httpUtils = new HttpUtils(this.broadcaster);
    }

    @Override
    public Node getNodeById(int idNode) {

        Node node = null;
        String response = httpUtils.sendGet("node/id/" + idNode);

        if (response != null){
            node = new Node(response);
        }
        return node;    }

    @Override
    public ArrayList<Node> getAllNodes() {
        ArrayList<Node> nodes = null;
        String response = httpUtils.sendGet("node");

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

}