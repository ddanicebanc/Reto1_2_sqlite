package com.example.reto_1_2_sqlite.conexiones;

import android.content.Context;
import android.util.Log;

import com.example.reto_1_2_sqlite.modelos.CabeceraPedido;
import com.example.reto_1_2_sqlite.modelos.LinPedido;
import com.example.reto_1_2_sqlite.modelos.Partner;
import com.example.reto_1_2_sqlite.modelos.User;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * <p>Hilo que inserta la información de los pedidos guardados en el sevidor de mysql</p>
 * <p><ol>
 *     <li>Primero recoge los ids de los pedidos en el servidor para filtrar los pedidos en local</li>
 *     <li>Luego recoge los pedidos filtrando los que no se han sincronizado previamente</li>
 * </ol></p>
 */
public class HiloCarga extends Thread {
    private Context context;
    private User user;

    public HiloCarga(Context context, User user) {
        this.context = context;
        this.user = user;
    }

    @Override
    public void run() {
        String url = "jdbc:mysql://192.168.1.133:3306/prueba_carga";
        ArrayList<Integer> idsMysql = new ArrayList<>();
        String idsQuery = "-1", insert;

        try {
            Connection conn = DriverManager.getConnection(url, "daniroot", "dani");
            DBHandler handler = new DBHandler(context);

            //PARTNERS
            //TODO Modificar el método de recuperación de los partners para filtrar con el not in ()
            String query = "select id_partner from partners;";
            Statement stmt = conn.createStatement();
            ResultSet result = stmt.executeQuery(query);
            //Recorremos los ids de las cabeceras de los pedidos y las transformamos en un string
            // para filtrar los pedidos guardados en local
            while (result.next()) {
                idsMysql.add(result.getInt(1));
            }
            if (idsMysql.size() > 0) {
                idsQuery = "";
                for (int i = 0; i < idsMysql.size(); i++) {
                    if (i != idsMysql.size() - 1) {
                        idsQuery += idsMysql.get(i) + ",";
                    } else {
                        idsQuery += idsMysql.get(i);
                    }
                }
            }
            ArrayList<Partner> partners = handler.getArrayPartners(user, -1, idsQuery);
            for (Partner p: partners) {
                insert = "insert into partners values (" +
                        "" + p.getId() + "," +
                        "'" + p.getNombre() + "'," +
                        "'" + p.getDireccion() + "'," +
                        "" + p.getTelefono() + "," +
                        "" + p.getUsuarioId() + ");";
                Statement stmti = conn.createStatement();
                stmti.execute(insert);
            }

            //PEDIDOS
            idsQuery = "-1";
            idsMysql.clear();
            query = "select id_pedido from cab_pedidos;";
            stmt = conn.createStatement();
            result = stmt.executeQuery(query);

            //Recorremos los ids de las cabeceras de los pedidos y las transformamos en un string
            // para filtrar los pedidos guardados en local
            while (result.next()) {
                idsMysql.add(result.getInt(1));
            }

            if (idsMysql.size() > 0) {
                idsQuery = "";
                for (int i = 0; i < idsMysql.size(); i++) {
                    if (i != idsMysql.size() - 1) {
                        idsQuery += idsMysql.get(i) + ",";
                    } else {
                        idsQuery += idsMysql.get(i);
                    }
                }
            }

            //Filtramos las cabeceras guardadas en local para hacer el insert en mysql
            ArrayList<CabeceraPedido> cabeceras = handler.getArrayPedidos(user, idsQuery);
            //Recorremos el array para insertar la información en el servidor de mysql
            for (int i = 0; i < cabeceras.size(); i++) {
                String fechaEnvio = cabeceras.get(i).getFechaEnvio();

                insert = "insert into cab_pedidos values (" +
                        "" + cabeceras.get(i).getId() + "," +
                        "" + cabeceras.get(i).getPartnerId() + "," +
                        "'" + cabeceras.get(i).getFechaPedido() + "'," +
                        "'" + cabeceras.get(i).getFechaPago() + "',";
                //Comprobación de la fecha de envío, por si la mete el comercial directamente
                if (fechaEnvio.equals("")) {
                    insert += "null,";
                } else {
                    insert += "'" + fechaEnvio + "',";
                }
                insert += "null," +
                        "null);";

                Statement stmti = conn.createStatement();
                stmti.execute(insert);
            }

            //Realizamos el mismo proceso con todas las lineas guardas
            idsQuery = "-1";
            idsMysql.clear();
            query = "select id_linea from lin_pedidos;";
            stmt = conn.createStatement();
            result = stmt.executeQuery(query);

            //Recorremos los ids de las cabeceras de los pedidos y las transformamos en un string
            // para filtrar los pedidos guardados en local
            while (result.next()) {
                idsMysql.add(result.getInt(1));
            }

            if (idsMysql.size() > 0) {
                idsQuery = "";
                for (int i = 0; i < idsMysql.size(); i++) {
                    if (i != idsMysql.size() - 1) {
                        idsQuery += idsMysql.get(i) + ",";
                    } else {
                        idsQuery += idsMysql.get(i);
                    }
                }
            }

            ArrayList<LinPedido> lineas = handler.getArrayLineas(user, idsQuery);
            for (int i = 0; i < lineas.size(); i++) {
                insert = "insert into lin_pedidos values (" +
                        "" + lineas.get(i).getIdLinea() + "," +
                        "" + lineas.get(i).getIdPedido() + "," +
                        "" + lineas.get(i).getIdArticulo() + "," +
                        "" + lineas.get(i).getIdDelegacion() + "," +
                        "" + lineas.get(i).getCantidad() + ");";
                Statement stmti = conn.createStatement();
                stmti.execute(insert);
            }
        } catch (Exception e) {
            Log.d("CONEXIÓN", e.getMessage());
        }
    }
}
