package com.example.reto_1_2_sqlite.consultas;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.reto_1_2_sqlite.PantallaPrincipal;
import com.example.reto_1_2_sqlite.conexiones.DBHandler;
import com.example.reto_1_2_sqlite.R;
import com.example.reto_1_2_sqlite.adaptadores.PartnersAdapter;
import com.example.reto_1_2_sqlite.anadir.AniadirPartners;
import com.example.reto_1_2_sqlite.modelos.Partner;
import com.example.reto_1_2_sqlite.modelos.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.Serializable;
import java.util.ArrayList;

public class ConsultaPartners extends AppCompatActivity implements Serializable, PartnersAdapter.ItemClickListener {
    public User user;
    public DBHandler handler;
    public EditText editIdPartner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.con_partners);
        Intent startIntent = getIntent();
        Bundle extras = startIntent.getExtras();
        editIdPartner = findViewById(R.id.editIdPartner);
        ArrayList<Partner> partners = new ArrayList<>();

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

        if (extras != null){
            int partnerId = extras.getInt("partnerId");

            if (partnerId == 0){
                partnerId = -1;
            }else{
                editIdPartner.setText(String.valueOf(extras.getInt("partnerId")));
            }

            partners = handler.getArrayPartners(user,partnerId,"");
        }else{
            partners = handler.getArrayPartners(user,-1,"");
        }

        //Referencia de recycleview
        RecyclerView rclPartner = findViewById(R.id.recyclerPartners);

        //Recuperamos los partners del ususario conectado
        handler.close();

        //Creamos y establecemos el adaptador para la lista de visitas
        PartnersAdapter adapter = new PartnersAdapter(partners, this);
        rclPartner.setAdapter(adapter);
        rclPartner.setLayoutManager(new LinearLayoutManager(this));
        adapter.setOnClickListener(this::onClick);

        // Referencia al botón y configuración del evento click
        Button validarButton = findViewById(R.id.buttonValidar);
        validarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validarIdPartner() == true){
                    int tempPartnerId;

                    if (editIdPartner.getText().toString().isEmpty()) {
                        tempPartnerId = 0;
                    } else {
                        tempPartnerId = Integer.parseInt(editIdPartner.getText().toString());
                    }
                    startIntent.putExtra("partnerId", tempPartnerId);
                    startIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    recreate();
                }
            }
        });

        FloatingActionButton btnAdd = findViewById(R.id.btnAddPartner);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(
                        ConsultaPartners.this,
                        AniadirPartners.class
                );
                i.putExtra("cUser", user);
                startActivity(i);
                finish();
            }
        });

        //Funcionamiento de los botones del footer
        ImageButton btnHome = findViewById(R.id.btnHome);
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(
                        ConsultaPartners.this,
                        PantallaPrincipal.class
                );
                i.putExtra("cUser", user);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                finish();
            }
        });

        ImageButton btnCal = findViewById(R.id.btnAg);
        btnCal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(
                        ConsultaPartners.this,
                        ConsultaVisitas.class
                );
                i.putExtra("cUser", user);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                finish();
            }
        });

        ImageButton btnCat = findViewById(R.id.btnCat);
        btnCat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(
                        ConsultaPartners.this,
                        ConsultaCatalogo.class
                );
                i.putExtra("cUser", user);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                finish();
            }
        });

        ImageButton btnPed = findViewById(R.id.btnPed);
        btnPed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(
                        ConsultaPartners.this,
                        ConsultaPedidos.class
                );
                i.putExtra("cUser", user);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                finish();
            }
        });
    }

    /**
     * Valida el ID de partner ingresado por el usuario.
     *
     * Verifica si el campo está vacío (se considera válido) o si contiene solo dígitos numéricos.
     * Si el ID no cumple con estos criterios, muestra un mensaje de error Toast.
     *
     * @return {@code true} si el ID es válido (está vacío o contiene solo números), {@code false} si no es válido.
     */
    private boolean validarIdPartner() {
        // Referencia al EditText del ID
        editIdPartner = findViewById(R.id.editIdPartner);

        // Obtener el valor ingresado
        String idPartner = editIdPartner.getText().toString().trim();

        if (idPartner.isEmpty()) {
            return true;
        }

        if (!idPartner.matches("\\d+")) {
            Toast.makeText(this, "El campo 'ID_Partner' solo puede contener números", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }
    /**
     * Método personalizado de escucha para los elementos del recyclerview
     * @param view La {@link View} en la que se hizo clic.
     * @param position La posición del elemento en el que se hizo clic.
     */
    @Override
    public void onClick(View view, int position) {}
}

