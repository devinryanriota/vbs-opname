package com.projects.devin.opname.cls;

import android.provider.BaseColumns;

/**
 * Created by devin on 1/23/2017.
 */

public class OpnameContract {

    public static final String SQL_CREATE_OPNAME = String.format(
            "CREATE TABLE %s(%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s)",
            OpnameEntry.TABLE_NAME,
            String.format("%s INTEGER PRIMARY KEY AUTOINCREMENT", OpnameEntry._ID),
            String.format("%s VARCHAR(255)", OpnameEntry.COLUMN_NAME_RELASI),
            String.format("%s VARCHAR(255)", OpnameEntry.COLUMN_NAME_RAK),
            String.format("%s VARCHAR(255)", OpnameEntry.COLUMN_NAME_SKU),
            String.format("%s VARCHAR(255)", OpnameEntry.COLUMN_NAME_ISBN),
            String.format("%s VARCHAR(255)", OpnameEntry.COLUMN_NAME_JUDUL),
            String.format("%s VARCHAR(255)", OpnameEntry.COLUMN_NAME_DISTRIBUTOR),
            String.format("%s INTEGER", OpnameEntry.COLUMN_NAME_HARGA_JUAL),
            String.format("%s INTEGER", OpnameEntry.COLUMN_NAME_QTY),
            String.format("%s DATETIME DEFAULT CURRENT_TIMESTAMP", OpnameEntry.COLUMN_NAME_DATE),
            String.format("%s VARCHAR(255)", OpnameEntry.COLUMN_NAME_LOGIN_NAME)
    );

    public static final String SQL_DELETE_OPNAME = String.format(
            "DROP TABLE IF EXISTS %s",
            OpnameContract.OpnameEntry.TABLE_NAME
    );

    public static class OpnameEntry implements BaseColumns {
        public static final String TABLE_NAME = "opname";
        public static final String COLUMN_NAME_RELASI = "relasi";
        public static final String COLUMN_NAME_RAK = "kode_rak";
        public static final String COLUMN_NAME_SKU = "kode_sku";
        public static final String COLUMN_NAME_ISBN = "isbn";
        public static final String COLUMN_NAME_JUDUL = "judul_buku";
        public static final String COLUMN_NAME_DISTRIBUTOR = "distributor";
        public static final String COLUMN_NAME_HARGA_JUAL = "harga_jual";
        public static final String COLUMN_NAME_QTY = "quantity";
        public static final String COLUMN_NAME_DATE = "date_time";
        public static final String COLUMN_NAME_LOGIN_NAME = "login_name";
    }
}
