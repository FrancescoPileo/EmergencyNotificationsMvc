package com.univpm.cpp.emergencynotificationsmvc.models.session;

import com.univpm.cpp.emergencynotificationsmvc.models.user.User;

public interface SessionModel {

    public boolean newSession(Session session);

    public boolean updateSession(Session session);

    public Session getLastSession(User user);

   /* public ArrayList<Session> getAccessByUsername(String username);

    public Session getAccessById(int id);

    ;

    public boolean deleteAccess(int id);*/
}
