package com.example.reto_1_2_sqlite.anadir;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.reto_1_2_sqlite.R;
import com.example.reto_1_2_sqlite.conexiones.DBHandler;
import com.example.reto_1_2_sqlite.consultas.ConsultaPartners;
import com.example.reto_1_2_sqlite.modelos.User;

import java.io.Serializable;
import java.util.ArrayList;

public class AniadirPartners extends AppCompatActivity implements Serializable {
    private Button saveButton;
    private EditText edtName, edtAddress, edtCity, edtPhone, edtEmail, edtZipC;
    private Spinner spnName;
    private User user;
    private DBHandler handler;
    private String partnerIndex;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.an_partners);

        handler = new DBHandler(AniadirPartners.this);

        user = (User) getIntent().getSerializableExtra("cUser");

        edtName = findViewById(R.id.edt_name);
        edtAddress = findViewById(R.id.edt_address);
        edtCity = findViewById(R.id.edt_population);
        edtZipC = findViewById(R.id.edt_zip_code);
        edtPhone = findViewById(R.id.edt_tlfn);
        edtEmail = findViewById(R.id.edt_email);

        saveButton = findViewById(R.id.btn_guardar);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!comprobarCampos()) {
                    ArrayList<String> columnas = new ArrayList<>();
                    ArrayList<String> datos = new ArrayList<>();

                    columnas.add("nombre");
                    datos.add(edtName.getText().toString());

                    columnas.add("direccion");
                    datos.add(edtAddress.getText().toString());

                    columnas.add("poblacion");
                    datos.add(edtCity.getText().toString());

                    columnas.add("telefono");
                    datos.add(edtPhone.getText().toString());

                    columnas.add("email");
                    datos.add(edtEmail.getText().toString());

                    columnas.add("usuario_id");
                    datos.add(String.valueOf(user.getId()));

                    handler.insertData("partners", columnas, datos);

                    Intent i = new Intent(
                            AniadirPartners.this,
                            ConsultaPartners.class
                    );
                    i.putExtra("cUser", user);
                    startActivity(i);
                    finish();
                }
            }
        });
    }

    private boolean comprobarCampos() {
        boolean error = false;
        String name, address, city, phone, email, zipcode;

        name = edtName.getText().toString();

        if (name.isEmpty()) {
            Toast.makeText(this,
                    "Es necesario introducir un nombre para el partner.",
                    Toast.LENGTH_SHORT).show();
            edtName.requestFocus();
            error = true;
        }

        if (handler.comprobarNombrePartner(name, user)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("ERROR")
                    .setMessage("El partner introducido ya existe.")
                    .setCancelable(false)
                    .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            edtName.setText("");
                            edtName.requestFocus();
                            dialog.cancel();
                        }
                    })
                    .show();
            error = true;
        }

        return error;
    }
}
