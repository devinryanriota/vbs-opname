package com.projects.devin.opname.cls;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by devin on 1/20/2017.
 */

public class DbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "opname.db";

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(UserContract.SQL_CREATE_USER);
        ContentValues values = new ContentValues();
        values.put(UserContract.UserEntry.COLUMN_NAME_USERNAME, "admin");
        values.put(UserContract.UserEntry.COLUMN_NAME_PASSWORD, "admin");
        db.insert(UserContract.UserEntry.TABLE_NAME, null, values);

        db.execSQL(SKUContract.SQL_CREATE_SKU);
        db.execSQL(OpnameContract.SQL_CREATE_OPNAME);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(UserContract.SQL_DELETE_USER);
        db.execSQL(SKUContract.SQL_DELETE_SKU);
        db.execSQL(OpnameContract.SQL_DELETE_OPNAME);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
