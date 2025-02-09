package com.example.reto_1_2_sqlite.anadir;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.reto_1_2_sqlite.R;
import com.example.reto_1_2_sqlite.conexiones.DBHandler;
import com.example.reto_1_2_sqlite.modelos.User;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
/**
 * {@code AnadirVisitas} es una {@link AppCompatActivity} que permite al usuario añadir nuevas visitas.
 * La actividad permite seleccionar la fecha de la visita, el socio y la dirección.
 */
public class AnadirVisitas extends AppCompatActivity implements Serializable {
    public static String fechaDato = "", direccion;
    private EditText editFecha, editDireccion;
    private Spinner spnNombrePartners;
    DBHandler handler;
    public static ArrayList<String> partnerNames = new ArrayList<>();
    public static ArrayList<Integer> partnerIds = new ArrayList<>();
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
            /**
             * Se llama cuando se hace clic en el campo de fecha.  Muestra un diálogo de
             * selección de fecha.
             * @param v La {@link View} en la que se hizo clic.
             */
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
                            /**
                             * Se llama cuando se establece la fecha en el diálogo.
                             * @param view El {@link DatePicker} que contiene la fecha seleccionada.
                             * @param year El año seleccionado.
                             * @param monthOfYear El mes seleccionado (0-11).
                             * @param dayOfMonth El día del mes seleccionado.
                             */
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // on below line we are setting date to our edit text.
                                editFecha.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                                String sDay = String.valueOf(dayOfMonth);
                                if (sDay.length() == 1) {
                                    sDay = "0" + sDay;
                                }
                                String sMonth = String.valueOf((monthOfYear + 1));
                                if (sMonth.length() == 1) {
                                    sMonth = "0" + sMonth;
                                }

                                fechaDato = sMonth + "-" + sDay + "-" + year;
                            }
                        },
                        // on below line we are passing year, month and day
                        // for selected date in our date picker.
                        anio, mes, dia);
                // at last we are calling show to display our date picker dialog.
                datePickerDialog.getDatePicker().setFirstDayOfWeek(Calendar.MONDAY);

                datePickerDialog.show();
            }
        });

        //Preparación de los datos para el spinner
        partnerNames = handler.getNombrePartners(user);
        partnerIds = handler.getIdPartners(user);

        spnNombrePartners = findViewById(R.id.spnNombrePartner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                partnerNames
        );
        spnNombrePartners.setAdapter(adapter);
        spnNombrePartners.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            /**
             * Se llama cuando se selecciona un elemento del spinner.
             * @param parent El {@link AdapterView} padre.
             * @param view La {@link View} seleccionada.
             * @param position La posición del elemento seleccionado.
             * @param id El ID de fila del elemento seleccionado.
             */
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedPartnerIndex = partnerIds.get(position);
                editDireccion.setHint(handler.getPartnerAddress(selectedPartnerIndex));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // Configurar el listener del botón
        guardarButton.setOnClickListener(new View.OnClickListener() {
            /**
             * Se llama cuando se hace clic en el botón "Guardar".  Guarda la visita en la base de datos.
             * @param v La {@link View} en la que se hizo clic.
             */
            @Override
            public void onClick(View v) {
                if (validarCampos()) {
                    ArrayList<String> columnas = new ArrayList<>();
                    ArrayList<String> datos = new ArrayList<>();

                    columnas.add("usuario_id");
                    datos.add(String.valueOf(user.getId()));

                    columnas.add("partner_id");
                    datos.add(String.valueOf(selectedPartnerIndex));

                    columnas.add("fechaVisita");
                    datos.add(fechaDato);

                    columnas.add("direccion");
                    if (editDireccion.getText().toString().isEmpty()) {
                        if (editDireccion.getHint().toString().equals("Dirección")) {
                            datos.add("");
                        } else {
                            datos.add(editDireccion.getHint().toString());
                        }
                    } else {
                        datos.add(editDireccion.getText().toString());
                    }

                    handler.insertData("visitas", columnas, datos);
                    finish();
                }
            }
        });
    }
    /**
     * Valida los campos de fecha y dirección.
     * @return {@code true} si ambos campos son válidos, {@code false} en caso contrario.
     */
    private boolean validarCampos() {
        direccion = editDireccion.getText().toString().trim();
        boolean validado = true;

        if (fechaDato.isEmpty()) {
            Toast.makeText(this, "Por favor, seleccione una fecha para la visita.",
                    Toast.LENGTH_LONG).show();
            validado = false;
        } else {
            if (!direccion.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ0-9 ]+") && !direccion.isEmpty()) {
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
