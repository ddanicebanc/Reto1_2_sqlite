package com.example.reto_1_2_sqlite;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.Serializable;

public class PantallaPrincipal extends AppCompatActivity implements Serializable {
    ImageButton btnCalendar;
    User user;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);

        user = (User) getIntent().getSerializableExtra("cUser");

        btnCalendar = findViewById(R.id.btn_calendar);
        btnCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(
                        PantallaPrincipal.this,
                        CalendarActivity.class
                );
                myIntent.putExtra("cUser", user);
                startActivity(myIntent);
            }
        });
    }
}
