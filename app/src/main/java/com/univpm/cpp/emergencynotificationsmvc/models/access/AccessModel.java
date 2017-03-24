package com.univpm.cpp.emergencynotificationsmvc.models.access;

import java.util.ArrayList;

public interface AccessModel {

    public boolean newAccess(Access access);

    public ArrayList<Access> getAccessByUsername(String username);

    public Access getAccessById(int id);

    public boolean updateAccess(Access access);

    public boolean deleteAccess(int id);
}
