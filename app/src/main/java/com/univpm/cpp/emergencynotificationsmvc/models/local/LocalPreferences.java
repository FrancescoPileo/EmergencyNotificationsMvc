package com.univpm.cpp.emergencynotificationsmvc.models.local;

import android.content.Context;

public interface LocalPreferences {

    public void rememberLogin(String username, String password);
    public boolean alreadyLoged();

}
