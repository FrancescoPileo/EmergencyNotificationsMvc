package com.univpm.cpp.emergencynotificationsmvc.models.local;

import android.content.Context;
import android.content.SharedPreferences;

import com.univpm.cpp.emergencynotificationsmvc.R;

public class LocalPreferencesImpl implements  LocalPreferences {

    Context context;

    private static final String LOGIN_PERFERENCES = "login";

    public LocalPreferencesImpl(Context context){
        this.context = context;
    }

    @Override
    public void rememberLogin(String username, String password) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(LOGIN_PERFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(getContext().getString(R.string.pref_username), username);
        editor.putString(getContext().getString(R.string.pref_password), password);
        editor.commit();
    }

    @Override
    public boolean alreadyLoged() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(LOGIN_PERFERENCES, Context.MODE_PRIVATE);
        String login = sharedPreferences.getString(getContext().getString(R.string.pref_username), null);
        return (login != null);
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @Override
    public String getUsername() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(LOGIN_PERFERENCES, Context.MODE_PRIVATE);
        return sharedPreferences.getString(getContext().getString(R.string.pref_username), null);
    }

    @Override
    public String getPassword() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(LOGIN_PERFERENCES, Context.MODE_PRIVATE);
        return sharedPreferences.getString(getContext().getString(R.string.pref_password), null);
    }
}
