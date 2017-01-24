package com.projects.devin.opname.apps;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.projects.devin.opname.R;

public class OpnameRelasiActivity extends AppCompatActivity {

    private EditText relasiText;
    public static Activity relasiActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        relasiActivity = this;
        setContentView(R.layout.activity_opname_relasi);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Barcode Relasi");

        initComponent();
    }

    private void initComponent(){
        relasiText = (EditText) findViewById(R.id.barcode_relasi_text);
        relasiText.setSingleLine();
        relasiText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(event.getAction() == KeyEvent.ACTION_DOWN){
                    switch(keyCode){
                        case KeyEvent.KEYCODE_ENTER:
                            String relasi = relasiText.getText().toString();
                            Intent i = new Intent(OpnameRelasiActivity.this, OpnameInputActivity.class);
                            i.putExtra("RELASI", relasi);
                            startActivity(i);
                            Toast.makeText(getApplicationContext(), relasi, Toast.LENGTH_LONG).show();
                            break;
                        default:
                    }
                }
                return false;
            }
        });
    }



    public void onBackPressed(){
        finish();
    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case android.R.id.home: onBackPressed();
                return true;
            default : return super.onOptionsItemSelected(item);
        }
    }
}
