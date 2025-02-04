package com.example.reto_1_2_sqlite.conexiones;

import android.content.Context;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class CargaDelegaciones extends Thread {
    private final Context context;

    public CargaDelegaciones (Context context) {
        super();
        this.context = context;
    }

    @Override
    public void run () {
        //Esta es la dirección en casa en el momento de prueba
        String url = "jdbc:mysql://192.168.21.193:3306/prueba_carga";

        //Conexión a la base de datos remota a través del conector jdbc
        try {
            Connection conn = DriverManager.getConnection(url, "daniroot", "dani");
            Log.d("CONEXIÓN", "CONEXIÓN CORRECTA");

            String query = "select * from delegaciones;";
            Statement stmt = conn.createStatement();
            ResultSet result = stmt.executeQuery(query);

            DBHandler handler = new DBHandler(context);
            ArrayList<String> columnas = new ArrayList<>();
            ArrayList<String> datos = new ArrayList<>();
            columnas.add("id_delegacion");
            columnas.add("nombre");


            //Accedemos a las columnas a traves de sus índices. El ResultSet está en base 1
            while (result.next()) {
                datos.add(String.valueOf(result.getInt(1)));
                datos.add(result.getString(2));

                handler.insertData("delegaciones", columnas, datos);

                datos.clear();
            }

            conn.close();
            handler.close();
        } catch (Exception e) {
            Log.e("Conexión", e.getMessage());
        }
    }
}
