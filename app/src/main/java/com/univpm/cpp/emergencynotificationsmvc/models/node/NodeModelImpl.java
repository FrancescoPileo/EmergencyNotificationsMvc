package com.univpm.cpp.emergencynotificationsmvc.models.node;

import com.univpm.cpp.emergencynotificationsmvc.utils.DbUtils;

import java.util.ArrayList;

/**
 * Created by marcociotti on 09/05/17.
 */

public class NodeModelImpl implements NodeModel {

    @Override
    public Node getNodeById(int idNode) {
        return DbUtils.getNodeById(idNode);
    }

    @Override
    public ArrayList<Node> getNodeByMap(int idMap) {
        return DbUtils.getNodeByMap(idMap);
    }
}
