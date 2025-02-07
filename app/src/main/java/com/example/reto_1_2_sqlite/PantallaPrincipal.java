package com.example.reto_1_2_sqlite;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.reto_1_2_sqlite.conexiones.HiloCarga;
import com.example.reto_1_2_sqlite.consultas.ConsultaCatalogo;
import com.example.reto_1_2_sqlite.consultas.ConsultaPartners;
import com.example.reto_1_2_sqlite.consultas.ConsultaPedidos;
import com.example.reto_1_2_sqlite.consultas.ConsultaVisitas;
import com.example.reto_1_2_sqlite.modelos.User;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import java.io.Serializable;
import java.util.ArrayList;

public class PantallaPrincipal extends AppCompatActivity implements Serializable {
    private final int REQUEST_PERMISSIONS_REQUEST_CODE = 1;
    private ImageButton btnCalendar, btnPartners, btnPedidos, btnCatalogo;
    private User user;
    private MapView map = null;

    TextView info;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Context ctx = getApplicationContext();

        user = (User) getIntent().getSerializableExtra("cUser");

        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));

        setContentView(R.layout.layout_pantalla_principal);

        //Configuración de la actionbar
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        //Configuración del mapview
        map = findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.MAPNIK);

        //Controlador para el mapview
        IMapController controller = map.getController();
        controller.setZoom(15d);
        //Punto de incio para el mapa con un GeoPoint
        GeoPoint inicio = new GeoPoint(43.3048085436558, -2.01689600060729);
        //Establecemos el punto de incio en el controlador del mapa
        controller.setCenter(inicio);

        //Añadimos un marcador al mapa para que sea más visible
        Marker marcadorInicio = new Marker(map);
        //Establecemos la posición del marcador con las corrdenadas del principio
        marcadorInicio.setPosition(inicio);
        marcadorInicio.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER);
        //Aplicamos el marcador al mapa
        map.getOverlays().add(marcadorInicio);

        btnCalendar = findViewById(R.id.btn_calendar);
        btnCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(
                        PantallaPrincipal.this,
                        ConsultaVisitas.class
                );
                myIntent.putExtra("cUser", user);
                startActivity(myIntent);
            }
        });

        btnPartners = findViewById(R.id.btn_partners);
        btnPartners.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(
                        PantallaPrincipal.this,
                        ConsultaPartners.class
                );
                myIntent.putExtra("cUser", user);
                startActivity(myIntent);
            }
        });

        btnPedidos = findViewById(R.id.btnPedidos);
        btnPedidos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(
                        PantallaPrincipal.this,
                        ConsultaPedidos.class
                );
                myIntent.putExtra("cUser", user);
                startActivity(myIntent);
            }
        });

        btnCatalogo = findViewById(R.id.btnCatalogo);
        btnCatalogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(
                        PantallaPrincipal.this,
                        ConsultaCatalogo.class
                );
                myIntent.putExtra("cUser", user);
                startActivity(myIntent);
            }
        });

        //Configuración del footer
        ImageButton btnCal = findViewById(R.id.btnAg);
        btnCal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(
                        PantallaPrincipal.this,
                        ConsultaVisitas.class
                );
                i.putExtra("cUser", user);
                startActivity(i);
            }
        });

        ImageButton btnPart = findViewById(R.id.btnPar);
        btnPart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(
                        PantallaPrincipal.this,
                        ConsultaPartners.class
                );
                i.putExtra("cUser", user);
                startActivity(i);
            }
        });

        ImageButton btnPed = findViewById(R.id.btnPed);
        btnPed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(
                        PantallaPrincipal.this,
                        ConsultaPedidos.class
                );
                i.putExtra("cUser", user);
                startActivity(i);
            }
        });

        ImageButton btnCat = findViewById(R.id.btnCat);
        btnCat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(
                        PantallaPrincipal.this,
                        ConsultaCatalogo.class
                );
                i.putExtra("cUser", user);
                startActivity(i);
            }
        });

        info = findViewById(R.id.info);
    }

    @Override
    public void onResume() {
        super.onResume();
        //this will refresh the osmdroid configuration on resuming.
        //if you make changes to the configuration, use
        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this));
        map.onResume(); //needed for compass, my location overlays, v6.0.0 and up
    }

    @Override
    public void onPause() {
        super.onPause();
        //this will refresh the osmdroid configuration on resuming.
        //if you make changes to the configuration, use
        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //Configuration.getInstance().save(this, prefs);
        map.onPause();  //needed for compass, my location overlays, v6.0.0 and up
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        ArrayList<String> permissionsToRequest = new ArrayList<>();
        for (int i = 0; i < grantResults.length; i++) {
            permissionsToRequest.add(permissions[i]);
        }
        if (permissionsToRequest.size() > 0) {
            ActivityCompat.requestPermissions(
                    this,
                    permissionsToRequest.toArray(new String[0]),
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }

    private void requestPermissionsIfNecessary(String[] permissions) {
        ArrayList<String> permissionsToRequest = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                // Permission is not granted
                permissionsToRequest.add(permission);
            }
        }
        if (permissionsToRequest.size() > 0) {
            ActivityCompat.requestPermissions(
                    this,
                    permissionsToRequest.toArray(new String[0]),
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sincronizar:

                return true;
                case R.id.cargar:
                    HiloCarga hilo = new HiloCarga(PantallaPrincipal.this, user);
                    hilo.start();
                    try {
                        hilo.join();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
