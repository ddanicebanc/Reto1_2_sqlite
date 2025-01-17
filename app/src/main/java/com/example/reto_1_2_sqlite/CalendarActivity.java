package com.example.reto_1_2_sqlite;

import android.os.Bundle;
import android.os.PersistableBundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CalendarActivity extends AppCompatActivity {
    private User user;
    private ArrayList<Visita> visitas;
    private RecyclerView rclView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_pantalla_calendario);

        //Obtenemos la referencia al RecyclerView
        rclView = findViewById(R.id.rcVisitas);

        //Recuperamos el User que ha iniciado sesión
        user = (User) getIntent().getSerializableExtra("cUser");

        //Creamos el gestor para la conexión a la base de datos sqlite
        DBHandler handler = new DBHandler(this);

        //Recuperamos las visitas del usuario conectado de la base de datos
        visitas = handler.getArrayVisitas(user);
        handler.close();

        //Creamos y establecemos el adaptador para la lista de visitas
        VisitasAdapter adapter = new VisitasAdapter(visitas, this);
        rclView.setAdapter(adapter);
        rclView.setLayoutManager(new LinearLayoutManager(this));
    }
}
