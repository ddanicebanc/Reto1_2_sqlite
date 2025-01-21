package com.example.reto_1_2_sqlite;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class AnadirVisitas extends AppCompatActivity implements Serializable {
    public static String nombre, fecha, direccion;

    private EditText editNombreVista, editFecha, editDireccion;
    private Button guardarButton;
    DBHandler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_pantalla_anadir_visitas);

        User user = (User) getIntent().getSerializableExtra("cUser");
        handler = new DBHandler(AnadirVisitas.this);

        // Inicializar los campos y el botón
        editNombreVista = findViewById(R.id.editNombreVista);
        editFecha = findViewById(R.id.editFecha);
        editDireccion = findViewById(R.id.editDireccion);
        guardarButton = findViewById(R.id.Guardar);

        // Configurar el listener del botón
        guardarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validarCampos()) {
                    ArrayList<String> columnas = new ArrayList<>();
                    ArrayList<String> datos = new ArrayList<>();

                    columnas.add("usuarioId");
                    datos.add(String.valueOf(user.getId()));

                    columnas.add("partnerId");
                    datos.add(String.valueOf(user.getId()));

                    columnas.add("fechaVisita");
                    datos.add(editFecha.getText().toString());

                    columnas.add("direccion");
                    datos.add(editDireccion.getText().toString());

                    handler.insertData("visitas", columnas, datos);
                    finish();
                }
            }
        });
    }

    private boolean validarCampos() {
        //TODO Cambiar toast por AlertDialog
        nombre = editNombreVista.getText().toString().trim();
        fecha = editFecha.getText().toString().trim();
        direccion = editDireccion.getText().toString().trim();
        boolean validado = true;

        if (TextUtils.isEmpty(nombre) || TextUtils.isEmpty(fecha) || TextUtils.isEmpty(direccion)) {
            Toast.makeText(this, "Todos los campos deben estar llenos", Toast.LENGTH_LONG).show();
            validado = false;
        } else {
            //Query para comprobar que el nombre del partner introducido existe
            if (!handler.searchByName("partners", "nombre", nombre)) {
                Toast.makeText(this,
                        "El campo 'El partner seleccionado no existe.",
                        Toast.LENGTH_LONG)
                        .show();
                validado = false;
            }
            if (!isFechaValida(fecha)) {
                Toast.makeText(this,
                        "El campo 'Fecha' debe contener una fecha válida (dd/mm/yyyy)",
                        Toast.LENGTH_LONG)
                        .show();
                validado = false;
            }
            if (!direccion.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ0-9 ]+")) {
                Toast.makeText(this,
                        "El campo 'Dirección' solo puede contener letras y números",
                        Toast.LENGTH_LONG)
                        .show();
                validado = false;
            }
        }

        return validado;
    }

    private boolean isFechaValida(String fecha) {
        SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
        formatoFecha.setLenient(false); // Desactiva la tolerancia para fechas inválidas
        try {
            Date date = formatoFecha.parse(fecha);
            return date != null; // Verifica que se pudo convertir correctamente
        } catch (ParseException e) {
            return false;
        }
    }
}
