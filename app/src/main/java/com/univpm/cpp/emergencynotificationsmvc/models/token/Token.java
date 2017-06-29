package com.univpm.cpp.emergencynotificationsmvc.models.token;

import com.univpm.cpp.emergencynotificationsmvc.models.Jsonable;

import org.json.JSONException;
import org.json.JSONObject;

public class Token implements Jsonable{

    private int idToken;
    private String token;

    public Token(int idToken, String token) {
        this.idToken = idToken;
        this.token = token;
    }

    public int getIdToken() {
        return idToken;
    }

    public void setIdToken(int idToken) {
        this.idToken = idToken;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public JSONObject toJson() {
            JSONObject jsonObject = new JSONObject();
            try {
                if (this.idToken != -1) jsonObject.put("idtoken", getIdToken());
                jsonObject.put("token", getToken());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return jsonObject;
    }

}
