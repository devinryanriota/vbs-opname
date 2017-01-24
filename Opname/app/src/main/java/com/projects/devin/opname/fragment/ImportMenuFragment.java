package com.projects.devin.opname.fragment;

import android.app.Fragment;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.projects.devin.opname.R;
import com.projects.devin.opname.adapter.MenuGridAdapter;
import com.projects.devin.opname.apps.MainActivity;
import com.projects.devin.opname.apps.MainMenuActivity;
import com.projects.devin.opname.cls.DbHelper;
import com.projects.devin.opname.cls.MenuList;
import com.projects.devin.opname.cls.SKU;
import com.projects.devin.opname.cls.SKUContract;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by devin on 1/21/2017.
 */

public class ImportMenuFragment extends Fragment {
    private View view;
    private GridView menuGrid;
    private List<MenuList> lsMenu;
    private List<SKU> lsSKU;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_import_menu, container, false);

        ((MainMenuActivity)getActivity()).getSupportActionBar().setTitle("Import Menu");
        initComponent();

        return view;
    }

    private void initComponent(){
        menuGrid = (GridView) view.findViewById(R.id.import_menu_grid);
        lsSKU = new ArrayList<SKU>();
        lsMenu = new ArrayList<>();
        lsMenu.add(new MenuList(BitmapFactory.decodeResource(getResources(), R.drawable.ic_employee_attendance), "Import Master SKU"));

        menuGrid.setAdapter(new MenuGridAdapter(getActivity(), lsMenu));
        menuGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(lsMenu.get(position).getTitle().equalsIgnoreCase("Import Master SKU")){
                    importDataSKU();
                }
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
            Toast.makeText(getActivity(), text, Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(getActivity(), "File doesn't exists", Toast.LENGTH_SHORT).show();
        }
    }

    private void insertDBSKU(){

        DbHelper dbHelper = new DbHelper(getActivity());
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
