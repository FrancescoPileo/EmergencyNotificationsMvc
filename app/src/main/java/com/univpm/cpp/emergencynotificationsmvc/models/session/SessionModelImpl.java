package com.univpm.cpp.emergencynotificationsmvc.models.session;

import com.univpm.cpp.emergencynotificationsmvc.utils.DbUtils;

public class SessionModelImpl implements SessionModel {

    @Override
    public boolean newSession(Session session) {
        return DbUtils.newSession(session);
    }

    @Override
    public boolean updateSession(Session session) {
        return DbUtils.updateSession(session);
    }
}
