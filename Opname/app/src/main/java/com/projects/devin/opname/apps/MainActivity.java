package com.projects.devin.opname.apps;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.projects.devin.opname.R;
import com.projects.devin.opname.cls.DbHelper;
import com.projects.devin.opname.cls.SKU;
import com.projects.devin.opname.cls.SKUContract;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Button importDataButton, opnameButton, opnameReportButton, configurationButton;
    private List<SKU> lsSKU;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initComponent();
    }

    private void initComponent(){
        lsSKU = new ArrayList<SKU>();

        importDataButton = (Button) findViewById(R.id.import_data_button);
        opnameButton = (Button) findViewById(R.id.opname_button);
        opnameReportButton = (Button) findViewById(R.id.opname_report_button);
        configurationButton = (Button) findViewById(R.id.configuration_button);

        importDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                importDataSKU();
            }
        });

        configurationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, ConfigurationMenuActivity.class);
                startActivity(i);
            }
        });
    }

    private void importDataSKU(){
        //file txt di folder /StockOpname/master_sku.txt
        File file = new File(Environment.getExternalStorageDirectory(), "/StockOpname/master_sku.txt");

        if(file.exists()){
            StringBuilder text = new StringBuilder();
            try{
                BufferedReader reader = new BufferedReader(new FileReader(file));
                String line;
                while((line = reader.readLine()) != null){
                    String[] content = line.split("~");
                    SKU sku = new SKU();
                    sku.setISBN(content[0]);
                    sku.setSKU(content[1]);
                    sku.setJudul(content[2]);
                    sku.setDistributor(content[3]);
                    sku.setHargaJual(Integer.valueOf(content[4]));

                    lsSKU.add(sku);

                    text.append(line);
                }
                reader.close();
                insertDBSKU();
            }
            catch(Exception e){
                e.printStackTrace();
            }
            Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(getApplicationContext(), "File doesn't exists", Toast.LENGTH_SHORT).show();
        }
    }

    private void insertDBSKU(){

        DbHelper dbHelper = new DbHelper(MainActivity.this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        db.execSQL(SKUContract.SQL_DELETE_SKU);
        db.execSQL(SKUContract.SQL_CREATE_SKU);

        for(int i = 0; i < lsSKU.size(); i++){
            ContentValues values = new ContentValues();
            values.put(SKUContract.SKUEntry.COLUMN_NAME_ISBN, lsSKU.get(i).getISBN());
            values.put(SKUContract.SKUEntry.COLUMN_NAME_SKU, lsSKU.get(i).getSKU());
            values.put(SKUContract.SKUEntry.COLUMN_NAME_JUDUL, lsSKU.get(i).getJudul());
            values.put(SKUContract.SKUEntry.COLUMN_NAME_DISTRIBUTOR, lsSKU.get(i).getDistributor());
            values.put(SKUContract.SKUEntry.COLUMN_NAME_HARGA_JUAL, lsSKU.get(i).getHargaJual());

            long insertID = db.insert(SKUContract.SKUEntry.TABLE_NAME, null, values);
        }
    }

}
