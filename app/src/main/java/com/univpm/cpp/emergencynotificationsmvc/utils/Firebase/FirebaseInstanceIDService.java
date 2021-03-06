package com.univpm.cpp.emergencynotificationsmvc.utils.Firebase;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.univpm.cpp.emergencynotificationsmvc.models.token.Token;
import com.univpm.cpp.emergencynotificationsmvc.models.token.TokenModel;
import com.univpm.cpp.emergencynotificationsmvc.models.token.TokenModelImpl;
import com.univpm.cpp.emergencynotificationsmvc.utils.Broadcaster;

public class FirebaseInstanceIDService extends FirebaseInstanceIdService implements Broadcaster {

    @Override
    public void onTokenRefresh() {

        String token = FirebaseInstanceId.getInstance().getToken();
        Log.w("Token", token);
        registerToken(token);

    }

    private void registerToken(String tokenString) {

        TokenModel tokenModel = new TokenModelImpl(this);
        Token token = new Token(-1, tokenString);
        tokenModel.newToken(token);

    }
}