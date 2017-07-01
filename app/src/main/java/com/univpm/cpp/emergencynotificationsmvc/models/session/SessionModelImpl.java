package com.univpm.cpp.emergencynotificationsmvc.models.session;

import com.univpm.cpp.emergencynotificationsmvc.models.user.User;
import com.univpm.cpp.emergencynotificationsmvc.utils.Broadcaster;
import com.univpm.cpp.emergencynotificationsmvc.utils.HttpUtils;

public class SessionModelImpl implements SessionModel {

    Broadcaster broadcaster;
    HttpUtils httpUtils;
    public SessionModelImpl(Broadcaster broadcaster){
        this.broadcaster = broadcaster;
        this.httpUtils = new HttpUtils(broadcaster);
    }

    @Override
    public boolean newSession(Session session) {

        return httpUtils.sendPost("appsession/", session);
    }

    @Override
    public boolean updateSession(Session session) {

        return httpUtils.sendPut("appsession/id/" + session.getId(), session);
    }

    @Override
    public Session getLastSession(User user) {
        Session session = null;

        String response = httpUtils.sendGet("appsession/username/" + user.getUsername() + "/last");

        if (response != null){
            session = new Session(response);
        }
        return session;
    }
}
