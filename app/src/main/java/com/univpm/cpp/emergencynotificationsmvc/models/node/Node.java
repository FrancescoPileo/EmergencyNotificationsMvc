package com.univpm.cpp.emergencynotificationsmvc.models.node;

/**
 * Created by marcociotti on 09/05/17.
 */

public class Node {

    int idNode;
    int idMap;
    int x;
    int y;

    public Node() {

        super();
        this.idNode = -1;
        this.idMap = -1;
        this.x = -1;
        this.y = -1;
    }

    public Node(int idNode, int idMap, int x, int y) {
        this.idNode = idNode;
        this.idMap = idMap;
        this.x = x;
        this.y = y;
    }

    public int getIdNode() {
        return idNode;
    }

    public void setIdNode(int idNode) {
        this.idNode = idNode;
    }

    public int getIdMap() {
        return idMap;
    }

    public void setIdMap(int idMap) {
        this.idMap = idMap;
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
}
