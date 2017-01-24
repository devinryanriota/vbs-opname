package com.projects.devin.opname.apps;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.projects.devin.opname.R;
import com.projects.devin.opname.cls.DbHelper;
import com.projects.devin.opname.cls.OpnameContract;
import com.projects.devin.opname.cls.SKUContract;

import org.w3c.dom.Text;

public class OpnameInputActivity extends AppCompatActivity {

    private EditText relasiText, kodeRakText, isbnText, judulText, hargaText;
    private TextView qtyTextView;
    private Button searchButton, plusButton, minusButton, finishButton;
    private LinearLayout linError, linResult;
    private String relasi, judul, sku, isbn, distributor, username;
    private Integer harga;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opname_input);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Input Opname");

        doGetExtras();
        initComponent();
        getSharedPreferences();
    }

    private void doGetExtras(){
        relasi = getIntent().getExtras().getString("RELASI");
    }

    private void getSharedPreferences(){
        SharedPreferences pref = getApplicationContext().getSharedPreferences("SESSION_PREFERENCES", MODE_PRIVATE);
        username = pref.getString("username", "");
    }

    private void initComponent(){
        relasiText = (EditText) findViewById(R.id.relasi_text);
        kodeRakText = (EditText) findViewById(R.id.kode_rak_text);
        isbnText = (EditText) findViewById(R.id.isbn_text);
        judulText = (EditText) findViewById(R.id.judul_text);
        hargaText = (EditText) findViewById(R.id.harga_text);
        qtyTextView = (TextView) findViewById(R.id.qty_textview);
        searchButton = (Button) findViewById(R.id.search_button);
        plusButton = (Button) findViewById(R.id.plus_button);
        minusButton = (Button) findViewById(R.id.minus_button);
        finishButton = (Button) findViewById(R.id.finish_button);
        linError = (LinearLayout) findViewById(R.id.linear_error);
        linResult = (LinearLayout) findViewById(R.id.linear_result);

        relasiText.setText(relasi);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchSKU();
            }
        });

        isbnText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(event.getAction() == KeyEvent.ACTION_DOWN){
                    switch(keyCode){
                        case KeyEvent.KEYCODE_ENTER:
                            searchSKU();
                            break;
                        default:
                    }
                }
                return false;
            }
        });

        plusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer qty = Integer.valueOf(qtyTextView.getText().toString());
                qty++;
                qtyTextView.setText(qty.toString());
            }
        });

        minusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer qty = Integer.valueOf(qtyTextView.getText().toString());
                if(qty > 0){
                    qty--;
                }
                qtyTextView.setText(qty.toString());
            }
        });

        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(OpnameInputActivity.this)
                        .setTitle("Opname")
                        .setMessage("Apakah masih ada barang yang mau di opname?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //clear semua textview, edittext, masukkin ke db barang ini
                                dialogYes();

                            }
                        })
                        .setNegativeButton("No, Closing", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //keluar dari menu input
                                dialogNo();
                            }
                        })
                        .show();
            }
        });

        linError.setVisibility(View.GONE);
        linResult.setVisibility(View.GONE);
    }

    private void searchSKU(){
        DbHelper dbHelper = new DbHelper(OpnameInputActivity.this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String isbn = isbnText.getText().toString();

        if(isbn.equalsIgnoreCase("")){
            Toast.makeText(getApplicationContext(), "Scan atau isi ISBN terlebih dahulu!", Toast.LENGTH_SHORT).show();
        }
        else{
            Cursor cursor = db.query(
                    SKUContract.SKUEntry.TABLE_NAME,
                    new String[]{
                            SKUContract.SKUEntry.COLUMN_NAME_JUDUL,
                            SKUContract.SKUEntry.COLUMN_NAME_HARGA_JUAL,
                            SKUContract.SKUEntry.COLUMN_NAME_SKU,
                            SKUContract.SKUEntry.COLUMN_NAME_ISBN,
                            SKUContract.SKUEntry.COLUMN_NAME_DISTRIBUTOR
                    },
                    String.format("%s = ?",
                            SKUContract.SKUEntry.COLUMN_NAME_ISBN
                    ),
                    new String[]{
                        isbn
                    },
                    null,
                    null,
                    null
            );
            cursor.moveToFirst();

            if(cursor.getCount() > 0){
                judul = cursor.getString(0);
                harga = cursor.getInt(1);
                sku = cursor.getString(2);
                isbn = cursor.getString(3);
                distributor = cursor.getString(4);

                linError.setVisibility(View.GONE);
                linResult.setVisibility(View.VISIBLE);
                judulText.setText(judul);
                hargaText.setText(harga.toString());
                qtyTextView.setText("0");
            }
            else{
                linError.setVisibility(View.VISIBLE);
                linResult.setVisibility(View.GONE);
            }
        }
    }

    private long insertOpnameDB(){
        DbHelper dbHelper = new DbHelper(OpnameInputActivity.this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(OpnameContract.OpnameEntry.COLUMN_NAME_RELASI, relasi);
        values.put(OpnameContract.OpnameEntry.COLUMN_NAME_RAK, kodeRakText.getText().toString());
        values.put(OpnameContract.OpnameEntry.COLUMN_NAME_SKU, sku);
        values.put(OpnameContract.OpnameEntry.COLUMN_NAME_ISBN, isbnText.getText().toString());
        values.put(OpnameContract.OpnameEntry.COLUMN_NAME_JUDUL, judul);
        values.put(OpnameContract.OpnameEntry.COLUMN_NAME_DISTRIBUTOR, distributor);
        values.put(OpnameContract.OpnameEntry.COLUMN_NAME_HARGA_JUAL, harga);
        values.put(OpnameContract.OpnameEntry.COLUMN_NAME_QTY, Integer.valueOf(qtyTextView.getText().toString()));
        values.put(OpnameContract.OpnameEntry.COLUMN_NAME_LOGIN_NAME, username);

        long insertID = db.insert(OpnameContract.OpnameEntry.TABLE_NAME, null, values);
        return insertID;
    }

    private void dialogYes(){

        //validasi kode rak text, isbn

        if(insertOpnameDB() != 0){
            Toast.makeText(getApplicationContext(), "Insert Success", Toast.LENGTH_SHORT).show();
            reset();
        }
    }

    private void dialogNo(){
        if(insertOpnameDB() != 0){
            Toast.makeText(getApplicationContext(), "Insert Success", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(OpnameInputActivity.this, OpnameReportActivity.class);
            startActivity(i);
            finish();
            OpnameRelasiActivity.relasiActivity.finish();
        }
    }

    private void reset(){
        kodeRakText.setText("");
        isbnText.setText("");
        judulText.setText("");
        hargaText.setText("");
        qtyTextView.setText("0");

        linResult.setVisibility(View.GONE);
        linError.setVisibility(View.GONE);
    }
}
