package com.example.reto_1_2_sqlite;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class AnadirVisitas extends AppCompatActivity implements Serializable {
    public static String nombre, fecha, direccion;
    private EditText editFecha, editDireccion;
    private Spinner spnNombrePartners;
    DBHandler handler;
    public static ArrayList<String> partnerNames = new ArrayList<>();
    public static ArrayList<String> partnerIds = new ArrayList<>();
    public int selectedPartnerIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_pantalla_anadir_visitas);

        User user = (User) getIntent().getSerializableExtra("cUser");
        handler = new DBHandler(AnadirVisitas.this);

        // Inicializar los campos y el botón
        editFecha = findViewById(R.id.editFecha);
        editDireccion = findViewById(R.id.editDireccion);
        Button guardarButton = findViewById(R.id.Guardar);

        //Preparación de los datos para el spinner
        partnerNames = handler.getSearchFieldArray("partners","nombre");
        partnerIds = handler.getSearchFieldArray("partners", "id");

        spnNombrePartners = findViewById(R.id.spnNombrePartner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                partnerNames
        );
        spnNombrePartners.setAdapter(adapter);
        spnNombrePartners.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedPartnerIndex = Integer.parseInt(partnerIds.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

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
                    datos.add(String.valueOf(selectedPartnerIndex));

                    columnas.add("fechaVisita");
                    //Para dar el formato adecuado a la fecha introducida
                    String[] partesFecha = editFecha.getText().toString().split("/");
                    fecha = "";
                    for (int i = partesFecha.length - 1; i >= 0; i--) {
                        if (i != 0) {
                            fecha = fecha + partesFecha[i] + "-";
                        } else {
                            fecha = fecha + partesFecha[i];
                        }
                    }
                    datos.add(fecha);

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
        fecha = editFecha.getText().toString().trim();
        direccion = editDireccion.getText().toString().trim();
        boolean validado = true;

        if (fecha.isEmpty() || direccion.isEmpty()) {
            Toast.makeText(this, "Todos los campos deben estar llenos", Toast.LENGTH_LONG).show();
            validado = false;
        } else {
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
