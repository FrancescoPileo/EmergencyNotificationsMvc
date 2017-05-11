package com.univpm.cpp.emergencynotificationsmvc.models.position;

/**
 * Created by marcociotti on 11/05/17.
 */

public class Position {

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
}
