package com.univpm.cpp.emergencynotificationsmvc.models.user;

import java.sql.ResultSet;

public class User extends UserGuest{

    private String surname;
    private String username;
    private int age;
    private String mobilephone;
    private String email;
    private String password;

    public User(){
        super();
        this.surname = null;
        this.username = null;
        this.age = -1;
        this.mobilephone = null;
        this.email = null;
        this.password = null;
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

    public User(ResultSet userRS) throws Exception{
        this.id = userRS.getInt("idUser");
        this.name = userRS.getString("name");
        this.surname = userRS.getString("surname");
        this.username = userRS.getString("username");
        this.age = userRS.getInt("age");
        this.mobilephone = userRS.getString("mobilephone");
        this.email = userRS.getString("email");
        this.password = userRS.getString("password");
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
}
