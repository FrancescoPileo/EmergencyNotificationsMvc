package com.univpm.cpp.emergencynotificationsmvc.models.session;

import com.univpm.cpp.emergencynotificationsmvc.models.Jsonable;

import org.json.JSONException;
import org.json.JSONObject;

//todo vedere come funzionano le date
public class Session implements Jsonable {

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

    public Session(String jsonString){
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            this.id = jsonObject.getInt("iduser");
            this.username = jsonObject.getString("username");
            this.timeIn = jsonObject.getString("sessiontimestart");
            this.timeOut = jsonObject.getString("sessiontimestop");
        } catch (JSONException e) {
            e.printStackTrace();
        }
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

    @Override
    public JSONObject toJson() {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("idsession", this.getId());
            jsonObject.put("username", this.getUsername());
            jsonObject.put("sessiontimestart", this.getTimeIn());
            jsonObject.put("sessiontimestop", this.getTimeOut());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
}
