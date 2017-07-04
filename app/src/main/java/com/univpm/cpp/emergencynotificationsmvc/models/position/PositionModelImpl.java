package com.univpm.cpp.emergencynotificationsmvc.models.position;

import com.univpm.cpp.emergencynotificationsmvc.models.user.User;
import com.univpm.cpp.emergencynotificationsmvc.utils.Broadcaster;
import com.univpm.cpp.emergencynotificationsmvc.utils.HttpUtils;

/**
 * Implementazione del modello delle posizioni utente che fa riferimento al server REST
 */
public class PositionModelImpl implements PositionModel {

    private Broadcaster broadcaster;     //Classe capace di inviare messaggi broadcast
    private HttpUtils httpUtils;        //Classe che gestisce la connessione HTTP

    public PositionModelImpl(Broadcaster broadcaster){
        this.broadcaster = broadcaster;
        this.httpUtils = new HttpUtils(this.broadcaster);
    }

    @Override
    public boolean newPosition(Position position) {

        return httpUtils.sendPost("userposition", position);

    }

    @Override
    public Position getLastPositionByUser(User user) {
        Position position = null;
        String response = httpUtils.sendGet("userposition/username/" + user.getUsername() + "/last");

        if (response != null){
            position = new Position(response);
        }
        return position;
    }
}
