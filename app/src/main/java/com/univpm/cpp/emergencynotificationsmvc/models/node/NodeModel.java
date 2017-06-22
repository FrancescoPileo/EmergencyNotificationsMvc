package com.univpm.cpp.emergencynotificationsmvc.models.node;

import java.util.ArrayList;



public interface NodeModel {

    public Node getNodeById(int idNode);

    public ArrayList<Node> getNodeByMap (int idMap);

    public ArrayList<Node> getAllNodes();

}
