package com.example.reto_1_2_sqlite;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private Button btnRegister;
    private static Intent myIntent;
    public static boolean usuariosVacios;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DBHandler dbhandler = new DBHandler(MainActivity.this);

        //If usuarios table is empty, there are no users registered
        if (dbhandler.countTable("usuarios")) {
            dbhandler.close();
            myIntent = new Intent(
                    MainActivity.this,
                    RegisterActivity.class);
            startActivity(myIntent);
        }

        Button btnLogin = findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText edtUser = findViewById(R.id.edt_usr);
                String sUser = edtUser.getText().toString();

                if (sUser.isEmpty()) {

                } else {
                    DBHandler handler = new DBHandler(MainActivity.this);

                    if (dbhandler.searchUser("nombre", sUser)) {
                        EditText edtPassword = findViewById(R.id.edt_psswd);
                        String sPassword = edtPassword.getText().toString();

                    }
                }
            }
        });

        Button btnRegister = findViewById(R.id.btn_registrar);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Open the register activity
                myIntent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(myIntent);
            }
        });
    }
}