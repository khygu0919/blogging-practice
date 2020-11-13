package com.example.myprotobuf;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

public class GlobalDBHelper extends SQLiteAssetHelper {
    private static final String DB_NAME = "myprotobuf.db";
    private static final String PROTO_TABLE = "stored_proto";
    private static final String[] PROTO_SELECT = {"proto_string"};
    private static final int DB_VERSION = 1;
    private static GlobalDBHelper mInstance = null;

    public GlobalDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        setForcedUpgrade();
    }

    public static GlobalDBHelper getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new GlobalDBHelper(context.getApplicationContext());
        }
        return mInstance;
    }

    private Cursor getCursor(String table, String[] sqlProjectionIn, String sqlSelect, String[] whereArgs) {
        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(table);
        Cursor mCursor = queryBuilder.query(db, sqlProjectionIn, sqlSelect, whereArgs, null, null, "idx desc");
        return mCursor;
    }

    public Cursor getProto() {
        return getCursor(PROTO_TABLE, PROTO_SELECT, null, null);
    }

    public void insertProto(ContentValues contentValues) {
        SQLiteDatabase db = getWritableDatabase();
        db.insert(PROTO_TABLE, null, contentValues);
        db.close();
    }

    public void cleanProtoTable() {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(PROTO_TABLE, null, null);
        db.close();
    }
}