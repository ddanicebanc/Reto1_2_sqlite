package com.example.reto_1_2_sqlite.consultas;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.reto_1_2_sqlite.anadir.AnadirVisitas;
import com.example.reto_1_2_sqlite.conexiones.DBHandler;
import com.example.reto_1_2_sqlite.R;
import com.example.reto_1_2_sqlite.adaptadores.VisitasAdapter;
import com.example.reto_1_2_sqlite.modelos.User;
import com.example.reto_1_2_sqlite.modelos.Visita;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Locale;

public class ConsultaVisitas extends AppCompatActivity implements Serializable {
    public User user;
    public DBHandler handler;
    public TextView txvTitulo;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.con_visitas);
        boolean check = false;
        Intent startIntent = getIntent();
        Bundle extras = startIntent.getExtras();

        //Recuperamos la información del usuario registrado
        user = (User) getIntent().getSerializableExtra("cUser");

        //Creamos el gestor para la conexión a la base de datos sqlite
        handler = new DBHandler(this);

        //Asignación del id a los elementos en pantalla
        txvTitulo = findViewById(R.id.txvTituloVisitas);

        cargarTitulo();

        CheckBox cboxHistorico = findViewById(R.id.cboxHistorico);
        if (extras != null) {
            cboxHistorico.setChecked(extras.getBoolean("check"));
        }
        cboxHistorico.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //Relanzamos la actividad con un nuevo extra en la intención
                if (isChecked) {
                    startIntent.putExtra("check", true);
                    startIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    //Destruye la actividad actual y lanza la nueva manteniendo la intención
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
                        ConsultaVisitas.this,
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

    public void cargarTitulo () {
        String region = Locale.getDefault().toString();
        String titulo;
        String userName = user.getName();

        if (region.startsWith("es_")) {
            titulo = "Agenda de " + userName.toUpperCase();
        } else {
            titulo = userName.toUpperCase() + "'s agenda";
        }

        txvTitulo.setText(titulo);
    }
}
