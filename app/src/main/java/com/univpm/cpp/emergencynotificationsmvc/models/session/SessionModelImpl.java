package com.univpm.cpp.emergencynotificationsmvc.models.session;

import com.univpm.cpp.emergencynotificationsmvc.models.user.User;
import com.univpm.cpp.emergencynotificationsmvc.utils.Broadcaster;
import com.univpm.cpp.emergencynotificationsmvc.utils.HttpUtils;

/**
 * Implementazione del modello delle sessioni utente che fa riferimento al server REST
 */
public class SessionModelImpl implements SessionModel {

    private Broadcaster broadcaster;     //Classe capace di inviare messaggi broadcast
    private HttpUtils httpUtils;        //Classe che gestisce la connessione HTTP

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
