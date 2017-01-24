package com.projects.devin.opname.cls;

import android.provider.BaseColumns;

/**
 * Created by devin on 1/20/2017.
 */

public class UserContract {

    public static final String SQL_CREATE_USER = String.format(
            "CREATE TABLE %s(%s, %s, %s)",
            UserEntry.TABLE_NAME,
            String.format("%s INTEGER PRIMARY KEY AUTOINCREMENT", UserEntry._ID),
            String.format("%s VARCHAR(255)", UserEntry.COLUMN_NAME_USERNAME),
            String.format("%s VARCHAR(255)", UserEntry.COLUMN_NAME_PASSWORD)
    );

    public static final String SQL_DELETE_USER = String.format(
            "DROP TABLE IF EXISTS %s",
            UserEntry.TABLE_NAME
    );

    public static class UserEntry implements BaseColumns {
        public static final String TABLE_NAME = "user";
        public static final String COLUMN_NAME_USERNAME = "username";
        public static final String COLUMN_NAME_PASSWORD= "password";
    }
}
