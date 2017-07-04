package com.univpm.cpp.emergencynotificationsmvc.models.token;

import com.univpm.cpp.emergencynotificationsmvc.utils.Broadcaster;
import com.univpm.cpp.emergencynotificationsmvc.utils.HttpUtils;

/**
 * Implementazione del modello dei token Firebase che fa riferimento al server REST
 */
public class TokenModelImpl implements TokenModel {

    private Broadcaster broadcaster;     //Classe capace di inviare messaggi broadcast
    private HttpUtils httpUtils;        //Classe che gestisce la connessione HTTP

    public TokenModelImpl(Broadcaster broadcaster){
        this.broadcaster = broadcaster;
        this.httpUtils = new HttpUtils(broadcaster);
    }

    @Override
    public boolean newToken(Token token) {

        return httpUtils.sendPost("token", token);
    }
}
