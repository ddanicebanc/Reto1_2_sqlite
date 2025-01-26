package com.example.reto_1_2_sqlite;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.reto_1_2_sqlite.modelos.User;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;

public class AnadirVisitas extends AppCompatActivity implements Serializable {
public static String fechaDato, direccion;
    private EditText editFecha, editDireccion;
    private Spinner spnNombrePartners;
    DBHandler handler;
    public static ArrayList<String> partnerNames = new ArrayList<>();
    public static ArrayList<String> partnerIds = new ArrayList<>();
    public int selectedPartnerIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.an_visitas);

        User user = (User) getIntent().getSerializableExtra("cUser");
        handler = new DBHandler(AnadirVisitas.this);

        // Inicializar los campos y el botón
        editFecha = findViewById(R.id.editFecha);
        editDireccion = findViewById(R.id.editDireccion);
        Button guardarButton = findViewById(R.id.Guardar);

        //Selección de la fecha a través de la UI
        editFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();

                int anio = c.get(Calendar.YEAR);
                int mes = c.get(Calendar.MONTH);
                int dia = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        // on below line we are passing context.
                        AnadirVisitas.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // on below line we are setting date to our edit text.
                                editFecha.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                                fechaDato = "";
                                fechaDato = fechaDato + year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                            }
                        },
                        // on below line we are passing year,
                        // month and day for selected date in our date picker.
                        anio, mes, dia);
                // at last we are calling show to
                // display our date picker dialog.
                datePickerDialog.show();
            }
        });

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
                    datos.add(fechaDato);

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
        direccion = editDireccion.getText().toString().trim();
        boolean validado = true;

        if (fechaDato.isEmpty() || direccion.isEmpty()) {
            Toast.makeText(this, "Todos los campos deben estar llenos", Toast.LENGTH_LONG).show();
            validado = false;
        } else {
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
}
