package com.example.reto_1_2_sqlite.conexiones;

import static com.example.reto_1_2_sqlite.RegisterActivity.comercialIds;
import static com.example.reto_1_2_sqlite.RegisterActivity.comercialNames;
import static com.example.reto_1_2_sqlite.RegisterActivity.comercialTels;
import static com.example.reto_1_2_sqlite.RegisterActivity.delegationIds;

import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Hilo para cargar los comerciales en la base de datos local en la primera carga de la aplicación
 */
public class MysqlConnection extends Thread {
    public void run() {
        String url;
        //Vaciado de los arrays con los datos de los comerciales para evitar duplicados
        comercialIds.clear();
        comercialNames.clear();
        comercialTels.clear();
        delegationIds.clear();

        //Esta es la dirección en casa en el momento de prueba
        url = "jdbc:mysql://192.168.1.134:3306/db_delegaciones";

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
