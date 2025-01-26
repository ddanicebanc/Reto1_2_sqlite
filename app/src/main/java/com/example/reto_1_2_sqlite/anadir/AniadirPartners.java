package com.example.reto_1_2_sqlite.anadir;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.reto_1_2_sqlite.DBHandler;
import com.example.reto_1_2_sqlite.R;
import com.example.reto_1_2_sqlite.modelos.User;

import java.io.Serializable;
import java.util.ArrayList;

public class AniadirPartners extends AppCompatActivity implements Serializable {
    Button saveButton;
    EditText edtName, edtAddress, edtCity, edtPhone, edtEmail, edtZipC;
    Spinner spnName;
    DBHandler handler;
    String partnerIndex;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.an_partners);

        handler = new DBHandler(AniadirPartners.this);

        User user = (User) getIntent().getSerializableExtra("cUser");

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

                    columnas.add("usuarioId");
                    datos.add(String.valueOf(user.getId()));

                    handler.insertData("partners", columnas, datos);
                    finish();
                } else {
                    //TODO En caso de que no pase la validación
                }
            }
        });
    }

    private boolean comprobarCampos() {
        boolean error = false;
        String name, address, city, phone, email, zipcode;

        name = edtName.getText().toString();
        address = edtAddress.getText().toString();
        city = edtCity.getText().toString();
        phone = edtPhone.getText().toString();
        email = edtEmail.getText().toString();
        zipcode = edtEmail.getText().toString();

        if (address.isEmpty() || city.isEmpty() || phone.isEmpty() || email.isEmpty() || zipcode.isEmpty()) {
            error = true;
        }

        if (handler.checkMatchingStringField("partners", "nombre", name)) {
            //TODO AlertDialog con "El partner que quieres introducir ya existe
            error = true;
        }

        return error;
    }
}
