package com.univpm.cpp.emergencynotificationsmvc.models.user;

import com.univpm.cpp.emergencynotificationsmvc.models.Jsonable;

import org.json.JSONException;
import org.json.JSONObject;

public class User extends UserGuest implements Jsonable {

    private String surname;
    private String username;
    private int age;
    private String mobilephone;
    private String email;
    private String password;
    private boolean isGuest;

    public User(){
        super();
        this.surname = null;
        this.username = null;
        this.age = -1;
        this.mobilephone = null;
        this.email = null;
        this.password = null;
        this.isGuest = false;
    }

    public User(int id, String name, String surname, String username, int age, String mobilephone, String email, String password){
        super(id, name);
        this.surname = surname;
        this.username = username;
        this.age = age;
        this.mobilephone = mobilephone;
        this.email = email;
        this.password = password;
    }

    public User(String jsonString){
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            this.id = jsonObject.getInt("iduser");
            this.username = jsonObject.getString("username");
            this.name = jsonObject.optString("name", null);
            this.surname = jsonObject.optString("surname", null);
            this.age = jsonObject.optInt("age", -1);
            this.mobilephone = jsonObject.optString("mobilephone", null);
            this.email = jsonObject.optString("email", null);
            this.password = jsonObject.optString("password", null);
            this.isGuest = jsonObject.optBoolean("isguest", false);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public JSONObject toJson() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("name", this.getName());
            jsonObject.put("surname", this.getSurname());
            jsonObject.put("username", this.getUsername());
            jsonObject.put("age", this.getAge());
            jsonObject.put("mobilephone", this.getMobilephone());
            jsonObject.put("email", this.getEmail());
            jsonObject.put("password", this.getPassword());
            jsonObject.put("isguest", this.isGuest());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getMobilephone() {
        return mobilephone;
    }

    public void setMobilephone(String mobilephone) {
        this.mobilephone = mobilephone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isGuest() {
        return isGuest;
    }

    public void setGuest(boolean guest) {
        isGuest = guest;
    }

    @Override
    public String toString() {
        return "User{" +
                "surname='" + surname + '\'' +
                ", username='" + username + '\'' +
                ", age=" + age +
                ", mobilephone='" + mobilephone + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", isGuest=" + isGuest +
                '}';
    }
}
