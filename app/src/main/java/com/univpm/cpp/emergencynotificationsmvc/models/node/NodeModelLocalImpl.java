package com.univpm.cpp.emergencynotificationsmvc.models.node;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.univpm.cpp.emergencynotificationsmvc.models.local.LocalSQLiteDbHelper;
import com.univpm.cpp.emergencynotificationsmvc.models.local.LocalSQLiteContract.*;
import com.univpm.cpp.emergencynotificationsmvc.models.map.MapModel;
import com.univpm.cpp.emergencynotificationsmvc.models.map.MapModelLocalImpl;

import java.nio.channels.FileChannel;
import java.util.ArrayList;

public class NodeModelLocalImpl implements NodeModel {

    Context context;
    LocalSQLiteDbHelper mLocalSQLiteDbHelper = null;

    public NodeModelLocalImpl(Context context){
        this.context = context;
        mLocalSQLiteDbHelper = LocalSQLiteDbHelper.getInstance(context);
        Log.w("LocalNodeModel", "ok");
    }

    @Override
    public Node getNodeById(int idNode) {
        Node node = null;
        SQLiteDatabase db = mLocalSQLiteDbHelper.getReadableDatabase();

        Cursor cursor = db.query( NodeTable.TABLE_NAME,
                new String[] { NodeTable._ID, NodeTable.COLUMN_NAME_IDMAP, NodeTable.COLUMN_NAME_NODENAME,
                        NodeTable.COLUMN_NAME_X, NodeTable.COLUMN_NAME_Y}
                , NodeTable._ID + "=?",
                new String[] { String.valueOf(idNode) }, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            node = new Node();
            node.setIdNode(cursor.getInt(0));
            MapModel mapModel = new MapModelLocalImpl(context);
            node.setMap(mapModel.getMapById(cursor.getInt(1)));
            node.setNodename(cursor.getString(2));
            node.setX(cursor.getInt(3));
            node.setY(cursor.getInt(4));
        }
        return node;
    }

    @Override
    public ArrayList<Node> getAllNodes() {
        ArrayList<Node> nodes = null;
        String selectQuery = "SELECT * FROM " + NodeTable.TABLE_NAME;
        SQLiteDatabase db = mLocalSQLiteDbHelper.getReadableDatabase();

        MapModel mapModel = new MapModelLocalImpl(context);

        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                nodes = new ArrayList<>();
                do {
                    Node node = new Node();
                    node.setIdNode(cursor.getInt(0));
                    node.setMap(mapModel.getMapById(cursor.getInt(1)));
                    node.setNodename(cursor.getString(2));
                    node.setX(cursor.getInt(3));
                    node.setY(cursor.getInt(4));
                    nodes.add(node);
                } while (cursor.moveToNext());
            }
        }
        //db.close();
        return nodes;
    }

    @Override
    public ArrayList<Node> getNodeByMap(int idMap) {
        return null;
    }
}
