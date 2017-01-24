package com.projects.devin.opname.apps;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.projects.devin.opname.R;
import com.projects.devin.opname.cls.DbHelper;
import com.projects.devin.opname.cls.UserContract;

public class ChangePasswordActivity extends AppCompatActivity {

    private EditText newPassText, confirmPassText;
    private Button changePassButton;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Change Password");

        initComponent();
    }

    private void initComponent(){
        newPassText = (EditText) findViewById(R.id.new_password_text);
        confirmPassText = (EditText) findViewById(R.id.confirm_password_text);
        changePassButton = (Button) findViewById(R.id.change_password_button);

        changePassButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePassword();
            }
        });

        SharedPreferences pref = getApplicationContext().getSharedPreferences("SESSION_PREFERENCES", MODE_PRIVATE);
        username = pref.getString("username", "");
    }

    private void changePassword(){
        String password = newPassText.getText().toString();
        String confirmPassword = confirmPassText.getText().toString();

        if(password.equals("")){
            newPassText.setError("Password must be filled!");
        }
        else if(confirmPassword.equals("")){
            confirmPassText.setError("Password must be filled!");
        }
        else if(!password.equals(confirmPassword)){
            Toast.makeText(getApplicationContext(), "New Password doesn't match!", Toast.LENGTH_LONG).show();
        }
        else if(password.equals(confirmPassword)){
            DbHelper dbHelper = new DbHelper(ChangePasswordActivity.this);
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(
                    UserContract.UserEntry.COLUMN_NAME_PASSWORD,
                    password
            );

            int count = db.update(
                    UserContract.UserEntry.TABLE_NAME,
                    values,
                    String.format("%s = ?", UserContract.UserEntry.COLUMN_NAME_USERNAME),
                    new String[] {username}
            );

            if(count > 0){
                Toast.makeText(getApplicationContext(), "Password Changed Successfully!", Toast.LENGTH_SHORT).show();
                finish();
            }
            else{
                Toast.makeText(getApplicationContext(), "Password Change Unsuccessful!", Toast.LENGTH_SHORT).show();
            }
        }
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
