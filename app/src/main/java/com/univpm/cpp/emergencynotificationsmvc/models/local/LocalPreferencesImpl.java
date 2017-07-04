package com.univpm.cpp.emergencynotificationsmvc.models.local;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.univpm.cpp.emergencynotificationsmvc.R;
import com.univpm.cpp.emergencynotificationsmvc.models.session.Session;
import com.univpm.cpp.emergencynotificationsmvc.models.user.User;

/**
 * Implementazione del modello delle preferenze locali
 */
public class LocalPreferencesImpl implements  LocalPreferences {

    private Context context;

    private static final String LOGIN_PERFERENCES = "login";

    public LocalPreferencesImpl(Context context){
        this.context = context;
    }

    @Override
    public void rememberUser(User user) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(LOGIN_PERFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String jsonString = user.toJson().toString();
        editor.putString(getContext().getString(R.string.pref_user), jsonString);
        editor.commit();
    }

    @Override
    public void deleteLogin() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(LOGIN_PERFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(getContext().getString(R.string.pref_user));
        editor.commit();
    }

    @Override
    public boolean alreadyLoged() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(LOGIN_PERFERENCES, Context.MODE_PRIVATE);
        String userString = sharedPreferences.getString(getContext().getString(R.string.pref_user), null);
        Boolean flag = false;
        if (userString != null){
            User user = new User(userString);
            if (user.getPassword() != null){
                flag = true;
            }
        }
        return flag;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @Override
    public User getUser() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(LOGIN_PERFERENCES, Context.MODE_PRIVATE);
        return new User(sharedPreferences.getString(getContext().getString(R.string.pref_user), null));
    }

    @Override
    public void storeSession(Session session) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(LOGIN_PERFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Log.w("StoreSession", session.toJson().toString());
        editor.putString(getContext().getString(R.string.pref_session), session.toJson().toString());
        editor.commit();
    }

    @Override
    public Session getSession() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(LOGIN_PERFERENCES, Context.MODE_PRIVATE);
        String stringSession = sharedPreferences.getString(getContext().getString(R.string.pref_session), null);
        return new Session(stringSession);
    }

    @Override
    public void deleteSession() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(LOGIN_PERFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(getContext().getString(R.string.pref_session));
        editor.commit();
    }

}
