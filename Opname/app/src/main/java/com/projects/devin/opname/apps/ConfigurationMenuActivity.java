package com.projects.devin.opname.apps;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.projects.devin.opname.R;

public class ConfigurationMenuActivity extends AppCompatActivity {

    private Button changePasswordButton, printerButton, paperButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuration_menu);

        initComponent();
    }

    private void initComponent(){
        changePasswordButton = (Button) findViewById(R.id.change_password_button);
        printerButton = (Button) findViewById(R.id.printer_button);
        paperButton = (Button) findViewById(R.id.paper_button);

        changePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ConfigurationMenuActivity.this, ChangePasswordActivity.class);
                startActivity(i);
            }
        });
    }
}
