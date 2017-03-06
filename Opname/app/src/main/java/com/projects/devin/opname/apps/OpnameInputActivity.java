package com.projects.devin.opname.apps;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.projects.devin.opname.R;
import com.projects.devin.opname.cls.DbHelper;
import com.projects.devin.opname.cls.OpnameContract;
import com.projects.devin.opname.cls.RakContract;
import com.projects.devin.opname.cls.SKUContract;

import java.sql.SQLInput;

public class OpnameInputActivity extends AppCompatActivity {

    private EditText kodeRakText, isbnText, relasiText, judulText, hargaText, distributorText, statusText, qtyText;
    private LinearLayout linError, linResult;
    private Button searchButton, plusButton, minusButton;
    private String relasi, judul, sku, isbn, distributor, username, kodeRak, status;
    private Integer harga;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opname_input);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Input Opname");

        doGetExtras();
        getSharedPreferences();
        initComponent();
    }

    private void initComponent(){
        kodeRakText = (EditText) findViewById(R.id.rak_text);
        isbnText = (EditText) findViewById(R.id.isbn_text);
        relasiText = (EditText) findViewById(R.id.relasi_text);
        judulText = (EditText) findViewById(R.id.judul_text);
        hargaText = (EditText) findViewById(R.id.harga_text);
        distributorText = (EditText) findViewById(R.id.distributor_text);
        statusText = (EditText) findViewById(R.id.status_text);
        qtyText = (EditText) findViewById(R.id.qty_text);
        linError = (LinearLayout) findViewById(R.id.linear_error);
        linResult = (LinearLayout) findViewById(R.id.linear_result);
        searchButton = (Button) findViewById(R.id.search_button);
        plusButton = (Button) findViewById(R.id.plus_button);
        minusButton = (Button) findViewById(R.id.minus_button);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchSKU();
                qtyText.setFocusableInTouchMode(true);
                qtyText.requestFocus();
                isbnText.setFocusableInTouchMode(true);
                isbnText.requestFocus();
            }
        });

        isbnText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(event.getAction() == KeyEvent.ACTION_DOWN){
                    switch(keyCode){
                        case KeyEvent.KEYCODE_ENTER:
                            searchSKU();
                            qtyText.clearFocus();
                            isbnText.clearFocus();
                            isbnText.requestFocus();
                            qtyText.setFocusableInTouchMode(false);
                            qtyText.setFocusable(false);
                            returnFocus();
                            return true;
                        default:
                    }
                }
                return OpnameInputActivity.super.onKeyDown(keyCode, event);
            }
        });

        plusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer qty = Integer.valueOf(qtyText.getText().toString());
                qty++;
                qtyText.setText(qty.toString());
                updateDatabase();
            }
        });

        minusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer qty = Integer.valueOf(qtyText.getText().toString());
                if(qty > 0){
                    qty--;
                }
                qtyText.setText(qty.toString());
                updateDatabase();
            }
        });

        qtyText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updateDatabase();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        linError.setVisibility(View.GONE);
        linResult.setVisibility(View.GONE);

        kodeRakText.setText(kodeRak);
    }

    private void returnFocus(){
        CountDownTimer counter = new CountDownTimer(1000, 1000){
            public void onTick(long millisUntilDone){
            }

            public void onFinish() {
                qtyText.setFocusable(true);
                qtyText.setFocusableInTouchMode(true);
            }
        }.start();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode==KeyEvent.KEYCODE_ENTER)
        {
            // Just ignore the [Enter] key
            return true;
        }
        // Handle all other keys in the default way
        return super.onKeyDown(keyCode, event);
    }

    private void doGetExtras(){
        relasi = getIntent().getExtras().getString("RELASI");
        kodeRak = getIntent().getExtras().getString("KODE_RAK");
    }

    private void getSharedPreferences(){
        SharedPreferences pref = getApplicationContext().getSharedPreferences("SESSION_PREFERENCES", MODE_PRIVATE);
        username = pref.getString("username", "");
    }

    private void addToDatabase(){
        DbHelper dbHelper = new DbHelper(OpnameInputActivity.this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(OpnameContract.OpnameEntry.COLUMN_NAME_RELASI, relasi);
        values.put(OpnameContract.OpnameEntry.COLUMN_NAME_RAK, kodeRak);
        values.put(OpnameContract.OpnameEntry.COLUMN_NAME_SKU, sku);
        values.put(OpnameContract.OpnameEntry.COLUMN_NAME_ISBN, isbnText.getText().toString());
        values.put(OpnameContract.OpnameEntry.COLUMN_NAME_JUDUL, judul);
        values.put(OpnameContract.OpnameEntry.COLUMN_NAME_DISTRIBUTOR, distributor);
        values.put(OpnameContract.OpnameEntry.COLUMN_NAME_HARGA_JUAL, harga);
        values.put(OpnameContract.OpnameEntry.COLUMN_NAME_QTY, Integer.valueOf(qtyText.getText().toString()));
        values.put(OpnameContract.OpnameEntry.COLUMN_NAME_LOGIN_NAME, username);

        long insertID = db.insert(OpnameContract.OpnameEntry.TABLE_NAME, null, values);
        //return insertID;
    }

    private void updateDatabase(){
        DbHelper dbHelper = new DbHelper(OpnameInputActivity.this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(OpnameContract.OpnameEntry.COLUMN_NAME_QTY, Integer.valueOf(qtyText.getText().toString()));

        int res = db.update(
                OpnameContract.OpnameEntry.TABLE_NAME,
                cv,
                String.format("%s=? AND %s=? AND %s=?", OpnameContract.OpnameEntry.COLUMN_NAME_RELASI,
                        OpnameContract.OpnameEntry.COLUMN_NAME_RAK,
                        OpnameContract.OpnameEntry.COLUMN_NAME_ISBN),
                new String[]{
                        relasi,
                        kodeRak,
                        isbnText.getText().toString()
                }
        );
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
                            SKUContract.SKUEntry.COLUMN_NAME_DISTRIBUTOR,
                            SKUContract.SKUEntry.COLUMN_NAME_STATUS
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
                status = cursor.getString(5);

                linError.setVisibility(View.GONE);
                linResult.setVisibility(View.VISIBLE);
                judulText.setText(judul);
                hargaText.setText(harga.toString());
                distributorText.setText(distributor);
                statusText.setText(status);

                if(status.equalsIgnoreCase("Cut Off")){
                    new AlertDialog.Builder(OpnameInputActivity.this)
                            .setTitle("Cut-Off")
                            .setMessage("Buku berstatus cut-off!")
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            })
                            .show();
                }

                Integer q = checkISBN();
                if(q == 0){
                    qtyText.setText("1");
                    addToDatabase();
                }
                else{
                    q++;
                    qtyText.setText(q.toString());
                    updateDatabase();
                }
                //isbnText.setFocusable(true);
                isbnText.clearFocus();
                isbnText.requestFocus();
            }
            else{
                linError.setVisibility(View.VISIBLE);

                ToneGenerator toneGenerator = new ToneGenerator(AudioManager.STREAM_ALARM, 100);
                toneGenerator.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 300);

                linResult.setVisibility(View.GONE);
            }
        }
    }

    private Integer checkISBN(){
        DbHelper dbHelper = new DbHelper(OpnameInputActivity.this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(
                OpnameContract.OpnameEntry.TABLE_NAME,
                new String[]{
                        OpnameContract.OpnameEntry.COLUMN_NAME_ISBN,
                        OpnameContract.OpnameEntry.COLUMN_NAME_QTY
                },
                String.format("%s=? AND %s=? AND %s=?", OpnameContract.OpnameEntry.COLUMN_NAME_RELASI,
                        OpnameContract.OpnameEntry.COLUMN_NAME_RAK,
                        OpnameContract.OpnameEntry.COLUMN_NAME_ISBN),
                new String[]{
                        relasi,
                        kodeRak,
                        isbnText.getText().toString()
                },
                null,
                null,
                null
        );
        cursor.moveToFirst();

        if(cursor.getCount() == 0){ //kalo 0 brarti belum ada di db dengan isbn segitu
            return 0;
        }
        else{
            return cursor.getInt(1);
        }
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
