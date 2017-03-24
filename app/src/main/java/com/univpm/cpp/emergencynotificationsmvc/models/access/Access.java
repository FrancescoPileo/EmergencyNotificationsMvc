package com.univpm.cpp.emergencynotificationsmvc.models.access;

import java.util.Date;

//todo vedere come funzionano le date
public class Access {

    protected int id;
    protected String username;
    protected Date timeIn;
    protected Date timeOut;

    public Access(){
        this.id = -1;
        this.username = null;
        this.timeIn = null;
        this.timeOut = null;
    }

    public Access(int id, String username, Date timeIn, Date timeOut) {
        this.id = id;
        this.username = username;
        this.timeIn = timeIn;
        this.timeOut = timeOut;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Date getTimeIn() {
        return timeIn;
    }

    public void setTimeIn(Date timeIn) {
        this.timeIn = timeIn;
    }

    public Date getTimeOut() {
        return timeOut;
    }

    public void setTimeOut(Date timeOut) {
        this.timeOut = timeOut;
    }
}
