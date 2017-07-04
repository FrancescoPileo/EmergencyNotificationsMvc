package com.univpm.cpp.emergencynotificationsmvc.models.session;

import com.univpm.cpp.emergencynotificationsmvc.models.Jsonable;
import com.univpm.cpp.emergencynotificationsmvc.models.user.User;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Classe che modella una sessione
 */
public class Session implements Jsonable {

    protected int id;
    protected User user;
    protected String timeIn;
    protected String timeOut;

    public Session(){
        this.id = -1;
        this.user = null;
        this.timeIn = null;
        this.timeOut = null;
    }

    public Session(int id, User user, String timeIn, String timeOut) {
        this.id = id;
        this.user = user;
        this.timeIn = timeIn;
        this.timeOut = timeOut;
    }

    /**
     * Costruttore della classe Session
     * @param jsonString Stringa che contiente la rappresentazione JSON dell'oggetto da istanziare
     */
    public Session(String jsonString){
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            User user1 = new User(jsonObject.getJSONObject("username").toString());
            this.id = jsonObject.getInt("idsession");
            this.user = user1;
            this.timeIn = jsonObject.getString("sessiontimestart");
            this.timeOut = jsonObject.optString("sessiontimestop", null);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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

    /**
     * Metodo che produce il JSONObject che rappresenta l'oggetto
     * @return JSONOBject che rappresenta l'oggetto
     */
    @Override
    public JSONObject toJson() {

        JSONObject jsonObject = new JSONObject();
        try {
            if (this.getId() != -1) jsonObject.accumulate("idsession", this.getId());
            jsonObject.accumulate("username", this.getUser().toJson());
            jsonObject.accumulate("sessiontimestart", this.getTimeIn());
            jsonObject.accumulate("sessiontimestop", this.getTimeOut());
        }

        catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    @Override
    public String toString() {
        return "Session{" +
                "id=" + id +
                ", user=" + user +
                ", timeIn='" + timeIn + '\'' +
                ", timeOut='" + timeOut + '\'' +
                '}';
    }
}
