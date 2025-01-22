package com.example.reto_1_2_sqlite;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.reto_1_2_sqlite.modelos.CabeceraPedido;
import com.example.reto_1_2_sqlite.modelos.User;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class AnadirCabeceraPedido extends AppCompatActivity implements Serializable {
    EditText edtFPedido, edtFPago, edtFEnvio;
    private Spinner spnNombrePartners;
    DBHandler handler;
    public static ArrayList<String> partnerNames = new ArrayList<>();
    public static ArrayList<String> partnerIds = new ArrayList<>();
    public int selectedPartnerIndex;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_anadir_cabecera_pedido);
        Intent startIntent = getIntent();
        Bundle extras = startIntent.getExtras();

        User user = (User) getIntent().getSerializableExtra("cUser");
        handler = new DBHandler(this);

        edtFPedido = findViewById(R.id.edtOrderDate);
        edtFPago = findViewById(R.id.edtPaymentDate);
        edtFEnvio = findViewById(R.id.edtShippingDate);

        inicializarCampos();

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

        Button btnSiguiente = findViewById(R.id.btnNext);
        btnSiguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (comprobarCampos()) {
                    int id = handler.getLatestId("cab_pedidos");
                    CabeceraPedido cabecera = new CabeceraPedido(
                            id,
                            user.getId(),
                            user.getDelegationId(),
                            edtFPedido.getText().toString(),
                            edtFEnvio.getText().toString(),
                            edtFPago.getText().toString(),
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

    private boolean comprobarCampos () {
        boolean valido = true;

        if (edtFPedido.getText().toString().isEmpty()) {
            valido = false;
        }

        if (!edtFPago.getText().toString().isEmpty()) {
            valido = false;
        }

        return valido;
    }

    private void inicializarCampos () {
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String today = LocalDate.now().format(format).toString();

        edtFPedido.setText(today);
    }
}
