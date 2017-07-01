package com.univpm.cpp.emergencynotificationsmvc.models.position;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.univpm.cpp.emergencynotificationsmvc.models.local.LocalSQLiteContract.UserpositionTable;
import com.univpm.cpp.emergencynotificationsmvc.models.local.LocalSQLiteDbHelper;
import com.univpm.cpp.emergencynotificationsmvc.models.node.NodeModel;
import com.univpm.cpp.emergencynotificationsmvc.models.node.NodeModelLocalImpl;
import com.univpm.cpp.emergencynotificationsmvc.models.user.User;
import com.univpm.cpp.emergencynotificationsmvc.models.user.UserModel;
import com.univpm.cpp.emergencynotificationsmvc.models.user.UserModelLocalImpl;

public class PositionModelLocalImpl implements PositionModel {

    Context context;
    LocalSQLiteDbHelper mLocalSQLiteDbHelper = null;

    public PositionModelLocalImpl(Context context){
        this.context = context;
        mLocalSQLiteDbHelper = LocalSQLiteDbHelper.getInstance(context);
        Log.w("LocalMapModel", "ok");
    }

    @Override
    public boolean newPosition(Position position) {
        return false;
    }

    @Override
    public Position getLastPositionByUser(User user) {
       Position position = null;
        if (user != null) {
            SQLiteDatabase db = mLocalSQLiteDbHelper.getReadableDatabase();
            Cursor cursor = db.query(UserpositionTable.TABLE_NAME,
                    new String[]{UserpositionTable._ID, UserpositionTable.COLUMN_NAME_IDUSER,
                            UserpositionTable.COLUMN_NAME_IDNODE, UserpositionTable.COLUMN_NAME_DETECTIONTIME}
                    , UserpositionTable.COLUMN_NAME_IDUSER + "=?",
                    new String[]{String.valueOf(user.getId())}, null, null, null, null); //cambiare i null
            if (cursor != null && cursor.getCount() != 0) {
                cursor.moveToFirst();
                position = new Position();
                position.setIdPosition(cursor.getInt(0));

                UserModel userModel = new UserModelLocalImpl(context);
                position.setUser(userModel.getUser(user.getUsername()));

                NodeModel nodeModel = new NodeModelLocalImpl(context);
                position.setNode(nodeModel.getNodeById(cursor.getInt(2)));

                position.setTime(cursor.getString(3));
            }
            //db.close();
        }
        return position;
    }
}
