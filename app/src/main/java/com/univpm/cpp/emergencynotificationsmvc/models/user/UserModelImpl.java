package com.univpm.cpp.emergencynotificationsmvc.models.user;


import com.univpm.cpp.emergencynotificationsmvc.utils.Broadcaster;
import com.univpm.cpp.emergencynotificationsmvc.utils.HttpUtils;

/**
 * Implementazione del model utente usando un db
 */
public class UserModelImpl implements UserModel {

    Broadcaster broadcaster;
    HttpUtils httpUtils;
    public UserModelImpl(Broadcaster broadcaster){
        this.broadcaster = broadcaster;
        this.httpUtils = new HttpUtils(this.broadcaster);
    }

    @Override
    public User getUser(String username) {
        User user = null;
        String response = httpUtils.sendGet("appuser/username/" + username);

        if (response != null){
            user = new User(response);
        }
        return user;
    }

    @Override
    public User getLastGuestUser() {
        User user = null;

        String response = httpUtils.sendGet("appuser/lastguest");

        if (response != null){
            user = new User(response);
        }

        return user;
    }

    @Override
    public boolean newUser(User user) {

        return httpUtils.sendPost("appuser", user);
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
