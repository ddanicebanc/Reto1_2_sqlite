package com.example.reto_1_2_sqlite.anadir;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.reto_1_2_sqlite.conexiones.DBHandler;
import com.example.reto_1_2_sqlite.R;
import com.example.reto_1_2_sqlite.modelos.CabeceraPedido;
import com.example.reto_1_2_sqlite.modelos.User;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * {@code AnadirCabeceraPedido} es una {@link AppCompatActivity} que permite al usuario añadir una nueva
 * cabecera de pedido.  La actividad permite seleccionar las fechas de pedido, pago y envío, así como
 * el socio asociado al pedido.
 */
public class AnadirCabeceraPedido extends AppCompatActivity implements Serializable {
    EditText edtFPedido, edtFPago, edtFEnvio;
    private Spinner spnNombrePartners;
    DBHandler handler;
    public static ArrayList<String> partnerNames = new ArrayList<>();
    public static ArrayList<Integer> partnerIds = new ArrayList<>();
    private int selectedPartnerIndex;
    private String sFechaPedido, sFechaPago, sFechaEnvio;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.an_cabecera_pedido);
        Intent startIntent = getIntent();
        Bundle extras = startIntent.getExtras();

        User user = (User) getIntent().getSerializableExtra("cUser");
        handler = new DBHandler(this);

        edtFPedido = findViewById(R.id.edtOrderDate);
        edtFPago = findViewById(R.id.edtPaymentDate);
        edtFEnvio = findViewById(R.id.edtShippingDate);

        inicializarCampos();

        //Establecimiento de las escucha para los campos de las fechas
        //Los campos de las fechas muestran una vista de DatePickerDialog para seleccionar una fecha
        //desde un calendario.
        edtFPago.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();

                int anio = c.get(Calendar.YEAR);
                int mes = c.get(Calendar.MONTH);
                int dia = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        AnadirCabeceraPedido.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // on below line we are setting date to our edit text.
                                edtFPago.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                                sFechaPago = "";
                                sFechaPago = sFechaPago + year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                                Log.d("Fecha Pago", sFechaPago);
                            }
                        },
                        // on below line we are passing year,
                        // month and day for selected date in our date picker.
                        anio, mes, dia);
                //Cambiamos el primer día de la semana para el cuadro de diálogo
                datePickerDialog.getDatePicker().setFirstDayOfWeek(Calendar.MONDAY);

                datePickerDialog.show();
            }
        });

        edtFPedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();

                int anio = c.get(Calendar.YEAR);
                int mes = c.get(Calendar.MONTH);
                int dia = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        AnadirCabeceraPedido.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // on below line we are setting date to our edit text.
                                edtFPedido.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                                sFechaPedido = "";
                                sFechaPedido = sFechaPedido + year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                            }
                        },
                        anio, mes, dia);
                //Cambiamos el primer día de la semana para el cuadro de diálogo
                datePickerDialog.getDatePicker().setFirstDayOfWeek(Calendar.MONDAY);

                datePickerDialog.show();
            }
        });

        edtFEnvio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();

                int anio = c.get(Calendar.YEAR);
                int mes = c.get(Calendar.MONTH);
                int dia = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        AnadirCabeceraPedido.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // on below line we are setting date to our edit text.
                                edtFEnvio.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                                sFechaEnvio = "";
                                sFechaEnvio = sFechaEnvio + year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                            }
                        },
                        anio, mes, dia);
                //Cambiamos el primer día de la semana para el cuadro de diálogo
                datePickerDialog.getDatePicker().setFirstDayOfWeek(Calendar.MONDAY);

                datePickerDialog.show();
            }
        });

        //Preparación de los datos para el spinner
        partnerNames = handler.getNombrePartners(user);
        partnerIds = handler.getIdPartners(user);

        //Asignamos el adaptador del spinner con el array de nombres de partners
        spnNombrePartners = findViewById(R.id.spnNombrePartner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                partnerNames
        );
        spnNombrePartners.setAdapter(adapter);
        //Cambiamos el id del partner seleccionado cada vez que elegimos un elemento del spinner
        spnNombrePartners.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedPartnerIndex = partnerIds.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Button btnSiguiente = findViewById(R.id.btnNext);
        btnSiguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (comprobarCampos()) {
                    int id = handler.getLatestId("cab_pedidos") + 1;
                    CabeceraPedido cabecera = new CabeceraPedido(
                            id,
                            user.getId(),
                            user.getDelegationId(),
                            sFechaPedido,
                            sFechaEnvio,
                            sFechaPago,
                            selectedPartnerIndex
                    );

                    Intent intent = new Intent(
                            AnadirCabeceraPedido.this,
                            AnadirLineasPedido.class
                    );
                    intent.putExtra("cUser", user);
                    intent.putExtra("cabecera", cabecera);
                    startActivity(intent);
                }
            }
        });
    }
    /**
     * Comprueba si los campos obligatorios han sido rellenados correctamente.
     * @return {@code true} si todos los campos son válidos, {@code false} en caso contrario.
     */
    private boolean comprobarCampos () {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        boolean valido = true;

        if (sFechaPedido == null) {
            sFechaPedido = "";
        }
        if (sFechaPago == null) {
            sFechaPago = "";
        }
        if (sFechaEnvio == null) {
            sFechaEnvio = "";
        }

        if (edtFPedido.getText().toString().isEmpty()) {
            valido = false;
        }

        if (edtFPago.getText().toString().isEmpty()) {
            valido = false;
        }

        try {
            if (sdf.parse(sFechaPedido).after(sdf.parse(sFechaPago))) {
                Toast.makeText(this,
                        "La fecha de pago no puede ser anterior a la fecha del pedido.",
                        Toast.LENGTH_LONG).show();
                valido = false;
            }
        } catch (ParseException pe) {
            Log.d ("Date pase exception", pe.getMessage());
        }

        return valido;
    }

    /**
     * Método para inicializar los distintos campos de la actividad. Cada elemento se inicializa
     * con el valor por defecto para cada tipo de dato:
     * <ul>
     *     <li>String: ""</li>
     *     <li>Númerico: 0</li>
     *     <li>Fecha: Fecha del día actual</li>
     * </ul>
     */
    private void inicializarCampos () {
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String today = LocalDate.now().format(format).toString();
        format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        sFechaPedido = LocalDate.now().format(format).toString();

        edtFPedido.setText(today);
    }
}
