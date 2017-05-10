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

    @Override
    public User getLastGuestUser() {
        User user = null;
        user = DbUtils.getLastGuestUser();
        return user;
    }

    @Override
    public boolean newUser(User user) {

        return DbUtils.newUser(user);
    }

    @Override
    public boolean newGuestUser(int index) {

        return DbUtils.newGuestUser(index);
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
