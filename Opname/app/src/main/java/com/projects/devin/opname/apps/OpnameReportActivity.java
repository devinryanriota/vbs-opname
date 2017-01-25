package com.projects.devin.opname.apps;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.projects.devin.opname.R;
import com.projects.devin.opname.adapter.OpnameListAdapter;
import com.projects.devin.opname.cls.DbHelper;
import com.projects.devin.opname.cls.Opname;
import com.projects.devin.opname.cls.OpnameContract;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OpnameReportActivity extends AppCompatActivity {

    private Button closingButton;
    private ListView opnameList;
    private List<Opname> lsOpname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opname_report);

        initComponent();
        getDataFromDB();
    }

    private void initComponent(){
        lsOpname = new ArrayList<Opname>();
        closingButton = (Button) findViewById(R.id.closing_button);
        opnameList = (ListView) findViewById(R.id.opname_list);

        closingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveToText();
            }
        });
    }

    private void getDataFromDB(){
        DbHelper dbHelper = new DbHelper(OpnameReportActivity.this);
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
                        OpnameContract.OpnameEntry.COLUMN_NAME_LOGIN_NAME
                },
                null,
                null,
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

            lsOpname.add(opname);
        }

        opnameList.setAdapter(new OpnameListAdapter(getApplicationContext(), lsOpname));
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
            String fileName = String.valueOf(String.format("%02d", day)) + String.valueOf(String.format("%02d", month)) + String.valueOf(String.format("%02d", year)) + "_" + kodeRelasi + ".txt";

            File f = new File(file, fileName);
            FileOutputStream fOut = new FileOutputStream(f);
            OutputStreamWriter osw = new OutputStreamWriter(fOut);

            osw.write(output);
            osw.flush();
            osw.close();
            fOut.flush();
            fOut.close();

            Toast.makeText(getApplicationContext(), output, Toast.LENGTH_LONG).show();
            Toast.makeText(getApplicationContext(), "File hasil closing tersedia pada /StockOpname/" + fileName, Toast.LENGTH_LONG).show();
            //finish();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}
