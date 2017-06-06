package com.univpm.cpp.emergencynotificationsmvc.models.node;

import com.univpm.cpp.emergencynotificationsmvc.utils.DbUtils;
import com.univpm.cpp.emergencynotificationsmvc.utils.HttpUtils;

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
    public ArrayList<Node> getNodeByMap(int idMap) {


        return null;//DbUtils.getNodeByMap(idMap);
    }
}