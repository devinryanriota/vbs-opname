package com.projects.devin.opname.cls;

import android.provider.BaseColumns;

/**
 * Created by devin on 1/20/2017.
 */

public class SKUContract {

    public static final String SQL_CREATE_SKU = String.format(
            "CREATE TABLE %s(%s, %s, %s, %s, %s, %s, %s)",
            SKUContract.SKUEntry.TABLE_NAME,
            String.format("%s INTEGER PRIMARY KEY AUTOINCREMENT", SKUContract.SKUEntry._ID),
            String.format("%s VARCHAR(255)", SKUContract.SKUEntry.COLUMN_NAME_ISBN),
            String.format("%s VARCHAR(255)", SKUContract.SKUEntry.COLUMN_NAME_SKU),
            String.format("%s VARCHAR(255)", SKUContract.SKUEntry.COLUMN_NAME_JUDUL),
            String.format("%s VARCHAR(255)", SKUContract.SKUEntry.COLUMN_NAME_DISTRIBUTOR),
            String.format("%s VARCHAR(255)", SKUEntry.COLUMN_NAME_STATUS),
            String.format("%s INTEGER", SKUContract.SKUEntry.COLUMN_NAME_HARGA_JUAL)
    );

    public static final String SQL_DELETE_SKU = String.format(
            "DROP TABLE IF EXISTS %s",
            SKUContract.SKUEntry.TABLE_NAME
    );

    public static class SKUEntry implements BaseColumns {
        public static final String TABLE_NAME = "sku";
        public static final String COLUMN_NAME_ISBN = "isbn";
        public static final String COLUMN_NAME_SKU = "sku";
        public static final String COLUMN_NAME_JUDUL = "judul";
        public static final String COLUMN_NAME_DISTRIBUTOR = "distributor";
        public static final String COLUMN_NAME_STATUS = "status";
        public static final String COLUMN_NAME_HARGA_JUAL = "harga_jual";
    }
}
