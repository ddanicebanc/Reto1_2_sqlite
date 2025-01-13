package com.example.reto_1_2_sqlite;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class PantallaPrincipal extends AppCompatActivity {
    ImageButton btnAgenda;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);

        btnAgenda = findViewById(R.id.btn_calendar);
        btnAgenda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(
                        PantallaPrincipal.this,
                        CalendarActivity.class
                );
                startActivity(myIntent);
            }
        });
    }
}
