package com.univpm.cpp.emergencynotificationsmvc.models.token;

import com.univpm.cpp.emergencynotificationsmvc.utils.Broadcaster;
import com.univpm.cpp.emergencynotificationsmvc.utils.HttpUtils;

public class TokenModelImpl implements TokenModel {

    private Broadcaster broadcaster;
    private HttpUtils httpUtils;

    public TokenModelImpl(Broadcaster broadcaster){
        this.broadcaster = broadcaster;
        this.httpUtils = new HttpUtils(broadcaster);
    }

    @Override
    public boolean newToken(Token token) {

        return httpUtils.sendPost("token", token);
    }
}
