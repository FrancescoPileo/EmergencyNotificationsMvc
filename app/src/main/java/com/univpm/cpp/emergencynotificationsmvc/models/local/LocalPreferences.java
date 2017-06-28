package com.univpm.cpp.emergencynotificationsmvc.models.local;

import android.content.Context;

import com.univpm.cpp.emergencynotificationsmvc.models.session.Session;
import com.univpm.cpp.emergencynotificationsmvc.models.user.User;

public interface LocalPreferences {

    public void rememberUser(User user);

    public void deleteLogin();

    public void storeSession(Session session);

    public void deleteSession();

    public Session getSession();

    public boolean alreadyLoged();

    //public String getUsername();

    //public String getPassword();

    public User getUser();


}
