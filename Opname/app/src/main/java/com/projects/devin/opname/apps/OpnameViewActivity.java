package com.projects.devin.opname.apps;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.projects.devin.opname.R;
import com.projects.devin.opname.adapter.OpnameListAdapter;
import com.projects.devin.opname.cls.DbHelper;
import com.projects.devin.opname.cls.Opname;
import com.projects.devin.opname.cls.OpnameContract;

import java.util.ArrayList;
import java.util.List;

public class OpnameViewActivity extends AppCompatActivity {

    private ListView opnameList;
    private List<Opname> lsOpname;
    private TextView emptyText;
    private String relasi, kodeRak;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opname_view);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("View Opname");

        initComponent();
        doGetExtras();
        getDataFromDB();
    }

    private void initComponent(){
        opnameList = (ListView) findViewById(R.id.opname_list);
        lsOpname = new ArrayList<Opname>();
        emptyText = (TextView) findViewById(R.id.empty_text);
        opnameList.setEmptyView(emptyText);
    }

    private void doGetExtras(){
        relasi = getIntent().getExtras().getString("RELASI");
        kodeRak = getIntent().getExtras().getString("KODE_RAK");
    }

    private void getDataFromDB(){
        DbHelper dbHelper = new DbHelper(OpnameViewActivity.this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(
                OpnameContract.OpnameEntry.TABLE_NAME,
                new String[]{
                        OpnameContract.OpnameEntry.COLUMN_NAME_RELASI,
                        OpnameContract.OpnameEntry.COLUMN_NAME_RAK,
                        OpnameContract.OpnameEntry.COLUMN_NAME_SKU,
                        OpnameContract.OpnameEntry.COLUMN_NAME_ISBN,
                        OpnameContract.OpnameEntry.COLUMN_NAME_JUDUL,
                        OpnameContract.OpnameEntry.COLUMN_NAME_DISTRIBUTOR,
                        OpnameContract.OpnameEntry.COLUMN_NAME_HARGA_JUAL,
                        OpnameContract.OpnameEntry.COLUMN_NAME_QTY,
                        OpnameContract.OpnameEntry.COLUMN_NAME_DATE,
                        OpnameContract.OpnameEntry.COLUMN_NAME_LOGIN_NAME,
                        OpnameContract.OpnameEntry.COLUMN_NAME_STATUS
                },
                String.format("%s=? AND %s=?", OpnameContract.OpnameEntry.COLUMN_NAME_RELASI, OpnameContract.OpnameEntry.COLUMN_NAME_RAK),
                new String[]{
                        relasi,
                        kodeRak
                },
                null,
                null,
                null
        );
        cursor.moveToFirst();

        for(int i = 0; i < cursor.getCount(); i++, cursor.moveToNext()){

            Opname opname = new Opname();
            opname.setRelasi(cursor.getString(0));
            opname.setKodeRak(cursor.getString(1));
            opname.setSKU(cursor.getString(2));
            opname.setISBN(cursor.getString(3));
            opname.setJudul(cursor.getString(4));
            opname.setDistributor(cursor.getString(5));
            opname.setHarga(cursor.getInt(6));
            opname.setQty(cursor.getInt(7));
            opname.setDateTime(cursor.getString(8));
            opname.setLoginName(cursor.getString(9));
            opname.setStatus(cursor.getString(10));

            lsOpname.add(opname);
        }

        opnameList.setAdapter(new OpnameListAdapter(getApplicationContext(), lsOpname));
    }

    public void onBackPressed(){
        finish();
    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case android.R.id.home: onBackPressed();
                return true;
            default : break;
        }
        return super.onOptionsItemSelected(item);
    }
}
