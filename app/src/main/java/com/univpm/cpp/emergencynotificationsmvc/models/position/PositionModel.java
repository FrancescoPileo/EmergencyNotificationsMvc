package com.univpm.cpp.emergencynotificationsmvc.models.position;

import java.util.ArrayList;

/**
 * Created by marcociotti on 11/05/17.
 */

public interface PositionModel {

    public boolean newPosition(Position position);

    public ArrayList<Position> getPositionByIdUser(int idUser);
}
