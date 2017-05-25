package com.univpm.cpp.emergencynotificationsmvc.models.user;


import android.util.Log;

import com.univpm.cpp.emergencynotificationsmvc.utils.DbUtils;
import com.univpm.cpp.emergencynotificationsmvc.utils.HttpUtils;

import org.apache.http.*;

/**
 * Implementazione del model utente usando un db
 */
public class UserModelImpl implements UserModel {

    @Override
    public User getUser(String username) {
        User user = null;
        String response = null;
        try {
            response = HttpUtils.sendGet("appuser/username/" + username);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //user = DbUtils.getUser(username);
        if (response != null){
            user = new User(response);
        }
        return user;
    }

    @Override
    public User getLastGuestUser() {
        User user = null;
        String response = null;
        try {
            response = HttpUtils.sendGet("appuser/lastguest");
        } catch (Exception e) {
            e.printStackTrace();
        }
        //user = DbUtils.getUser(username);
        if (response != null){
            user = new User(response);
            Log.w("Response", user.getUsername());
        }

        return user;
    }

    @Override
    public boolean newUser(User user) {

        Boolean success = false;
        try {
            success = HttpUtils.sendPost("appuser", user);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return success;
    }

    @Override
    public boolean newGuestUser(int index) {
        User user = new User();
        user.setUsername("Guest-" + String.valueOf(index));
        user.setGuest(true);
        return newUser(user);
    }

    @Override
    public boolean updateUser(String name, String surname, int age, String mobilephone, String username, String email, String password) {
        return false;
    }



    @Override
    public boolean deleteUser(String username) {
        return false;
    }


}
