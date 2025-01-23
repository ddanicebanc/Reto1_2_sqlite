package com.example.reto_1_2_sqlite;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.reto_1_2_sqlite.adaptadores.PartnersAdapter;
import com.example.reto_1_2_sqlite.modelos.Partner;

import java.io.Serializable;
import java.util.ArrayList;

public class ConsultaPartners extends AppCompatActivity implements Serializable {
    public User user;
    public DBHandler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.layout_consulta_partners);

        //Recuperamos la información del usuario registrado
        user = (User) getIntent().getSerializableExtra("cUser");

        //Creamos el gestor para la conexión a la base de datos sqlite
        handler = new DBHandler(this);

        // Configuración de las barras del sistema
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //Referencia de recycleview
        RecyclerView rclPartner = findViewById(R.id.recyclerPartners);

        //Recuperamos los partners del ususario conectado
        ArrayList<Partner> partners = handler.getArrayPartners(user);
        handler.close();

        //Creamos y establecemos el adaptador para la lista de visitas
        PartnersAdapter adapter = new PartnersAdapter(partners, this);
        rclPartner.setAdapter(adapter);
        rclPartner.setLayoutManager(new LinearLayoutManager(this));

        // Referencia al botón y configuración del evento click
        Button validarButton = findViewById(R.id.buttonValidar);
        validarButton.setOnClickListener(v -> validarIdPartner());

        /////////////////////////////////////////////////Footer/////////////////////////////////////////////
        Button btnIni = findViewById(R.id.btnInicio);
        btnIni.setOnClickListener(view -> {
            Intent myIntent = new Intent(ConsultaPartners.this, MainActivity.class);
            startActivity(myIntent);
        });

        Button btnCnPed = findViewById(R.id.btnConPed);
        btnCnPed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(ConsultaPartners.this, PedidosUsuario.class);
                startActivity(myIntent);
            }
        });

        Button btnCnPar = findViewById(R.id.btnConPar);
        btnCnPar.setEnabled(false);

    }

    private void validarIdPartner() {
        // Referencia al EditText del ID
        EditText editIdPartner = findViewById(R.id.editIdPartner);

        // Obtener el valor ingresado
        String idPartner = editIdPartner.getText().toString().trim();

        // Validaciones
        if (idPartner.isEmpty()) {
            Toast.makeText(this, "El campo 'ID_Partner' no puede estar vacío", Toast.LENGTH_LONG).show();
            return;
        }

        if (!idPartner.matches("\\d+")) {
            Toast.makeText(this, "El campo 'ID_Partner' solo puede contener números", Toast.LENGTH_LONG).show();
            return;
        }
    }
}

