package com.univpm.cpp.emergencynotificationsmvc.models.session;

import com.univpm.cpp.emergencynotificationsmvc.models.user.User;
import com.univpm.cpp.emergencynotificationsmvc.utils.HttpUtils;
import android.util.Log;

public class SessionModelImpl implements SessionModel {

    @Override
    public boolean newSession(Session session) {

        boolean success = false;
        try {
            success = HttpUtils.sendPost("appsession/", session);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return success;
    }

    @Override
    public boolean updateSession(Session session) {

        boolean success = false;
        try {
            success = HttpUtils.sendPut("appsession/id/" + session.getId(), session);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return success;    }

    @Override
    public Session getLastSession(User user) {
        Session session = null;
        String response = null;

        try {
            response = HttpUtils.sendGet("appsession/username/" + user.getUsername());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (response != null){
            session = new Session(response);
        }
        return session;
    }
}
