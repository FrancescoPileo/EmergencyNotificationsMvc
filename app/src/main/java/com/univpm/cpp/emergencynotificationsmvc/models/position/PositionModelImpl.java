package com.univpm.cpp.emergencynotificationsmvc.models.position;

import com.univpm.cpp.emergencynotificationsmvc.models.session.Session;
import com.univpm.cpp.emergencynotificationsmvc.models.user.User;
import com.univpm.cpp.emergencynotificationsmvc.utils.DbUtils;
import com.univpm.cpp.emergencynotificationsmvc.utils.HttpUtils;

import java.util.ArrayList;

/**
 * Created by marcociotti on 11/05/17.
 */

public class PositionModelImpl implements PositionModel {

    @Override
    public boolean newPosition(Position position) {

        Boolean success = false;
        try {
            success = HttpUtils.sendPost("userposition", position);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return success;
    }

    @Override
    public Position getLastPositionByUser(User user) {
        Position position = null;
        String response = null;

        try {
            response = HttpUtils.sendGet("userposition/username/" + user.getUsername() + "/last");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (response != null){
            position = new Position(response);
        }
        return position;
    }
}
