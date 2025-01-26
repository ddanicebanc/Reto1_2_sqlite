package com.example.reto_1_2_sqlite;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.reto_1_2_sqlite.consultas.CalendarActivity;
import com.example.reto_1_2_sqlite.consultas.ConsultaPartners;
import com.example.reto_1_2_sqlite.consultas.ConsultaPedidos;
import com.example.reto_1_2_sqlite.modelos.User;

import java.io.Serializable;

public class PantallaPrincipal extends AppCompatActivity implements Serializable {
    ImageButton btnCalendar, btnPartners, btnPedidos;
    User user;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_pantalla_principal);

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

        btnPartners = findViewById(R.id.btn_partners);
        btnPartners.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(
                        PantallaPrincipal.this,
                        ConsultaPartners.class
                );
                myIntent.putExtra("cUser", user);
                startActivity(myIntent);
            }
        });

        btnPedidos = findViewById(R.id.btnPedidos);
        btnPedidos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(
                        PantallaPrincipal.this,
                        ConsultaPedidos.class
                );
                myIntent.putExtra("cUser", user);
                startActivity(myIntent);
            }
        });
    }
}
