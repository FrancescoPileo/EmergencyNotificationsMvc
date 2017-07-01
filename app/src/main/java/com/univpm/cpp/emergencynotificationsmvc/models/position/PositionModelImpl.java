package com.univpm.cpp.emergencynotificationsmvc.models.position;

import com.univpm.cpp.emergencynotificationsmvc.models.user.User;
import com.univpm.cpp.emergencynotificationsmvc.utils.Broadcaster;
import com.univpm.cpp.emergencynotificationsmvc.utils.HttpUtils;

/**
 * Created by marcociotti on 11/05/17.
 */

public class PositionModelImpl implements PositionModel {

    Broadcaster broadcaster;
    HttpUtils httpUtils;

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
