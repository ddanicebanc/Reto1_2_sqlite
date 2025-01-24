package com.example.reto_1_2_sqlite;

import static com.example.reto_1_2_sqlite.RegisterActivity.comercialIds;
import static com.example.reto_1_2_sqlite.RegisterActivity.comercialNames;
import static com.example.reto_1_2_sqlite.RegisterActivity.comercialTels;
import static com.example.reto_1_2_sqlite.RegisterActivity.delegationIds;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class RegisterActivity extends AppCompatActivity {
    private EditText edtUser, edtPassword;
    public static ArrayList<Integer> delegationIds = new ArrayList<>();
    public static ArrayList<String> comercialNames = new ArrayList<>();
    public static ArrayList<Integer> comercialTels = new ArrayList<>();
    public static ArrayList<Integer> comercialIds = new ArrayList<>();
    private int idComercialSeleccionado;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_pantalla_registro);
        //DECLARACIONES
        //Conexiones
        MysqlConnection loadThread = new MysqlConnection();
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

        if (handler.countTable("comerciales")) {
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
                        columnas.add("delegacion_id");

                        datos.add(String.valueOf(comercialIds.get(i)));
                        datos.add(comercialNames.get(i));
                        datos.add(String.valueOf(comercialTels.get(i)));
                        datos.add(String.valueOf(delegationIds.get(i)));

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
                            //Rellenamos los arrayList con las columnas y datos
                            columnas.add("nombre");
                            columnas.add("contrasenia");
                            columnas.add("comercial_id");

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

                    }
                } else {

                }
            }
        });
    }
}

/**
 * Hilo para cargar los comerciales en la base de datos local
 */
class MysqlConnection extends Thread {
    public void run () {
        String url;
        //Vaciado de los arrays con los datos de los comerciales para evitar duplicados
        comercialIds.clear();
        comercialNames.clear();
        comercialTels.clear();
        delegationIds.clear();

        //Esta es la dirección en casa en el momento de prueba
        url = "jdbc:mysql://192.168.1.139:3306/db_delegaciones";

        //Conexión a la base de datos remota a través del conector jdbc
        try {
            Connection conn = DriverManager.getConnection(url, "daniroot", "dani");
            Log.d("CONEXIÓN", "CONEXIÓN CORRECTA");

            String query = "select * from comerciales;";
            Statement stmt = conn.createStatement();
            ResultSet result = stmt.executeQuery(query);

            //Accedemos a las columnas a traves de sus índices. El ResultSet está en base 1
            while (result.next()) {
                comercialIds.add(result.getInt(1));
                comercialNames.add(result.getString(2));
                comercialTels.add(result.getInt(3));
                delegationIds.add(result.getInt(4));
            }

            conn.close();
        } catch (Exception e) {
            Log.e("Conexión", e.getMessage());
        }
    }
}