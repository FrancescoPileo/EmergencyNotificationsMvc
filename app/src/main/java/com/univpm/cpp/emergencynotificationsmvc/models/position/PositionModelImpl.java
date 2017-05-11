package com.univpm.cpp.emergencynotificationsmvc.models.position;

import com.univpm.cpp.emergencynotificationsmvc.utils.DbUtils;

import java.util.ArrayList;

/**
 * Created by marcociotti on 11/05/17.
 */

public class PositionModelImpl implements PositionModel {

    @Override
    public boolean newPosition(Position position) {
        return DbUtils.newPosition(position);
    }

    @Override
    public ArrayList<Position> getPositionByIdUser(int idUser) {
        return DbUtils.getPositionByIdUser(idUser);
    }
}
