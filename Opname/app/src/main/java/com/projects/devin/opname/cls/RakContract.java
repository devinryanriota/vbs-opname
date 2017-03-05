package com.projects.devin.opname.cls;

import android.provider.BaseColumns;

/**
 * Created by devin on 3/4/2017.
 */

public class RakContract {
    public static final String SQL_CREATE_RAK = String.format(
            "CREATE TABLE %s(%s, %s, %s, %s)",
            RakEntry.TABLE_NAME,
            String.format("%s INTEGER PRIMARY KEY AUTOINCREMENT", RakEntry._ID),
            String.format("%s VARCHAR(255)", RakEntry.COLUMN_NAME_KODE_RELASI),
            String.format("%s INTEGER", RakEntry.COLUMN_NAME_ID),
            String.format("%s VARCHAR(255)", RakEntry.COLUMN_NAME_KODE_RAK)
    );

    public static final String SQL_DELETE_RAK = String.format(
            "DROP TABLE IF EXISTS %s",
            RakEntry.TABLE_NAME
    );

    public static class RakEntry implements BaseColumns {
        public static final String TABLE_NAME = "rak";
        public static final String COLUMN_NAME_ID = "row_id";
        public static final String COLUMN_NAME_KODE_RELASI = "kode_relasi";
        public static final String COLUMN_NAME_KODE_RAK = "kode_rak";
    }
}
