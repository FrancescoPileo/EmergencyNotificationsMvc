package com.univpm.cpp.emergencynotificationsmvc.models.user;


import com.univpm.cpp.emergencynotificationsmvc.utils.DbUtils;

/**
 * Implementazione del model utente usando un db
 */
public class UserModelImpl implements UserModel {

    @Override
    public User getUser(String username) {
        User user = null;
        user = DbUtils.getUser(username);
        return user;
    }
}
