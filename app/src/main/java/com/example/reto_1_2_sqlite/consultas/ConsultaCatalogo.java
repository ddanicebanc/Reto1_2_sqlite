package com.example.reto_1_2_sqlite.consultas;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.reto_1_2_sqlite.PantallaPrincipal;
import com.example.reto_1_2_sqlite.R;
import com.example.reto_1_2_sqlite.adaptadores.CatalogoAdapter;
import com.example.reto_1_2_sqlite.conexiones.DBHandler;
import com.example.reto_1_2_sqlite.modelos.Articulo;
import com.example.reto_1_2_sqlite.modelos.User;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * <h2>Clase para la pantalla del Catálogo</h2>
 * <p>
 *     La pantalla tiene los siguientes elementos:
 *     <ul>
 *         <li>Encabezado con el nombre del usuario</li>
 *         <li>Lista con las visitas del usuario actual, ordenados en orden ascendente por fecha</li>
 *         <li>Checkbox para mostrar visitas anteriores a la fecha actual</li>
 *         <li>Botón para añadir nuevas visitas</li>
 *     </ul>
 * </p>
 */
public class ConsultaCatalogo extends AppCompatActivity implements Serializable, CatalogoAdapter.ItemClickListener {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.con_catalogo);

        Intent startIntent = getIntent();
        Bundle extras = startIntent.getExtras();

        User user = (User) extras.getSerializable("cUser");
        DBHandler handler = new DBHandler(this);

        ArrayList<Articulo> articulos = handler.getArrayArticulos(user);

        //Para que el catálogo no salte más de un artículo a la vez
        PagerSnapHelper snapHelper = new PagerSnapHelper();
        RecyclerView rclCatalogo = findViewById(R.id.rclCatalogo);
        snapHelper.attachToRecyclerView(rclCatalogo);
        CatalogoAdapter adapter = new CatalogoAdapter(articulos, this);
        rclCatalogo.setAdapter(adapter);
        rclCatalogo.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL,
                false));
        adapter.setOnClickListener(this::onClick);

        //Funcionamiento de los botones del footer
        ImageButton btnHome = findViewById(R.id.btnHome);
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(
                        ConsultaCatalogo.this,
                        PantallaPrincipal.class
                );
                i.putExtra("cUser", user);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                finish();
            }
        });

        ImageButton btnCal = findViewById(R.id.btnAg);
        btnCal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(
                        ConsultaCatalogo.this,
                        ConsultaVisitas.class
                );
                i.putExtra("cUser", user);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                finish();
            }
        });

        ImageButton btnPar = findViewById(R.id.btnPar);
        btnPar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(
                        ConsultaCatalogo.this,
                        ConsultaPartners.class
                );
                i.putExtra("cUser", user);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                finish();
            }
        });

        ImageButton btnPed = findViewById(R.id.btnPed);
        btnPed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(
                        ConsultaCatalogo.this,
                        ConsultaPedidos.class
                );
                i.putExtra("cUser", user);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                finish();
            }
        });
    }

    /**
     * Método personalizado de escucha para los elementos del recyclerview
     * @param view La {@link View} en la que se hizo clic.
     * @param position La posición del elemento en el que se hizo clic.
     */
    @Override
    public void onClick(View view, int position) {}
}
