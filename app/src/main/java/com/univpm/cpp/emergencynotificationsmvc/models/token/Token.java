package com.univpm.cpp.emergencynotificationsmvc.models.token;

import com.univpm.cpp.emergencynotificationsmvc.models.Jsonable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Classe che modella un Token relativo al servizio Firebase
 */
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

    /**
     * Metodo che produce il JSONObject che rappresenta l'oggetto
     * @return JSONOBject che rappresenta l'oggetto
     */
    @Override
    public JSONObject toJson() {
            JSONObject jsonObject = new JSONObject();
            try {
                if (this.idToken != -1) jsonObject.put("idtoken", getIdToken());
                jsonObject.put("tokenvalue", getToken());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return jsonObject;
    }

}
