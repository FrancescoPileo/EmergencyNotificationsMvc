package com.univpm.cpp.emergencynotificationsmvc.models.position;

import com.univpm.cpp.emergencynotificationsmvc.models.user.User;

import java.util.ArrayList;

/**
 * Created by marcociotti on 11/05/17.
 */

public interface PositionModel {

    public boolean newPosition(Position position);

    public Position getLastPositionByUser(User user);
}
