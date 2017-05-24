package com.univpm.cpp.emergencynotificationsmvc.models.local;

import android.content.Context;

import com.univpm.cpp.emergencynotificationsmvc.models.session.Session;

public interface LocalPreferences {

    public void rememberLogin(String username, String password);

    public void deleteLogin();

    public void storeSession(Session session);

    public void deleteSession();

    public boolean alreadyLoged();

    public String getUsername();

    public String getPassword();

    public String getTimeIn();

}
