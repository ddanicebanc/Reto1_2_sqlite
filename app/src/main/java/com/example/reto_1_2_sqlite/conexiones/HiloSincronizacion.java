package com.example.reto_1_2_sqlite.conexiones;

import android.content.Context;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class HiloSincronizacion extends Thread {
    String nombre, tipo, imagen;
    int id;
    Context context;

    public HiloSincronizacion (Context context) {
        super();
        this.context = context;
    }

    @Override
    public void run () {
        String url = "jdbc:mysql://192.168.1.139:3306/db_delegaciones";

        try {
            Connection conn = DriverManager.getConnection(url, "daniroot", "dani");

            //ARTÍCULOS
            String query = "select * from articulos";
            Statement stmt = conn.createStatement();
            ResultSet result = stmt.executeQuery(query);

            DBHandler handler = new DBHandler(context, 2);
            ArrayList<String> campos = new ArrayList<>();
            ArrayList<String> datos = new ArrayList<>();

            campos.add("id");
            campos.add("nombre");
            campos.add("tipo");
            campos.add("imagen");

            while (result.next()) {
                id = result.getInt("id");
                nombre = result.getString("nombre");
                tipo = result.getString("tipo");
                imagen = result.getString("imagen");

                datos.add(String.valueOf(id));
                datos.add(nombre);
                datos.add(tipo);
                datos.add(imagen);

                handler.insertData("articulos", campos, datos);
            }

            conn.close();
        } catch (Exception e) {
            Log.d("CONEXIÓN", e.getMessage());
        }
    }
}
