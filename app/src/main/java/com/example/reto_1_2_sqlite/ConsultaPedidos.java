package com.example.reto_1_2_sqlite;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.reto_1_2_sqlite.adaptadores.PedidosAdapter;
import com.example.reto_1_2_sqlite.modelos.CabeceraPedido;
import com.example.reto_1_2_sqlite.modelos.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.Serializable;
import java.util.ArrayList;

public class ConsultaPedidos extends AppCompatActivity implements Serializable {
    DBHandler handler;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_consulta_pedidos);
        Intent startIntent = getIntent();
        Bundle extras = startIntent.getExtras();

        User user = (User) startIntent.getSerializableExtra("cUser");
        handler = new DBHandler(this);

        //Configuración del recyclerView
        //Inicializar el arraylist con los pedidos
        ArrayList<CabeceraPedido> pedidos = handler.getArrayPedidos(user);
        //Asignar el elemento del layout al objeto
        RecyclerView rclPedidos = findViewById(R.id.rclPedidos);
        //Crear el adaptador y el LayoutManager para mostrar la información en el recyclerView
        PedidosAdapter adapter = new PedidosAdapter(pedidos, this);
        rclPedidos.setAdapter(adapter);
        rclPedidos.setLayoutManager(new LinearLayoutManager(this));

        handler.close();

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
