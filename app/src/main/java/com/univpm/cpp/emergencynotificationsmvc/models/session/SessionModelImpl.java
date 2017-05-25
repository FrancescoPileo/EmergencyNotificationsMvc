package com.univpm.cpp.emergencynotificationsmvc.models.session;

import com.univpm.cpp.emergencynotificationsmvc.utils.DbUtils;
import com.univpm.cpp.emergencynotificationsmvc.utils.HttpUtils;

public class SessionModelImpl implements SessionModel {

    @Override
    public boolean newSession(Session session) {

        boolean success = false;
        try {
            success = HttpUtils.sendPost("appsession", session);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return success;
    }

    @Override
    public boolean updateSession(Session session) {

        boolean success = false;
        try {
            success = HttpUtils.sendPut("appsession/" + session.getId(), session);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return success;    }
}
