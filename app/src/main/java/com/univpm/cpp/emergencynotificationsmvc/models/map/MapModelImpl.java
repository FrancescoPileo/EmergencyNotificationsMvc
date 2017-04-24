package com.univpm.cpp.emergencynotificationsmvc.models.map;

import com.univpm.cpp.emergencynotificationsmvc.utils.DbUtils;

/**
 * Created by marcociotti on 24/04/17.
 */

public class MapModelImpl implements MapModel {

    @Override
    public Map getMapById(int idMap) {

        Map map = null;
        map = DbUtils.getMapById(idMap);
        return map;
    }
}
