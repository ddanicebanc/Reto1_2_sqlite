package com.example.reto_1_2_sqlite;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AnadirVisitas extends AppCompatActivity implements Serializable {

    private EditText editNombreVista, editFecha, editDireccion;
    private Button guardarButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.layout_pantalla_anadir_visitas);

        // Inicializar los campos y el botón
        editNombreVista = findViewById(R.id.editNombreVista);
        editFecha = findViewById(R.id.editFecha);
        editDireccion = findViewById(R.id.editDireccion);
        guardarButton = findViewById(R.id.Guardar);

        // Configurar el listener del botón
        guardarButton.setOnClickListener(v -> validarCampos());
    }

    private void validarCampos() {
        String nombre = editNombreVista.getText().toString().trim();
        String fecha = editFecha.getText().toString().trim();
        String direccion = editDireccion.getText().toString().trim();

        if (TextUtils.isEmpty(nombre) || TextUtils.isEmpty(fecha) || TextUtils.isEmpty(direccion)) {
            Toast.makeText(this, "Todos los campos deben estar llenos", Toast.LENGTH_LONG).show();
            return;
        }

        if (!nombre.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+")) {
            Toast.makeText(this, "El campo 'Nombre' solo puede contener letras", Toast.LENGTH_LONG).show();
            return;
        }

        if (!isFechaValida(fecha)) {
            Toast.makeText(this, "El campo 'Fecha' debe contener una fecha válida (dd/mm/yyyy)", Toast.LENGTH_LONG).show();
            return;
        }

        if (!direccion.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ0-9 ]+")) {
            Toast.makeText(this, "El campo 'Dirección' solo puede contener letras y números", Toast.LENGTH_LONG).show();
            return;
        }

        // Si pasa todas las validaciones
        Toast.makeText(this, "Datos guardados correctamente", Toast.LENGTH_LONG).show();
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
