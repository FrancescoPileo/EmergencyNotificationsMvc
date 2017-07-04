package com.univpm.cpp.emergencynotificationsmvc.models.position;

import com.univpm.cpp.emergencynotificationsmvc.models.Jsonable;
import com.univpm.cpp.emergencynotificationsmvc.models.node.Node;
import com.univpm.cpp.emergencynotificationsmvc.models.user.User;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Classe che modella la posizione di un utente
 */
public class Position implements Jsonable{

    private int idPosition;
    private Node node;
    private User user;
    private String time;

    public Position() {

        super();
        this.node = null;
        this.idPosition = -1;
        this.user = null;
        this.time = null;
    }

    public Position(int idPosition, Node node, User user, String time) {
        this.idPosition = idPosition;
        this.node = node;
        this.user = user;
        this.time = time;
    }


    /**
     * Costruttore della classe Position
     * @param jsonString Stringa che contiente la rappresentazione JSON dell'oggetto da istanziare
     */
    public Position(String jsonString){
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            this.idPosition = jsonObject.getInt("idposition");
            this.node = new Node(jsonObject.getJSONObject("idnode").toString());
            this.user = new User(jsonObject.getJSONObject("iduser").toString());
            this.time = jsonObject.getString("detectiontime");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public int getIdPosition() {
        return idPosition;
    }

    public void setIdPosition(int idPosition) {
        this.idPosition = idPosition;
    }

    public Node getNode() {
        return node;
    }

    public void setNode(Node node) {
        this.node = node;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    /**
     * Metodo che produce il JSONObject che rappresenta l'oggetto
     * @return JSONOBject che rappresenta l'oggetto
     */
    @Override
    public JSONObject toJson() {
        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put("idposition", this.idPosition);
            jsonObject.put("iduser", this.user.toJson());
            jsonObject.put("idnode", this.node.toJson());
            jsonObject.put("detectiontime", this.time);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
}
