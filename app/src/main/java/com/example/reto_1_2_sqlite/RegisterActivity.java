package com.example.reto_1_2_sqlite;

import static com.example.reto_1_2_sqlite.RegisterActivity.delegationIds;
import static com.example.reto_1_2_sqlite.RegisterActivity.delegationNames;

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
    public static ArrayList<String> delegationNames = new ArrayList<>();
    public static ArrayList<String> delegationIds = new ArrayList<>();
    public int selectedDelegationIndex;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_pantalla_registro);
        MysqlConnection loadThread = new MysqlConnection();

        DBHandler dbHandler = new DBHandler(RegisterActivity.this);
        if (dbHandler.countTable("delegaciones")) {
            //Remote connections have to be executed from a different thread than the mainThread
            //Load the spinner with the information in the MYSQL server
            loadThread.start();
            try {
                loadThread.join();

                //dbHandler.insertData("delegaciones");
                //Inserción de las delegaciones en sqlite para evitar la conexión cada vez que se
                //realiza el registro
                ArrayList<String> columns = new ArrayList<>();
                ArrayList<String> data = new ArrayList<>();

                for (int i = 0; i < delegationNames.size(); i++) {
                    columns.add("id");
                    columns.add("nombre");
                    data.add(String.valueOf(delegationIds.get(i)));
                    data.add(delegationNames.get(i));

                    dbHandler.insertData("delegaciones", columns, data);

                    columns.clear();
                    data.clear();
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        } else {
            delegationNames = dbHandler.getSearchFieldArray(
                    "delegaciones", "nombre");
            delegationIds = dbHandler.getSearchFieldArray(
                    "delegaciones",
                    "id"
            );
        }

        //Continue with UI load after spinner data retrieval
        //If loadThread is dead
        if (!loadThread.isAlive()) {
            ArrayList<String> columnas = new ArrayList<>();
            columnas.add("id");
            columnas.add("nombre");
            //TODO Terminar el registro de las delegaciones en la bbdd sqlite

            Spinner cmbDelegations = findViewById(R.id.cmbDelegation);
            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                    this,
                    android.R.layout.simple_spinner_item,
                    delegationNames
            );
            cmbDelegations.setAdapter(adapter);
            cmbDelegations.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    selectedDelegationIndex = Integer.parseInt(delegationIds.get(position));
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            Button btnSave = findViewById(R.id.btn_guardar);
            btnSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String sUser;

                    edtUser = findViewById(R.id.edt_usr);

                    sUser = edtUser.getText().toString();

                    if (sUser.isEmpty()) {
                        //TODO AlertDialog.builder
                        //Normal alert dialog
                    } else {
                        //Check if the username is already introduced
                        if (dbHandler.searchByName("usuarios", "nombre", sUser)) {
                            //TODO AlertDialog.builder
                            //Normal alert dialog
                            Log.d("Prueba", "El usuario ya existe");

                            edtUser.setText("");
                        } else {
                            String sPassword;

                            edtPassword = findViewById(R.id.edt_psswd);

                            sPassword = edtPassword.getText().toString();

                            if (sPassword.isEmpty()) {
                                //TODO AlertDialog.builder
                            } else {
                                ArrayList<String> columns = new ArrayList<>();
                                ArrayList<String> data = new ArrayList<>();
                                String sSelectedDelegationIndex = String.valueOf(selectedDelegationIndex);

                                columns.add("nombre");
                                columns.add("contrasenia");
                                columns.add("delegacionId");
                                data.add(sUser);
                                data.add(sPassword);
                                data.add(sSelectedDelegationIndex);

                                dbHandler.insertData("usuarios", columns, data);
                                dbHandler.close();
                                finish();
                            }
                        }
                    }
                }
            });
        }
    }
}

class MysqlConnection extends Thread {
    public void run () {
        String url;
        delegationIds.clear();
        delegationNames.clear();

        //Esta es la dirección en casa en el momento de prueba
        url = "jdbc:mysql://localhost:3306/db_delegaciones";

        try {
            Connection conn = DriverManager.getConnection(url, "root", "root");
            Log.d("Conexión", "CONEXIÓN CORRECTA");

            String query = "select * from delegaciones;";
            Statement stmt = conn.createStatement();
            ResultSet result = stmt.executeQuery(query);

            while (result.next()) {
                delegationNames.add(result.getString("nombre"));
                delegationIds.add(result.getString("id_delegacion"));
            }

            conn.close();
        } catch (Exception e) {
            Log.e("Conexión", e.getMessage());
        }
    }
}