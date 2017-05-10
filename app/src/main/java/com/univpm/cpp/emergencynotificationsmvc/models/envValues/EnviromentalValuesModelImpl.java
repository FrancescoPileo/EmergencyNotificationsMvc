package com.univpm.cpp.emergencynotificationsmvc.models.envValues;

import com.univpm.cpp.emergencynotificationsmvc.utils.DbUtils;

public class EnviromentalValuesModelImpl implements EnviromentalValuesModel {

    @Override
    public boolean newValues(EnviromentalValues values) {
        return DbUtils.newValues(values);
    }
}
