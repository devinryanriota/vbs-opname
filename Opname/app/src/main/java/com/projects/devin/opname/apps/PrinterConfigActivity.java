package com.projects.devin.opname.apps;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.projects.devin.opname.R;

public class PrinterConfigActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_printer_config);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Printer Configuration");
    }
}
