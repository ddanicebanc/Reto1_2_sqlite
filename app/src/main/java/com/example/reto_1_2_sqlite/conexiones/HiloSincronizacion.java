package com.example.reto_1_2_sqlite.conexiones;

import android.content.Context;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
/**
 * <h2>Clase para la sincronización de datos con el servidor de MYSQL</h2>
 * <p>
 *     El proceso descrito a continuación es homologo para todas las tablas que se tratan.
 * </p>
 * <p>
 *     Las tablas que se tratan en este hilo son las siguientes:
 *     <ul>
 *         <li>Artículos</li>
 *         <li>Delegaciones</li>
 *         <li>Catálogo</li>
 *         <li>Comerciales</li>
 *     </ul>
 * </p>
 * <p>
 *     El proceso de sincronización sigue los siguientes pasos:
 *     <ol>
 *         <li>Establece una conexión con el servidor de MYSQL</li>
 *         <li>Recupera los ids de la tabla y se guardan en un arrayList</li>
 *         <li>Se filtran los ids de la misma tabla en MYSQL quitando los de SQLite con un NOT IN</li>
 *         <li>Se recorre el ResultSet resultante y se hace el insert on la bbdd SQLite</li>
 *     </ol>
 * </p>
 * <p>El proceso está comentado sólo con la tabla de artículos.</p>
 */
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
        String url = "jdbc:mysql://192.168.21.193:3306/prueba_carga";

        try {
            Connection conn = DriverManager.getConnection(url, "daniroot", "dani");
            DBHandler handler = new DBHandler(context);

            //ARTÍCULOS
            //Recuperación de los ids de los artículos en local
            ArrayList<Integer> idsLocal = handler.getArticuloIntArray("articulos.id", -1);

            //Creación del string para la consulta en MYSQL
            String idsQuery = crearStringIds(idsLocal);

            String query = "select * from articulos";
            //Añadir el filtro en caso de que haya artículos en local
            if (idsQuery.length() > 0) {
                query = query + "\nwhere id_articulo in (" + idsQuery + ")";
            }
            query += ";";

            //Creación de los objetos para la consulta
            Statement stmt = conn.createStatement();
            ResultSet result = stmt.executeQuery(query);

            //Arraylists para los campos y datos a guardar
            ArrayList<String> campos = new ArrayList<>();
            ArrayList<String> datos = new ArrayList<>();
            //Columnas de la tabla articulos
            campos.add("id");
            campos.add("nombre");
            campos.add("tipo");
            campos.add("imagen");

            while (result.next()) {
                id = result.getInt(1);
                nombre = result.getString(2);
                tipo = result.getString(3);
                imagen = result.getString(4);

                datos.add(String.valueOf(id));
                datos.add(nombre);
                datos.add(tipo);
                datos.add(imagen);

                handler.insertData("articulos", campos, datos);

                //Limpieza de los datos a guardar para el siguiente registro
                datos.clear();
            }

            //Delegaciones
            idsLocal.clear();
            idsLocal = handler.getSearchFieldIntegerArray("delegaciones", "id_delegacion");

            idsQuery = crearStringIds(idsLocal);

            query = "select * from delegaciones";
            if (idsQuery.length() > 0) {
                query = query + "\nwhere id_delegacion in (" + idsQuery + ")";
            }
            query += ";";

            stmt = conn.createStatement();
            result = stmt.executeQuery(query);

            campos.clear();
            datos.clear();
            campos.add("id_delegacion");
            campos.add("nombre");

            while (result.next()) {
                id = result.getInt(1);
                nombre = result.getString(2);

                datos.add(String.valueOf(id));
                datos.add(nombre);

                handler.insertData("delegaciones", campos, datos);

                datos.clear();
            }

            //Catálogo
            query = "select * from catalogo;";
            stmt = conn.createStatement();
            result = stmt.executeQuery(query);
            Boolean encontrado = false;

            handler.getCatalogo(result);

            //COMERCIALES
            query = "select * from comerciales";
            stmt = conn.createStatement();
            result = stmt.executeQuery(query);

            datos.clear();
            campos.clear();
            campos.add("id");
            campos.add("nombre");
            campos.add("telefono");
            campos.add("email");
            campos.add("delegacion_id");

            while (result.next()) {
                datos.add(String.valueOf(result.getInt("id_comercial")));
                datos.add(result.getString("nombre"));
                datos.add(String.valueOf(result.getInt("telefono")));
                datos.add(result.getString("email"));
                datos.add(String.valueOf(result.getInt("id_delegacion")));

                handler.insertData("comerciales", campos, datos);

                datos.clear();
            }

            conn.close();
        } catch (Exception e) {
            Log.d("CONEXIÓN", e.getMessage());
        }
    }

    public String crearStringIds (ArrayList<Integer> idsLocal) {
        //Creación del string para la consulta en MYSQL
        String idsQuery = "";

        for (int id : idsLocal) {
            idsQuery += id + ", ";
        }
        //Eliminación de la última coma en el string
        if (idsQuery.length() > 0) {
            idsQuery = idsQuery.substring(0, idsQuery.length()-1);
        }

        return idsQuery;
    }
}
