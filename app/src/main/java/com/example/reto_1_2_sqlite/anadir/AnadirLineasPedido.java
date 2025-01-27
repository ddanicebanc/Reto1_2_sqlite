package com.example.reto_1_2_sqlite.anadir;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
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
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.reto_1_2_sqlite.conexiones.DBHandler;
import com.example.reto_1_2_sqlite.R;
import com.example.reto_1_2_sqlite.adaptadores.LineasAdapter;
import com.example.reto_1_2_sqlite.consultas.ConsultaPedidos;
import com.example.reto_1_2_sqlite.modelos.CabeceraPedido;
import com.example.reto_1_2_sqlite.modelos.LineaPedido;
import com.example.reto_1_2_sqlite.modelos.User;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.MissingResourceException;

public class AnadirLineasPedido extends AppCompatActivity implements Serializable {
    private EditText edtCantidad, edtPrecio;
    private int selectedProductIndex, numeroLinea;
    private String nombreArticuloSeleccionado;
    private Bitmap imagenArticulo;
//    private boolean aceptarSinLineas = false;
    private DBHandler handler;
    private CabeceraPedido cabecera;
    User user;
    private static ArrayList<String> nombreArticulos = new ArrayList<>();
    private static ArrayList<Integer> idArticulos = new ArrayList<>();
    private static ArrayList<LineaPedido> lineas = new ArrayList<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.an_lineas_pedido);

        user = (User) getIntent().getSerializableExtra("cUser");
        //Recuperamos el Intent inicial para diferenciarlo en cada carga al sacar los extras
        Intent startIntent = getIntent();
        Bundle extras = startIntent.getExtras();
        cabecera = (CabeceraPedido) extras.getSerializable("cabecera");
        handler = new DBHandler(this);


        //Inicializamos el número de línea con el siguiente id en la tabla, sólo si no se encuentra
        // el extra "numeroLinea"
        try {
            numeroLinea = extras.getInt("numeroLinea");
        } catch (MissingResourceException mre) {
            numeroLinea = handler.getLatestId("lin_pedidos") + 1;
        }

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
        nombreArticulos = handler.getArticuloStringArray("nombre", user.getDelegationId());
        idArticulos = handler.getArticuloIntArray("id", user.getDelegationId());
        ArrayAdapter<String> adapterArticulos = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                nombreArticulos
        );
        spnArticulos.setAdapter(adapterArticulos);
        spnArticulos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedProductIndex = idArticulos.get(position);
                nombreArticuloSeleccionado = nombreArticulos.get(position);
                imagenArticulo = handler.getImagenArticulo(selectedProductIndex);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //Configuración del recyclerView
        //Comprobación de que el arrayList no es nulo
        if (lineas != null) {
            RecyclerView rclLineas = findViewById(R.id.rclLineas);

            //Crear el adaptador y el LayoutManager para mostrar la información en el rcl
            LineasAdapter adapterLineas = new LineasAdapter(lineas, this);
            rclLineas.setAdapter(adapterLineas);
            rclLineas.setLayoutManager(new LinearLayoutManager(this));
        }

        //Configuración para el botón de añadir nuevas líneas
        Button btnNuevaLinea = findViewById(R.id.btnInsertLine);
        btnNuevaLinea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validarCampos()) {
                    if (lineas == null) {
                        lineas = new ArrayList<>();
                    }

                    numeroLinea++;

                    lineas.add(new LineaPedido(
                            selectedProductIndex,
                            Integer.parseInt(edtCantidad.getText().toString()),
                            Float.parseFloat(edtPrecio.getText().toString()),
                            cabecera.getId(),
                            nombreArticuloSeleccionado,
                            numeroLinea,
                            imagenArticulo
                    ));

                    //Refresco de la actividad para ver los cambios reflejados en la lista
                    startIntent.putExtra("lineas", lineas);
                    startIntent.putExtra("numeroLinea", numeroLinea);
                    startIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

                    //Vaciamos los campos de cantidad y precio
                    edtCantidad.setText("");
                    edtPrecio.setText("");

                    recreate();
                }
            }
        });

        //Configuración para el botón de finalizar el pedido
        Button finalizarPedido = findViewById(R.id.btnEndOrder);
        finalizarPedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Comprobación del número de lineas introducidas en el pedido
                //La comprobación del arraylist nulo tiene que ir la primera para que no salte la excepción
                if (lineas == null || lineas.size() == 0) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(AnadirLineasPedido.this);
                    builder.setMessage("No has introducido ningún articulo al pedido. \n" +
                            "¿Estás seguro que deseas continuar?")
                            .setTitle("¡ATENCIÓN!")
                            .setCancelable(false)
                            .setNegativeButton("Cancelar",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    })
                            .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //Inicializamos el arraylist de las lineas para que el proceso
                                    //de inserción de lineas en la bbdd no de nullPointerException
                                    if (lineas == null) {
                                        lineas = new ArrayList<>();
                                    }

                                    insertarPedido();
                                }
                            });
                    builder.show();
                } else {
                    insertarPedido();
                }
            }
        });
    }

    private void insertarPedido () {
        //Insertamos la información en la base de datos
        ArrayList<String> columnas = new ArrayList<>();
        ArrayList<String> datos = new ArrayList<>();

        //Cabecera del pedido
        columnas.add("id");
        columnas.add("fechaPedido");
        columnas.add("fechaPago");
        columnas.add("fechaEnvio");
        columnas.add("usuario_id");
        columnas.add("delegacion_id");
        columnas.add("partner_id");

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

        columnas.add("numeroLinea");
        columnas.add("articulo_id");
        columnas.add("cantidad");
        columnas.add("precio");
        columnas.add("cab_pedido_id");

        for (LineaPedido l : lineas) {
            datos.add(String.valueOf(l.getNumeroLinea()));
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
