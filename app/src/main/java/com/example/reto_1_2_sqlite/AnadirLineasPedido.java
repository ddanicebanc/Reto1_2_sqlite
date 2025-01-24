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
    private static ArrayList<Integer> idArticulos = new ArrayList<>();
    private static ArrayList<LineaPedido> lineas = new ArrayList<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_anadir_lineas_pedido);

        User user = (User) getIntent().getSerializableExtra("cUser");
        //Recuperamos el Intent inicial para diferenciarlo en cada carga al sacar los extras
        Intent startIntent = getIntent();
        Bundle extras = startIntent.getExtras();
        CabeceraPedido cabecera = (CabeceraPedido) extras.getSerializable("cabecera");
        DBHandler handler = new DBHandler(this);

        /*
        Cuando se añade una linea y vuelve a cargar la pantalla, tendremos un extra con el
        arrayList con las lineas del pedido. Controlamos la excepción para que no se cierre en la
        primera carga
        */
        try {
            lineas = (ArrayList<LineaPedido>) extras.getSerializable("lineas");
        } catch (MissingResourceException mre) {
            Log.d("Extras", "No se encuentra el extra lineas.");
        }

        edtCantidad = findViewById(R.id.edtLineQuantity);
        edtPrecio = findViewById(R.id.edtLinePrice);

        //Configuración para el spinner de los artículos
        Spinner spnArticulos = findViewById(R.id.spnNombreArticulo);

        //Carga de los articulos pertenecientes a la delegación del usuario (la del comercial)
        //TODO Hay que cambiar la base de datos para reflejar la tabla de catálogo
        nombreArticulos = handler.getArticuloStringData("nombre", user.getDelegationId());
        idArticulos = handler.getArticuloIntData("id", user.getDelegationId());
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                nombreArticulos
        );
        spnArticulos.setAdapter(adapter);
        spnArticulos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedProductIndex = idArticulos.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //Configuración para el botón de añadir nuevas líneas
        Button btnNuevaLinea = findViewById(R.id.btnInsertLine);
        btnNuevaLinea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validarCampos()) {
                    if (lineas == null) {
                        lineas = new ArrayList<>();
                    }
                    lineas.add(new LineaPedido(
                            selectedProductIndex,
                            Integer.parseInt(edtCantidad.getText().toString()),
                            Float.parseFloat(edtPrecio.getText().toString()),
                            cabecera.getId()
                    ));

                    //Refresco de la actividad para ver los cambios reflejados en la lista
                    startIntent.putExtra("lineas", lineas);
                    startIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    recreate();
                }
            }
        });

        //Configuración para el botón de finalizar el pedido
        Button finalizarPedido = findViewById(R.id.btnEndOrder);
        finalizarPedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO Hacer el insert de los datos en las tablas de cabecera y lineas
                //Insertamos la información en la base de datos
                ArrayList<String> columnas = new ArrayList<>();
                ArrayList<String> datos = new ArrayList<>();

                //Cabecera del pedido
                columnas.add("id");
                columnas.add("fechaPedido");
                columnas.add("fechaPago");
                columnas.add("fechaEnvio");
                columnas.add("usuarioId");
                columnas.add("delegacionId");
                columnas.add("partnerId");

                datos.add(String.valueOf(cabecera.getId()));
                datos.add(cabecera.getFechaPedido());
                datos.add(cabecera.getFechaPago());
                datos.add(cabecera.getFechaEnvio());
                datos.add(String.valueOf(cabecera.getUsuarioId()));
                datos.add(String.valueOf(cabecera.getDelegacionId()));
                datos.add(String.valueOf(cabecera.getPartnerId()));

                handler.insertData("cab_pedidos", columnas, datos);

                //Lineas de los pedidos
                columnas.clear();
                datos.clear();

                columnas.add("id");
                columnas.add("articuloId");
                columnas.add("cantidad");
                columnas.add("precio");
                columnas.add("cab_pedido_id");

                for (LineaPedido l : lineas) {
                    datos.add(String.valueOf(l.getId()));
                    datos.add(String.valueOf(l.getArticuloId()));
                    datos.add(String.valueOf(l.getCantidad()));
                    datos.add(String.valueOf(l.getPrecio()));
                    datos.add(String.valueOf(l.getCabPedidoId()));

                    handler.insertData("lin_pedidos", columnas, datos);

                    datos.clear();
                }

                //Al terminar lanzamos la actividad de la consulta de pedidos
                Intent myIntent = new Intent(
                        AnadirLineasPedido.this,
                        ConsultaPedidos.class
                );
                myIntent.putExtra("cUser", user);
                //Elimina las actividades de cabecera y lineas de pedido de la cola de actividades
                myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(myIntent);
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
