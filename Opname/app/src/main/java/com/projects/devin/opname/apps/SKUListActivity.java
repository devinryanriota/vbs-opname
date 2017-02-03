package com.projects.devin.opname.apps;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ListViewCompat;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import com.projects.devin.opname.R;
import com.projects.devin.opname.adapter.SKUListAdapter;
import com.projects.devin.opname.cls.DbHelper;
import com.projects.devin.opname.cls.SKU;
import com.projects.devin.opname.cls.SKUContract;

import java.util.ArrayList;
import java.util.List;

public class SKUListActivity extends AppCompatActivity {

    private ListView skuList;
    private List<SKU> lsSKU;
    private TextView emptyText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skulist);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("SKU Data List");

        initComponent();
        getSKUData();
    }

    private void initComponent(){
        skuList = (ListView) findViewById(R.id.sku_list);
        lsSKU = new ArrayList<SKU>();
        emptyText = (TextView) findViewById(R.id.empty_text);
        skuList.setEmptyView(emptyText);
    }

    private void getSKUData(){
        DbHelper dbHelper = new DbHelper(SKUListActivity.this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(
                SKUContract.SKUEntry.TABLE_NAME,
                new String[]{
                        SKUContract.SKUEntry.COLUMN_NAME_ISBN,
                        SKUContract.SKUEntry.COLUMN_NAME_SKU,
                        SKUContract.SKUEntry.COLUMN_NAME_JUDUL,
                        SKUContract.SKUEntry.COLUMN_NAME_DISTRIBUTOR,
                        SKUContract.SKUEntry.COLUMN_NAME_HARGA_JUAL
                },
                null,
                null,
                null,
                null,
                null
        );
        cursor.moveToFirst();

        for(int i = 0; i < cursor.getCount(); i++, cursor.moveToNext()){
            SKU sku = new SKU();
            sku.setISBN(cursor.getString(0));
            sku.setSKU(cursor.getString(1));
            sku.setJudul(cursor.getString(2));
            sku.setDistributor(cursor.getString(3));
            sku.setHargaJual(Integer.valueOf(cursor.getString(4)));

            lsSKU.add(sku);
        }

        skuList.setAdapter(new SKUListAdapter(getApplicationContext(), lsSKU));
    }

    private void clearList(){
        DbHelper dbHelper = new DbHelper(SKUListActivity.this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        db.execSQL(SKUContract.SQL_DELETE_SKU);
        db.execSQL(SKUContract.SQL_CREATE_SKU);

        lsSKU.clear();
        skuList.setAdapter(new SKUListAdapter(getApplicationContext(), lsSKU));
    }

    public void onBackPressed(){
        finish();
    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case android.R.id.home: onBackPressed();
                return true;
            case R.id.clear_sku_menu:
                clearList();
                return true;
            default : return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_sku_list, menu);

        return super.onCreateOptionsMenu(menu);
    }
}
