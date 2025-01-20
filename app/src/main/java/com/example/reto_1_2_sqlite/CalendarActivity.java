package com.example.reto_1_2_sqlite;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.Serializable;
import java.util.ArrayList;

public class CalendarActivity extends AppCompatActivity implements Serializable {
    public User user;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_pantalla_calendario);

        //Recuperamos la información del usuario registrado
        user = (User) getIntent().getSerializableExtra("cUser");

        //Obtenemos la referencia al RecyclerView
        RecyclerView rclView = findViewById(R.id.rcVisitas);

        //Recuperamos el User que ha iniciado sesión
        user = (User) getIntent().getSerializableExtra("cUser");

        //Creamos el gestor para la conexión a la base de datos sqlite
        DBHandler handler = new DBHandler(this);

        //Recuperamos las visitas del usuario conectado de la base de datos
        ArrayList<Visita> visitas = handler.getArrayVisitas(user);
        handler.close();

        //Creamos y establecemos el adaptador para la lista de visitas
        VisitasAdapter adapter = new VisitasAdapter(visitas, this);
        rclView.setAdapter(adapter);
        rclView.setLayoutManager(new LinearLayoutManager(this));

        //Acción de escucha para el botón de añadir visitas
        FloatingActionButton btnAdd = findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(
                        CalendarActivity.this,
                        AnadirVisitas.class
                );
                myIntent.putExtra("cUser", user);
                startActivity(myIntent);
            }
        });
    }

    /*
    Para refrescar la ventana cuando le damos al botón atrás en la barra de navegación del teléfono
     */
    @Override
    public void onRestart() {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }
}
