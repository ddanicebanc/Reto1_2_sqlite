package com.example.reto_1_2_sqlite;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.reto_1_2_sqlite.modelos.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.Serializable;

public class ConsultaPedidos extends AppCompatActivity implements Serializable {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_consulta_pedidos);
        Intent startIntent = getIntent();
        Bundle extras = startIntent.getExtras();

        User user = (User) startIntent.getSerializableExtra("cUser");

        FloatingActionButton btnAdd = findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(
                        ConsultaPedidos.this,
                        AnadirCabeceraPedido.class
                );
                intent.putExtra("cUser", user);
                startActivity(intent);
            }
        });
    }
}
