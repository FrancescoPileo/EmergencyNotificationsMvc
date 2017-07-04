package com.univpm.cpp.emergencynotificationsmvc.models.user;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.univpm.cpp.emergencynotificationsmvc.models.local.LocalSQLiteContract.AppuserTable;
import com.univpm.cpp.emergencynotificationsmvc.models.local.LocalSQLiteDbHelper;

/**
 * Implementazione del modello degli utenti che fa riferimento al database interno
 */
public class UserModelLocalImpl implements UserModel {

    LocalSQLiteDbHelper mLocalSQLiteDbHelper;

    public UserModelLocalImpl(Context context){
        mLocalSQLiteDbHelper = LocalSQLiteDbHelper.getInstance(context);
        Log.w("LocalMapModel", "ok");
    }

    @Override
    public User getUser(String username) {
        User user = null;
        SQLiteDatabase db = mLocalSQLiteDbHelper.getReadableDatabase();

        Cursor cursor = db.query( AppuserTable.TABLE_NAME,
                new String[] { AppuserTable._ID, AppuserTable.COLUMN_NAME_NAME, AppuserTable.COLUMN_NAME_SURNAME ,
                AppuserTable.COLUMN_NAME_USERNAME, AppuserTable.COLUMN_NAME_AGE, AppuserTable.COLUMN_NAME_MOBILEPHONE,
                AppuserTable.COLUMN_NAME_EMAIL, AppuserTable.COLUMN_NAME_PASSWORD, AppuserTable.COLUMN_NAME_ISGUEST}
                , AppuserTable.COLUMN_NAME_USERNAME+ "=?",
                new String[] { username }, null, null, null, null);
        if (cursor != null && cursor.getCount() != 0) {
            cursor.moveToFirst();
            user = new User();
            user.setId(cursor.getInt(0));
            user.setName(cursor.getString(1));
            user.setSurname(cursor.getString(2));
            user.setUsername(cursor.getString(3));
            user.setAge(cursor.getInt(4));
            user.setMobilephone(cursor.getString(5));
            user.setEmail(cursor.getString(6));
            user.setPassword(cursor.getString(7));
            user.setGuest(cursor.getInt(8) == 1);
        }
        //db.close();
        return user;
    }

    @Override
    public User getLastGuestUser() {
        return null;
    }

    @Override
    public boolean newUser(User user) {
        return false;
    }

    @Override
    public boolean newGuestUser(int index) {
        return false;
    }

    @Override
    public boolean updateUser(String name, String surname, int age, String mobilephone, String username, String email, String password) {
        return false;
    }

    @Override
    public boolean deleteUser(String username) {
        return false;
    }
}
