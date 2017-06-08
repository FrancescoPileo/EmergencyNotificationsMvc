package com.univpm.cpp.emergencynotificationsmvc.models.envValues;

import java.util.ArrayList;

public interface EnviromentalValuesModel {

    public boolean newValues(EnviromentalValues values);

    public ArrayList<EnviromentalValues> getAllValues();

    public ArrayList<EnviromentalValues> getLastValuesForEachBeacon();

}
