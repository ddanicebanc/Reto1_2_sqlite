package com.example.reto_1_2_sqlite;

import android.content.DialogInterface;
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

import com.example.reto_1_2_sqlite.conexiones.CargaComerciales;
import com.example.reto_1_2_sqlite.conexiones.DBHandler;
import com.example.reto_1_2_sqlite.conexiones.HiloSincronizacion;

import java.util.ArrayList;

/**
 * <h2>Clase para registrar nuevos usuarios</h2>
 * <p>
 *    Contiene los campos de:
 *    <ul>
 *        <li>Nombre de usuario</li>
 *        <li>Contraseña</li>
 *        <li>Partner</li>
 *    </ul>
 * </p>
 * <p>Los partners se cargan en un spinner desde la bbdd local o desde el servidor de la delegación de MYSQL si no existen datos.</p>
 * <p>
 *     Realiza las comprobaciones para los campos usuario y contraseña comprobando que:
 *      <ol>
 *          <li>No estén vacías</li>
 *          <li>El usuario no coincida con alguno existente</li>
 *          <li>La contraseña cumpla los requisitos establecidos</li>
 *      </ol>
 * </p>
 */
public class RegisterActivity extends AppCompatActivity {
    private EditText edtUser, edtPassword;
    public static ArrayList<Integer> delegationIds = new ArrayList<>();
    public static ArrayList<String> comercialNames = new ArrayList<>();
    public static ArrayList<Integer> comercialTels = new ArrayList<>();
    public static ArrayList<Integer> comercialIds = new ArrayList<>();
    public static ArrayList<String> comercialEmails = new ArrayList<>();
    private int idComercialSeleccionado;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_pantalla_registro);
        //DECLARACIONES
        //Conexiones
        CargaComerciales loadThread = new CargaComerciales();
        DBHandler handler = new DBHandler(RegisterActivity.this);

        //ArrayLists para los insert
        ArrayList<String> columnas = new ArrayList<>();
        ArrayList<String> datos = new ArrayList<>();

        //Elementos del layout
        Spinner cmbComerciales;
        Button btnGuardar;

        //Adaptadores
        ArrayAdapter<String> adapter;

        //ASIGNACIONES
        cmbComerciales = findViewById(R.id.cmbComercial);
        btnGuardar = findViewById(R.id.btn_guardar);
        edtUser = findViewById(R.id.edt_usr);
        edtPassword = findViewById(R.id.edt_psswd);

        if (handler.isEmpty("delegaciones")) {
            HiloSincronizacion hiloDel = new HiloSincronizacion(RegisterActivity.this);

            try {
                hiloDel.start();
                hiloDel.join();
            } catch (InterruptedException ie) {

            }
        }

        if (handler.isEmpty("comerciales")) {
            //Lanzamiento de un hilo diferente al MainThread, las conexiones remotas no pueden estar
            //en el hilo principal
            loadThread.start();

            try {
                //Espera a la carga de datos en los arrays
                loadThread.join();

                //Comprobamos que no se haya perdido ningún dato
                if (comercialIds.size() == comercialNames.size() &&
                        comercialTels.size() == delegationIds.size()) {
                    //Recorremos los datos recibidos de la base de datos remota para insertarlos
                    // en local
                    for (int i = 0; i < comercialIds.size(); i++) {
                        //Creamos las columnas e introducimos los datos en el ArrayList de datos
                        columnas.add("id");
                        columnas.add("nombre");
                        columnas.add("telefono");
                        columnas.add("email");
                        columnas.add("delegacion_id");

                        datos.add(String.valueOf(comercialIds.get(i))); //id_comercial
                        datos.add(comercialNames.get(i)); //nombre
                        datos.add(String.valueOf(comercialTels.get(i))); //teléfono
                        datos.add(comercialEmails.get(i)); //email
                        datos.add(String.valueOf(delegationIds.get(i))); //delegacion_id

                        //Insertamos la información un registro a la vez
                        handler.insertData("comerciales", columnas, datos);

                        //Limpiamos los datos de los ArrayList para insertar el siguiente registro
                        columnas.clear();
                        datos.clear();
                    }
                }
            } catch (InterruptedException ie) {
                Log.e ("CONEXIÓN", ie.getMessage());
            }
        } else {
            //Si la tabla comerciales tiene datos, cargamos los datos directamente desde local
            comercialIds = handler.getComercialIntData(
                    "id");
            comercialNames = handler.getComercialStringData(
                    "nombre");
            comercialTels = handler.getComercialIntData(
                    "telefono");
            delegationIds = handler.getComercialIntData(
                    "delegacion_id");
        }

        //Continuamos con el proceso de carga de la actividad
        //Configuración del combobox/spinner del los comerciales para el registro
        adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                comercialNames
        );
        cmbComerciales.setAdapter(adapter);
        //Escucha para el clic en el spinner
        cmbComerciales.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Cuando seleccionemos un elemento del spinner guardamos el id del comercial
                idComercialSeleccionado = comercialIds.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //Configuración del botón de registro de usuarios
        //Escucha para el clic del botón
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Variables locales para la función de escucha del clic
                String usuario, contrasenia;

                usuario = edtUser.getText().toString();
                contrasenia = edtPassword.getText().toString();

                //Seguimos con el proceso si el campo del nombre de usuario tiene contenido
                if (!usuario.isEmpty()) {
                    //Mismo proceso con el campo de la contraseña
                    if (!contrasenia.isEmpty()) {
                        //Comprobamos que el usuario no esté repetido
                        if (!handler.searchByName("usuarios", "nombre", usuario)) {
                            if (comercialNames.size() == 0) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                builder.setMessage("No existen comerciales, no se puede realizar el registro." +
                                                "\nPor favor, contacta con el administrador.")
                                        .setTitle("ERROR")
                                        .setCancelable(false)
                                        .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.cancel();
                                            }
                                        });
                                builder.show();
                            } else {
                                //Rellenamos los arrayList con las columnas y datos
                                columnas.add("id");
                                columnas.add("nombre");
                                columnas.add("contrasenia");
                                columnas.add("comercial_id");

                                datos.add(String.valueOf(idComercialSeleccionado));
                                datos.add(usuario);
                                datos.add(contrasenia);
                                datos.add(String.valueOf(idComercialSeleccionado));

                                handler.insertData("usuarios", columnas, datos);
                                //Cerramos la conexión antes de terminar la actividad
                                handler.close();

                                //Terminamos la actividad
                                finish();
                            }
                        } else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                            builder.setMessage("El nombre de usuario introducido ya existe, selecciona otro por favor.")
                                    .setTitle("ERROR")
                                    .setCancelable(false)
                                    .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            edtUser.setText("");
                                            edtUser.requestFocus();
                                            dialog.cancel();
                                        }
                                    });
                            builder.show();
                        }
                    } else {
                        Toast.makeText(
                                RegisterActivity.this,
                                "Por favor, introduce una contraseña.",
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                } else {
                    Toast.makeText(
                            RegisterActivity.this,
                            "Por favor, introduce un nombre de usuario.",
                            Toast.LENGTH_SHORT
                    ).show();
                }
            }
        });
    }
}

