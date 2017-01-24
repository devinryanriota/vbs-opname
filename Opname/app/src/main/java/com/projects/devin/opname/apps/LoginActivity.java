package com.projects.devin.opname.apps;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.projects.devin.opname.R;
import com.projects.devin.opname.cls.DbHelper;
import com.projects.devin.opname.cls.UserContract;

import java.io.File;
import java.util.Date;

public class LoginActivity extends AppCompatActivity {

    private EditText usernameText, passwordText;
    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initComponent();
        createDirectory();
    }

    protected void initComponent(){
        usernameText = (EditText) findViewById(R.id.username_text);
        passwordText = (EditText) findViewById(R.id.password_text);
        loginButton = (Button) findViewById(R.id.login_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doLogin();
            }
        });


        Date date = new Date();
        int day = date.getDate();
        int month = date.getMonth() + 1;
        int year = date.getYear() - 100;

        usernameText.setText(String.valueOf(String.format("%02d", day)) + " - " + String.valueOf(String.format("%02d", month)) + " - " + String.valueOf(String.format("%02d", year)));

        //Toast.makeText(getApplicationContext(), String.valueOf(day) + "-" + String.valueOf(month) + " - " + String.valueOf(year), Toast.LENGTH_LONG).show();

    }

    private void doLogin(){
        String username = usernameText.getText().toString();
        String password = passwordText.getText().toString();

        if(username.equals("")){
            usernameText.setError("Username must be filled");
        }
        else if(password.equals("")){
            passwordText.setError("Password must be filled");
        }
        else{
            DbHelper dbHelper = new DbHelper(LoginActivity.this);
            SQLiteDatabase db = dbHelper.getReadableDatabase();

            Cursor cursor = db.query(
                    UserContract.UserEntry.TABLE_NAME,
                    new String[]{
                            UserContract.UserEntry.COLUMN_NAME_USERNAME
                    },
                    String.format("%s=? AND %s=?",
                            UserContract.UserEntry.COLUMN_NAME_USERNAME,
                            UserContract.UserEntry.COLUMN_NAME_PASSWORD),
                    new String[]{
                        username, password
                    },
                    null,
                    null,
                    null
            );
            cursor.moveToFirst();

            if(cursor.getCount() == 1){
                Toast.makeText(getApplicationContext(), "Login Success", Toast.LENGTH_SHORT).show();

                SharedPreferences.Editor prefEdit = getSharedPreferences("SESSION_PREFERENCES", MODE_PRIVATE).edit();
                prefEdit.putString("username", username);
                prefEdit.commit();

                Intent i = new Intent(LoginActivity.this, MainMenuActivity.class);
                startActivity(i);
            }
            else{
                Toast.makeText(getApplicationContext(), "Login Failed", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void createDirectory(){
        String folder = "StockOpname";

        File f = new File(Environment.getExternalStorageDirectory(), folder);
        if(!f.exists()) {
            f.mkdirs();
            Log.d("DIR", "Create Success");
        }

    }
}
