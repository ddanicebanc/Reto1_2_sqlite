package com.example.reto_1_2_sqlite;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ConsultaPartners extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.layout_consulta_partner);

        // Configuración de las barras del sistema
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Referencia al botón y configuración del evento click
        Button validarButton = findViewById(R.id.buttonValidar);
        validarButton.setOnClickListener(v -> validarIdPartner());
    }

    private void validarIdPartner() {
        // Referencia al EditText del ID
        EditText editIdPartner = findViewById(R.id.editIdPartner);

        // Obtener el valor ingresado
        String idPartner = editIdPartner.getText().toString().trim();

        // Validaciones
        if (TextUtils.isEmpty(idPartner)) {
            Toast.makeText(this, "El campo 'ID_Partner' no puede estar vacío", Toast.LENGTH_LONG).show();
            return;
        }

        if (!idPartner.matches("\\d+")) {
            Toast.makeText(this, "El campo 'ID_Partner' solo puede contener números", Toast.LENGTH_LONG).show();
            return;
        }
    }
}

