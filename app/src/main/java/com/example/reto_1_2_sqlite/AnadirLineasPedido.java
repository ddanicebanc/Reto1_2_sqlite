package com.example.reto_1_2_sqlite;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.reto_1_2_sqlite.modelos.CabeceraPedido;
import com.example.reto_1_2_sqlite.modelos.LineaPedido;
import com.example.reto_1_2_sqlite.modelos.User;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.MissingResourceException;

public class AnadirLineasPedido extends AppCompatActivity implements Serializable {
    private EditText edtCantidad, edtPrecio;
    private int selectedProductIndex;
    private static ArrayList<String> nombreArticulos = new ArrayList<>();
    private static ArrayList<String> idArticulos = new ArrayList<>();
    private static ArrayList<LineaPedido> lineas = new ArrayList<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_anadir_lineas_pedido);
        Intent startIntent = getIntent();
        Bundle extras = startIntent.getExtras();

        User user = (User) getIntent().getSerializableExtra("cUser");
        CabeceraPedido cabecera = (CabeceraPedido) getIntent().getSerializableExtra("cabecera");
        DBHandler handler = new DBHandler(this);

        if (extras != null) {
            try {
                lineas = (ArrayList<LineaPedido>) extras.getSerializable("lineas");
            } catch (MissingResourceException mre) {
                Log.d("Extras", "No se encuentra el extra lineas.");
            }
        }

        edtCantidad = findViewById(R.id.edtLineQuantity);
        edtPrecio = findViewById(R.id.edtLinePrice);

        Spinner spnArticulos = findViewById(R.id.spnNombreArticulo);
        nombreArticulos = handler.getSearchFieldArray("articulos", "nombre");
        idArticulos = handler.getSearchFieldArray("articulos", "id");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                nombreArticulos
        );
        spnArticulos.setAdapter(adapter);
        spnArticulos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedProductIndex = Integer.parseInt(idArticulos.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Button btnNuevaLinea = findViewById(R.id.btnInsertLine);
        btnNuevaLinea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validarCampos()) {
                    lineas.add(new LineaPedido(
                            selectedProductIndex,
                            Integer.parseInt(edtCantidad.getText().toString()),
                            Integer.parseInt(edtPrecio.getText().toString()),
                            cabecera.getId()
                    ));

                    //Refresco de la actividad para ver los cambios reflejados en la lista
                    startIntent.putExtra("lineas", lineas);
                    startIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    recreate();
                }
            }
        });
    }

    private boolean validarCampos() {
        boolean valido = true;

        if (edtCantidad.getText().toString().isEmpty()) {
            Toast.makeText(this,
                    "La cantidad no puede estar vacía.",
                    Toast.LENGTH_SHORT);
            valido = false;
        }

        if (edtPrecio.getText().toString().isEmpty()) {
            Toast.makeText(this,
                    "El precio no puede estar vacío.",
                    Toast.LENGTH_SHORT);
            valido = false;
        }

        return valido;
    }
}
