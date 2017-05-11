package com.univpm.cpp.emergencynotificationsmvc.models.node;

import java.util.ArrayList;

/**
 * Created by marcociotti on 09/05/17.
 */

public interface NodeModel {

    public Node getNodeById(int idNode);

    public ArrayList<Node> getNodeByMap (int idMap);

}
