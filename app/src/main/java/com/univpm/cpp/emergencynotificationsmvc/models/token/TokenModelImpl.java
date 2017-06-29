package com.univpm.cpp.emergencynotificationsmvc.models.token;

import com.univpm.cpp.emergencynotificationsmvc.utils.HttpUtils;

public class TokenModelImpl implements TokenModel {

    @Override
    public boolean newToken(Token token) {
        Boolean success = false;
        try {
            success = HttpUtils.sendPost("token", token);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return success;
    }
}
