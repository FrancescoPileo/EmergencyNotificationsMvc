package com.univpm.cpp.emergencynotificationsmvc.models.envValues;

import com.univpm.cpp.emergencynotificationsmvc.utils.DbUtils;
import com.univpm.cpp.emergencynotificationsmvc.utils.HttpUtils;

public class EnviromentalValuesModelImpl implements EnviromentalValuesModel {

    @Override
    public boolean newValues(EnviromentalValues values) {

        Boolean success = false;
        try {
            success = HttpUtils.sendPost("enviromentalvalues", values);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return success;
    }
}
