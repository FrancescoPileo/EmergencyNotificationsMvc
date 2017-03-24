package com.univpm.cpp.emergencynotificationsmvc.models.user;

public class UserGuest {

    protected int id;
    protected String name;

    public UserGuest(){
        this.id = -1;
        this.name = null;
    }

    public UserGuest(int id, String name){
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
