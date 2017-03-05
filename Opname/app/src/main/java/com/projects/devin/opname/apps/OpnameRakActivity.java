package com.projects.devin.opname.apps;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.projects.devin.opname.R;
import com.projects.devin.opname.adapter.OpnameListAdapter;
import com.projects.devin.opname.cls.DbHelper;
import com.projects.devin.opname.cls.Opname;
import com.projects.devin.opname.cls.OpnameContract;
import com.projects.devin.opname.cls.Rak;
import com.projects.devin.opname.cls.RakContract;
import com.projects.devin.opname.cls.SKUContract;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OpnameRakActivity extends AppCompatActivity {

    private EditText relasiText;
    private Button addRakButton, closingButton;
    private TableLayout tabRak;
    private String relasi, username, kodeRak;
    //private List<String> lsRak;
    private List<Rak> lsRak;
    private List<Opname> lsOpname;

    public static Activity inputActivity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inputActivity = this;
        setContentView(R.layout.activity_opname_rak);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Rak Opname");

        doGetExtras();
        initComponent();
        getSharedPreferences();

        getRakFromDB();
        displayField();
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
        tabRak = (TableLayout) findViewById(R.id.table_rak);
        addRakButton = (Button) findViewById(R.id.add_rak_button);
        closingButton = (Button) findViewById(R.id.closing_button);
        lsRak = new ArrayList<Rak>();
        lsOpname = new ArrayList<Opname>();

        relasiText.setText(relasi);

        addRakButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer id = lsRak.get(lsRak.size() - 1).getId() + 1; //id nya last id + 1
                Rak rak = new Rak();
                rak.setKodeRelasi(relasi);
                rak.setKodeRak("");
                rak.setId(id);
                lsRak.add(rak);

                drawRowInput("", id);
                addToDatabase("", id);
            }
        });

        closingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDataFromDB();
                saveToText();
            }
        });
    }

    private void addToDatabase(String kodeRak, Integer id){
        //db
        DbHelper dbHelper = new DbHelper(OpnameRakActivity.this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(RakContract.RakEntry.COLUMN_NAME_KODE_RELASI, relasi);
        cv.put(RakContract.RakEntry.COLUMN_NAME_ID, id);
        cv.put(RakContract.RakEntry.COLUMN_NAME_KODE_RAK, kodeRak);
        long insertId = db.insert(RakContract.RakEntry.TABLE_NAME, null, cv);

        //Toast.makeText(getApplicationContext(), "add to db", Toast.LENGTH_SHORT).show();
    }

    private void updateDatabase(String kodeRak, Integer id){
        DbHelper dbHelper = new DbHelper(OpnameRakActivity.this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(RakContract.RakEntry.COLUMN_NAME_KODE_RAK, kodeRak);

        int res = db.update(
                RakContract.RakEntry.TABLE_NAME,
                cv,
                String.format("%s=? AND %s=?", RakContract.RakEntry.COLUMN_NAME_KODE_RELASI, RakContract.RakEntry.COLUMN_NAME_ID),
                new String[]{
                        relasi,
                        id.toString()
                }
        );
    }

    private void deleteFromDatabase(Integer id, String k){
        DbHelper dbHelper = new DbHelper(OpnameRakActivity.this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        db.delete(
                RakContract.RakEntry.TABLE_NAME,
                String.format("%s=? AND %s=?", RakContract.RakEntry.COLUMN_NAME_KODE_RELASI, RakContract.RakEntry.COLUMN_NAME_ID),
                new String[]{
                        relasi,
                        id.toString()
                }
        );

        db.delete(OpnameContract.OpnameEntry.TABLE_NAME,
                String.format("%s=? AND %s=?", OpnameContract.OpnameEntry.COLUMN_NAME_RELASI, OpnameContract.OpnameEntry.COLUMN_NAME_RAK),
                new String[]{
                        relasi,
                        k
                }
        );
        Toast.makeText(getApplicationContext(), "Rak deleted", Toast.LENGTH_SHORT).show();
    }

    private void getRakFromDB(){
        lsRak.clear();
        DbHelper dbHelper = new DbHelper(OpnameRakActivity.this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(
                RakContract.RakEntry.TABLE_NAME,
                new String[]{
                        RakContract.RakEntry.COLUMN_NAME_KODE_RAK,
                        RakContract.RakEntry.COLUMN_NAME_ID,
                        RakContract.RakEntry.COLUMN_NAME_KODE_RELASI
                },
                String.format("%s=?", RakContract.RakEntry.COLUMN_NAME_KODE_RELASI),
                new String[]{
                        relasi
                },
                null,
                null,
                null
        );
        cursor.moveToFirst();

        for(int i = 0; i < cursor.getCount(); i++, cursor.moveToNext()){
            Rak rak = new Rak();
            rak.setKodeRak(cursor.getString(0));
            rak.setId(cursor.getInt(1));
            rak.setKodeRelasi(cursor.getString(2));

            lsRak.add(rak);
        }
    }

    private void resetDraw(){
        tabRak.removeAllViews();
    }

    private void drawRowInput(String text, Integer id){

        final Integer rowID = id;

        TableRow tbRow = new TableRow(this);
        tbRow.setLayoutParams(new TableLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));
        tbRow.setId(id);

        final EditText rakText = new EditText(this);
        rakText.setLayoutParams(new TableRow.LayoutParams(
                0,
                ViewGroup.LayoutParams.MATCH_PARENT,
                2.0f
        ));
        rakText.setText(text);
        rakText.setId(id + 10000);
        rakText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String kode = rakText.getText().toString();

                for(int i = 0; i < lsRak.size(); i++){
                    if(lsRak.get(i).getId() == rowID){
                        lsRak.get(i).setKodeRak(kode);
                    }
                }
                updateDatabase(kode, rowID);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        Button viewButton = new Button(this);
        viewButton.setLayoutParams(new TableRow.LayoutParams(
                0,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                1.0f
        ));
        viewButton.setText("View");
        viewButton.setId(id + 100000);
        viewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String kodeR = "";
                for(int i = 0; i < lsRak.size(); i++){
                    if(lsRak.get(i).getId() == rowID){
                        kodeR = lsRak.get(i).getKodeRak();
                    }
                }

                Intent i = new Intent(OpnameRakActivity.this, OpnameViewActivity.class);
                i.putExtra("RELASI", relasi);
                i.putExtra("KODE_RAK", kodeR);
                startActivity(i);
            }
        });

        Button deleteButton = new Button(this);
        deleteButton.setLayoutParams(new TableRow.LayoutParams(
                0,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                1.0f
        ));
        deleteButton.setText("Del");
        deleteButton.setId(id + 1000000);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(OpnameRakActivity.this)
                        .setTitle("Delete")
                        .setMessage("Apakah ingin menghapus rak ini?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String k = "";
                                for(int i = 0; i < lsRak.size(); i++){
                                    if(lsRak.get(i).getId() == rowID){
                                        k = lsRak.get(i).getKodeRak();
                                    }
                                }
                                deleteFromDatabase(rowID, k);
                                resetDraw();
                                getRakFromDB();
                                displayField();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .show();
            }
        });

        Button opnameButton = new Button(this);
        opnameButton.setLayoutParams(new TableRow.LayoutParams(
                0,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                1.0f
        ));
        opnameButton.setText("Opn");
        opnameButton.setId(id + 10000000);
        opnameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String kodeR = "";
                for(int i = 0; i < lsRak.size(); i++){
                    if(lsRak.get(i).getId() == rowID){
                        kodeR = lsRak.get(i).getKodeRak();
                    }
                }

                Intent i = new Intent(OpnameRakActivity.this, OpnameInputActivity.class);
                i.putExtra("RELASI", relasi);
                i.putExtra("KODE_RAK", kodeR);
                startActivity(i);
            }
        });

        tbRow.addView(rakText);
        tbRow.addView(viewButton);
        tbRow.addView(deleteButton);
        tbRow.addView(opnameButton);
        tabRak.addView(tbRow);
    }

    private void displayField(){
        if(lsRak.size() == 0){ //list rak kosong, display 1 row kosong
            drawRowInput("", 0);

            Rak rak = new Rak();
            rak.setKodeRelasi(relasi);
            rak.setId(0);
            rak.setKodeRak("");

            lsRak.add(rak);
            addToDatabase("", 0);
        }
        else{
            for(int i = 0; i < lsRak.size(); i++){
                drawRowInput(lsRak.get(i).getKodeRak(), lsRak.get(i).getId());
            }
        }
    }

    private void getDataFromDB(){
        lsOpname.clear();
        DbHelper dbHelper = new DbHelper(OpnameRakActivity.this);
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
                String.format("%s=?", OpnameContract.OpnameEntry.COLUMN_NAME_RELASI),
                new String[]{
                        relasi
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
    }

    private void saveToText(){
        String output = "";
        for(int i = 0; i < lsOpname.size(); i++){
            output += lsOpname.get(i).getRelasi() + " ~ ";
            output += lsOpname.get(i).getKodeRak() + " ~ ";
            output += lsOpname.get(i).getSKU() + " ~ ";
            output += lsOpname.get(i).getISBN() + " ~ ";
            output += lsOpname.get(i).getJudul() + " ~ ";
            output += lsOpname.get(i).getDistributor() + " ~ ";
            output += lsOpname.get(i).getHarga() + " ~ ";
            output += lsOpname.get(i).getQty() + " ~ ";
            output += lsOpname.get(i).getDateTime() + " ~ ";
            output += lsOpname.get(i).getLoginName() + "\r\n";
        }

        try{
            File dir = Environment.getExternalStorageDirectory();
            File file = new File(dir.getAbsolutePath(), "/StockOpname");

            Date date = new Date();
            int day = date.getDate();
            int month = date.getMonth() + 1;
            int year = date.getYear() - 100;

            String[] splt = lsOpname.get(0).getRelasi().split("-");
            String kodeRelasi = splt[0];
            final String fileName = String.valueOf(String.format("%02d", day)) + String.valueOf(String.format("%02d", month)) + String.valueOf(String.format("%02d", year)) + "_" + kodeRelasi + ".txt";

            File f = new File(file, fileName);
            FileOutputStream fOut = new FileOutputStream(f);
            OutputStreamWriter osw = new OutputStreamWriter(fOut);

            osw.write(output);
            osw.flush();
            osw.close();
            fOut.flush();
            fOut.close();

            //dialog for uploading file
            new AlertDialog.Builder(this)
                    .setTitle("Upload to Server")
                    .setMessage("Upload closing file to server?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //uploadFile(fileName);
                            AsyncTaskUploadFile async = new AsyncTaskUploadFile();
                            async.execute(fileName);
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .show();

            Toast.makeText(getApplicationContext(), "File hasil closing tersedia pada /StockOpname/" + fileName, Toast.LENGTH_LONG).show();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    class AsyncTaskUploadFile extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(OpnameRakActivity.this);
            progressDialog.setMessage("Uploading File");
            progressDialog.setCancelable(true);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String result = uploadFile(params[0]);
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progressDialog.hide();
            if(result.equalsIgnoreCase("success")){
                Toast.makeText(getApplicationContext(), "Upload Success", Toast.LENGTH_LONG).show();
            }
            else{
                Toast.makeText(getApplicationContext(), "Upload Failed", Toast.LENGTH_LONG).show();
            }
        }

    }

    private String uploadFile(String fileName){
        String result = "";
        try{
            String username = "";
            String password = "";
            String host = "";
            FTPClient ftpClient = new FTPClient();
            ftpClient.connect(host, 21);
            boolean status = ftpClient.login(username, password);

            if(FTPReply.isPositiveCompletion(ftpClient.getReplyCode())){
                ftpClient.setFileType(FTP.ASCII_FILE_TYPE);
                ftpClient.enterLocalPassiveMode();

                File dir = Environment.getExternalStorageDirectory();
                File file = new File(dir.getAbsolutePath(), "/StockOpname/" + fileName);
                String srcFilePath = file.getAbsolutePath();

                //upload
                FileInputStream srcFileStream = new FileInputStream(new File(srcFilePath));
                status = ftpClient.storeFile(fileName, srcFileStream);
                srcFileStream.close();
                result = "success";
            }

            ftpClient.logout();
            ftpClient.disconnect();
        }
        catch(Exception e){
            e.printStackTrace();
            result = "failed";
        }
        return result;
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
