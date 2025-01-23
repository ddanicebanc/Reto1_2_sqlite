package com.example.reto_1_2_sqlite;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class FormulariosPedido extends AppCompatActivity {

    String nPedido;
    Date fPedido;
    Date fPago;
    String nComercial;
    Date fEnvio;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_formulario_pedidos);


        EditText pedNum = findViewById(R.id.pedidoInput);

        TextView fechaPedido = findViewById(R.id.inputFechaPedido);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String fechaActual = sdf.format(Calendar.getInstance().getTime());
        fechaPedido.setText(fechaActual);

        TextView fechaPago = findViewById(R.id.inputFechaPago);
        TextView fechaEnvio = findViewById(R.id.inputFechaEnvio);

// Validación del número de pedido
        String numPedidoStr = pedNum.getText().toString();
        if (numPedidoStr.isEmpty()) {
            pedNum.setError("El número de pedido no puede estar vacío.");
        } else {
            try {
                int numPedido = Integer.parseInt(numPedidoStr);
                if (numPedido <= 0) {
                    pedNum.setError("El número de pedido debe ser positivo.");
                }
            } catch (NumberFormatException e) {
                pedNum.setError("El número de pedido debe ser un valor numérico válido.");
            }
        }

// Validación de las fechas
        boolean fechasValidas = true;

        String fechaPedidoStr = fechaPedido.getText().toString();
        String fechaPagoStr = fechaPago.getText().toString();
        String fechaEnvioStr = fechaEnvio.getText().toString();

        try {
            Date fechaPedidoDate = sdf.parse(fechaPedidoStr);
            Date fechaPagoDate = sdf.parse(fechaPagoStr);
            Date fechaEnvioDate = sdf.parse(fechaEnvioStr);

            // Comprobamos que la fecha de pago no sea anterior a la de pedido
            if (fechaPagoDate != null && fechaPagoDate.before(fechaPedidoDate)) {
                fechaPago.setError("La fecha de pago no puede ser anterior a la fecha de pedido.");
            }

            // Comprobamos que la fecha de envío no sea anterior a la de pago
            if (fechaEnvioDate != null && fechaEnvioDate.before(fechaPagoDate)) {
                fechaEnvio.setError("La fecha de envío no puede ser anterior a la fecha de pago.");
            }

        } catch (ParseException e) {
            if (fechaPagoStr.isEmpty()) {
                fechaPago.setError("La fecha de pago debe estar en el formato (dd/mm/yyyy).");
            }
            if (fechaEnvioStr.isEmpty()) {
                fechaEnvio.setError("La fecha de envío debe estar en el formato (dd/mm/yyyy).");
            }
        }

        Button guardar= findViewById(R.id.btnguardar);
        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nPedido = pedNum.getText().toString();
                Intent myIntent =  new Intent(
                        FormulariosPedido.this,
                        LineasPedido.class

                );


                startActivity(myIntent);
            }
        });

    }
}
