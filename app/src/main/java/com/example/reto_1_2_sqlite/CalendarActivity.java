package com.example.reto_1_2_sqlite;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

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
        boolean check = false;
        Intent startIntent = getIntent();
        Bundle extras = startIntent.getExtras();

        //Recuperamos la información del usuario registrado
        user = (User) getIntent().getSerializableExtra("cUser");

        CheckBox cboxHistorico = findViewById(R.id.cboxHistorico);
        if (extras != null) {
            cboxHistorico.setChecked(extras.getBoolean("check"));
        }
        cboxHistorico.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    startIntent.putExtra("check", true);
                    startIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    recreate();
                } else {
                    startIntent.putExtra("check", false);
                    startIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    recreate();
                }
            }
        });

        //Obtenemos la referencia al RecyclerView
        RecyclerView rclView = findViewById(R.id.rcVisitas);

        //Creamos el gestor para la conexión a la base de datos sqlite
        DBHandler handler = new DBHandler(this);

        //Recuperamos las visitas del usuario conectado de la base de datos
        ArrayList<Visita> visitas = handler.getArrayVisitas(user, cboxHistorico.isChecked());
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
