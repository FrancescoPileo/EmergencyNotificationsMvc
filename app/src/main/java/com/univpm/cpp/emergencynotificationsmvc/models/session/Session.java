package com.univpm.cpp.emergencynotificationsmvc.models.session;

//todo vedere come funzionano le date
public class Session {

    protected int id;
    protected String username;
    protected String timeIn;
    protected String timeOut;

    public Session(){
        this.id = -1;
        this.username = null;
        this.timeIn = null;
        this.timeOut = null;
    }

    public Session(int id, String username, String timeIn, String timeOut) {
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

    public String getTimeIn() {
        return timeIn;
    }

    public void setTimeIn(String timeIn) {
        this.timeIn = timeIn;
    }

    public String getTimeOut() {
        return timeOut;
    }

    public void setTimeOut(String timeOut) {
        this.timeOut = timeOut;
    }
}
