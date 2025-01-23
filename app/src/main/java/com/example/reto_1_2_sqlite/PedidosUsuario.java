package com.example.reto_1_2_sqlite;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class PedidosUsuario extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_consulta_pedidos);
        FloatingActionButton nuevoPedido= findViewById(R.id.btNuevoPedido);
        nuevoPedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent =  new Intent(
                        PedidosUsuario.this,
                        FormulariosPedido.class

                );
                startActivity(myIntent);
            }
        });

        /////////////////////////////////////////////////Footer/////////////////////////////////////////////
        Button btnIni = findViewById(R.id.btnInicio);
        btnIni.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(PedidosUsuario.this, MainActivity.class);
                startActivity(myIntent);
            }
        });


        Button btnCnPed = findViewById(R.id.btnConPed);
        btnCnPed.setEnabled(false);

        Button btnCnPar = findViewById(R.id.btnConPar);
        btnCnPar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(PedidosUsuario.this, ConsultaPartners.class);
                startActivity(myIntent);
            }
        });
    }
}