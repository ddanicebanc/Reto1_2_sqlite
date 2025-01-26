package com.example.reto_1_2_sqlite.consultas;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.reto_1_2_sqlite.DBHandler;
import com.example.reto_1_2_sqlite.R;
import com.example.reto_1_2_sqlite.adaptadores.CatalogoAdapter;
import com.example.reto_1_2_sqlite.modelos.Articulo;
import com.example.reto_1_2_sqlite.modelos.User;

import java.io.Serializable;
import java.util.ArrayList;

public class ConsultaCatalogo extends AppCompatActivity implements Serializable {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.con_catalogo);

        Intent startIntent = getIntent();
        Bundle extras = startIntent.getExtras();

        User user = (User) extras.getSerializable("cUser");
        DBHandler handler = new DBHandler(this);

        ArrayList<Articulo> articulos = handler.getArrayArticulos(user);

        RecyclerView rclCatalogo = findViewById(R.id.rclCatalogo);
        CatalogoAdapter adapter = new CatalogoAdapter(articulos, this);
        rclCatalogo.setAdapter(adapter);
        rclCatalogo.setLayoutManager(new LinearLayoutManager(this));
    }
}
