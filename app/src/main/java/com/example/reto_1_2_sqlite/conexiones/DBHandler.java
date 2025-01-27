package com.example.reto_1_2_sqlite.conexiones;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.reto_1_2_sqlite.modelos.Articulo;
import com.example.reto_1_2_sqlite.modelos.CabeceraPedido;
import com.example.reto_1_2_sqlite.modelos.User;
import com.example.reto_1_2_sqlite.modelos.Visita;
import com.example.reto_1_2_sqlite.modelos.Partner;

import java.time.LocalDate;
import java.util.ArrayList;

public class DBHandler extends SQLiteOpenHelper {
    private static final String DB_NAME = "comercialesdb";
    private static final int DB_VERSION = 1;

    public DBHandler (Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        SQLiteDatabase db = this.getReadableDatabase();
        db.rawQuery("pragma foreign_keys = on", null, null);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String query;

        query = "create table comerciales (\n" +
                "id integer primary key autoincrement,\n" +
                "nombre text,\n" +
                "telefono integer,\n" +
                "delegacion_id integer\n" +
                ")";
        sqLiteDatabase.execSQL(query);

        query = "create table usuarios (\n" +
                "id integer primary key autoincrement,\n" +
                "nombre text unique,\n" +
                "contrasenia text,\n" +
                "comercial_id integer,\n" +
                "foreign key (comercial_id) references comerciales (id) on delete cascade\n" +
                ")";
        sqLiteDatabase.execSQL(query);

        query = "create table partners (" +
                "id integer primary key autoincrement," +
                "nombre text," +
                "direccion text," +
                "poblacion text," +
                "telefono integer," +
                "email text," +
                "usuario_Id integer," +
                "foreign key (usuario_id) references usuarios (id) on delete cascade)";
        sqLiteDatabase.execSQL(query);

        query = "create table articulos (" +
                "id integer primary key autoincrement," +
                "nombre varchar(50)," +
                "tipo varchar(50)," +
                "delegacion_id integer," +
                "imagen blob)";
        sqLiteDatabase.execSQL(query);

        query = "create table catalogo (\n" +
                "articulo_id integer,\n" +
                "delegacion_id integer,\n" +
                "precio real,\n" +
                "primary key (articulo_id, delegacionId),\n" +
                "foreign key (articulo_id) references articulos (id) on delete cascade,\n" +
                "foreign key (delegacion_id) references delegaciones (id) on delete cascade\n" +
                ")";
        sqLiteDatabase.execSQL(query);

        query = "create table visitas (" +
                "id integer primary key autoincrement," +
                "usuario_id integer," +
                "partner_id integer," +
                "fechaVisita date," +
                "direccion text," +
                "foreign key (usuario_id) references usuarios (id) on delete cascade," +
                "foreign key (partner_id) references partners (id) on delete cascade)";
        sqLiteDatabase.execSQL(query);

        query = "create table cab_pedidos (" +
                "id integer primary key autoincrement," +
                "fechaPedido date," +
                "fechaPago date," +
                "fechaEnvio date," +
                "usuario_id integer," +
                "delegacion_id integer," +
                "partner_id integer," +
                "foreign key (usuario_id) references usuarios (id) on delete cascade," +
                "foreign key (partner_id) references partners (id) on delete cascade)";
        sqLiteDatabase.execSQL(query);

        query = "create table lin_pedidos (" +
                "id integer primary key autoincrement," +
                "numeroLinea integer," +
                "articulo_id integer," +
                "cantidad integer," +
                "precio real," +
                "cab_pedido_id integer," +
                "foreign key (articulo_id) references articulos (id) on delete cascade," +
                "foreign key (cab_pedido_id) references cab_pedidos (id) on delete cascade)";
        sqLiteDatabase.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("drop table if exists lin_pedidos");
        sqLiteDatabase.execSQL("drop table if exists cab_pedidos");
        sqLiteDatabase.execSQL("drop table if exists visitas");
        sqLiteDatabase.execSQL("drop table if exists catalogo");
        sqLiteDatabase.execSQL("drop table if exists articulos");
        sqLiteDatabase.execSQL("drop table if exists partners");
        sqLiteDatabase.execSQL("drop table if exists usuarios");
        sqLiteDatabase.execSQL("drop table if exists comerciales");
        onCreate(sqLiteDatabase);
    }

    public boolean countTable (String tableName) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        boolean empty = true;

        Cursor cursor = sqLiteDatabase.query(
                tableName,
                null,
                null,
                null,
                null,
                null,
                null
        );

        if (cursor.getCount() > 0) {
            empty = false;
        }

        cursor.close();

        return empty;
    }

    public boolean searchByName (String tableName, String searchField, String searchValue) {
        SQLiteDatabase sqLiteDatabase;
        Cursor c;
        String query;
        boolean existe;

        sqLiteDatabase = this.getReadableDatabase();
        query = "select * from " + tableName + " where " + searchField + " = '" + searchValue + "'";
        c = sqLiteDatabase.rawQuery(query, null, null);
        existe = false;

        if (c.getCount() > 0) {
            existe = true;
        }

        c.close();

        return existe;
    }

    public boolean checkPassword (String searchValue) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        String[] selectionArgs = searchValue.split(";");
        String selection = "nombre = ? AND contrasenia = ?";
        boolean correct = false;

        Cursor cursor = sqLiteDatabase.query(
                "usuarios",
                null,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        if (cursor.getCount() > 0) {
            correct = true;
        }

        cursor.close();

        return correct;
    }

    public boolean comprobarNombrePartner (String nombrePartner, User user) {
        boolean matches = false;
        SQLiteDatabase db = this.getReadableDatabase();
        String query;

        query = "select partners.nombre " +
                "from partners " +
                "inner join usuarios on (usuarios.id = partners.usuarioId) " +
                "inner join comerciales on (comerciales.id = usuarios.comercial_id) " +
                "where upper(partners.nombre) = '" + nombrePartner.toUpperCase() + "' " +
                "and comerciales.delegacion_id = " + user.getDelegationId();

        Cursor c = db.rawQuery(query, null, null);
        if (c.getCount() == 1) {
            matches = true;
        }

        c.close();

        return matches;
    }

    public User loadUser (String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query, nombre;
        int idUsuario, idDelegacion;
        Cursor c;
        User user = null;

        query = "select u.id, u.nombre, c.delegacion_id " +
                "from usuarios u " +
                "inner join comerciales c on (c.id = u.comercial_id)" +
                "where u.nombre = '" + username + "'";

        c = db.rawQuery(query, null,null);

        if (c.getCount() == 1) {
            while (c.moveToNext()) {
                idUsuario = c.getInt(0);
                nombre = c.getString(1);
                idDelegacion = c.getInt(2);

                user = new User(
                        nombre,
                        idUsuario,
                        idDelegacion
                );
            }
        }

        return user;
    }

    public ArrayList<Visita> getArrayVisitas (User usuario, boolean historico) {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Visita> visitas = new ArrayList<>();
        String query;
        Cursor cursor;
        int visitaId, usuarioId, partnerId, activeUserId;
        String fechaVisita, direccion, partnerName = "", fechaHoy;
        LocalDate today = LocalDate.now();
        fechaHoy = today.toString();

        activeUserId = usuario.getId();

        query = "select * from visitas where usuarioId = " + String.valueOf(activeUserId);

        if (historico == false) {
            query = query + " and fechaVisita >= '" + fechaHoy + "'";
        }

        query = query + " order by fechaVisita asc";

        cursor = db.rawQuery(query, null, null);

        while (cursor.moveToNext()) {
            visitaId = cursor.getInt(0);
            usuarioId = cursor.getInt(1);
            partnerId = cursor.getInt(2);
            fechaVisita = cursor.getString(3);
            direccion = cursor.getString(4);

            //Formatear la fecha de visita para la correcta visualización
            String[] partesFecha = fechaVisita.split("-");
            fechaVisita = "";
            for (int i = partesFecha.length - 1; i >= 0; i--) {
                if (i != 0) {
                    fechaVisita = fechaVisita + partesFecha[i] + "-";
                } else {
                    fechaVisita = fechaVisita + partesFecha[i];
                }
            }

            String partnersQuery = "select nombre from partners where id = " + partnerId;

            Cursor cursorPartners = db.rawQuery(partnersQuery, null, null);

            if (cursorPartners.getCount() == 1) {
                while (cursorPartners.moveToNext()) {
                    partnerName = cursorPartners.getString(0);
                }
            }

            cursorPartners.close();

            visitas.add(new Visita(
                    visitaId,
                    usuarioId,
                    partnerName,
                    fechaVisita,
                    direccion
            ));
        }

        cursor.close();

        return visitas;
    }

    public ArrayList<Partner> getArrayPartners (User user, int partnerId) {
        ArrayList<Partner> partners = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "select * from partners where usuarioId = " + user.getId();
        int id,telefono,usuarioId;
        String nombre,direccion,poblacion,email;


        if (partnerId != -1 ){
            query=query + " and id = " + partnerId;
        }

        Cursor c = db.rawQuery(query, null, null);


        while (c.moveToNext()) {
            id=c.getInt(0);
            nombre=c.getString(1);
            direccion=c.getString(2);
            poblacion=c.getString(3);
            telefono=c.getInt(4);
            email=c.getString(5);
            usuarioId=c.getInt(6);

            partners.add(new Partner(
                    id,
                    usuarioId,
                    telefono,
                    nombre,
                    direccion,
                    poblacion,
                    email
            ));
        }
        c.close();
        return partners;
    }

    public ArrayList<CabeceraPedido> getArrayPedidos (User user) {
        ArrayList<CabeceraPedido> pedidos = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "select * from cab_pedidos where usuarioId = " + user.getId();
        int id,usuarioId,delegacionId, partnerId;
        String fechaPedido,fechaEnvio,fechaPago;

        Cursor c = db.rawQuery(query, null, null);


        while (c.moveToNext()) {
            id=c.getInt(0);
            fechaPedido = c.getString(1);
            fechaPago = c.getString(2);
            fechaEnvio = c.getString(3);
            usuarioId = c.getInt(4);
            delegacionId = c.getInt(5);
            partnerId = c.getInt(6);

            pedidos.add(new CabeceraPedido(
                    id,
                    usuarioId,
                    delegacionId,
                    fechaPedido,
                    fechaEnvio,
                    fechaPago,
                    partnerId
            ));
        }
        c.close();

        return pedidos;
    }

    public ArrayList<Articulo> getArrayArticulos (User user) {
        ArrayList<Articulo> articulos = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "select nombre, imagen from articulos where delegacion_id = " + user.getDelegationId();
        Cursor c;

        c = db.rawQuery(query, null);

        while (c.moveToNext()) {
            byte[] byteImage = c.getBlob(1);
            Bitmap imagen = BitmapFactory.decodeByteArray(byteImage, 0, byteImage.length);

            articulos.add(new Articulo(
                    c.getString(0),
                    imagen
            ));
        }

        return articulos;
    }

    //TODO Seguir aquí
    public ArrayList<String> getNombrePartners (User user) {
        ArrayList<String> datos = new ArrayList<>();
        String query = "select nombre" +
                " from partners " +
                " inner join usuarios on (usuarios.partner_id = partners.id)" +
                " inner join comerciales on (comerciales.id = usuarios.comercial_id)" +
                " where comerciales.delegacion_id = " + user.getDelegationId();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(query, null, null);

        if (c.getCount() > 0) {
            while (c.moveToNext()) {
                datos.add(c.getString(0));
            }
        }

        c.close();

        return datos;
    }

    public ArrayList<Integer> getIdPartners (User user) {
        ArrayList<Integer> datos = new ArrayList<>();
        String query = "select id " +
                " from partners " +
                " inner join usuarios on (usuarios.partner_id = partners.id)" +
                " inner join comerciales on (comerciales.id = usuarios.comercial_id)" +
                " where comerciales.delegacion_id = " + user.getDelegationId();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(query, null, null);

        if (c.getCount() > 0) {
            while (c.moveToNext()) {
                datos.add(c.getInt(0));
            }
        }

        c.close();

        return datos;
    }

    public ArrayList<String> getSearchFieldArray (String tableName, String searchColumn) {
        ArrayList<String> searchColumnArray = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "select "+ searchColumn +" from "+ tableName;

        Cursor c = db.rawQuery(query, null, null);

        while (c.moveToNext()) {
            searchColumnArray.add(c.getString(0));
        }

        c.close();

        return searchColumnArray;
    }

    public ArrayList<String> getArticuloStringArray(String nombreColumna, int idDel) {
        ArrayList<String> datos = new ArrayList<>();
        String query = "select " + nombreColumna +
                " from articulos " +
                "where delegacion_id = " + idDel;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(query, null, null);

        if (c.getCount() > 0) {
            while (c.moveToNext()) {
                datos.add(c.getString(0));
            }
        }

        c.close();

        return datos;
    }

    public ArrayList<Integer> getArticuloIntArray(String nombreColumna, int idDel) {
        ArrayList<Integer> datos = new ArrayList<>();
        String query = "select " + nombreColumna +
                " from articulos " +
                "where delegacion_id = " + idDel;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(query, null, null);

        if (c.getCount() > 0) {
            while (c.moveToNext()) {
                datos.add(c.getInt(0));
            }
        }

        c.close();

        return datos;
    }

    public String getArticuloStringData (String nombreColumna, int idArticulo) {
        String valor = "";
        String query = "select " + nombreColumna + " from articulos where id = " + idArticulo;
        Cursor c = this.getReadableDatabase().rawQuery(query, null);

        if (c.getCount() == 1) {
            while (c.moveToNext()) {
                valor = c.getString(0);
            }
        }
        c.close();

        return valor;
    }

    public ArrayList<String> getComercialStringData (String nombreColumna) {
        ArrayList<String> datos = new ArrayList<>();
        String query = "select " + nombreColumna +
                " from comerciales " +
                "where id not in (" +
                "select comercial_id " +
                "from usuarios)";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(query, null, null);

        if (c.getCount() > 0) {
            while (c.moveToNext()) {
                datos.add(c.getString(0));
            }
        }

        c.close();

        return datos;
    }

    public ArrayList<Integer> getComercialIntData (String nombreColumna) {
        ArrayList<Integer> datos = new ArrayList<>();
        String query = "select " + nombreColumna +
                " from comerciales " +
                "where id not in (" +
                "select comercial_id " +
                "from usuarios)";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(query, null, null);

        if (c.getCount() > 0) {
            while (c.moveToNext()) {
                datos.add(c.getInt(0));
            }
        }

        c.close();

        return datos;
    }



    public ArrayList<Integer> getSearchFieldIntegerArray (String tableName, String searchColumn) {
        ArrayList<Integer> searchColumnArray = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "select "+ searchColumn +" from "+ tableName;

        Cursor c = db.rawQuery(query, null, null);

        while (c.moveToNext()) {
            searchColumnArray.add(c.getInt(0));
        }

        c.close();

        return searchColumnArray;
    }

    public String getSearchField (String tableName, String filterColumn, String filterValue, String searchColumn) {
        String returnValue = "";
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "select "+ searchColumn +
                " from " + tableName +
                " where " + filterColumn + " = " + "'" + filterValue + "'";

        Cursor c = db.rawQuery(query, null, null);

        if (c.getCount() == 1) {
            while (c.moveToNext()) {
                returnValue = c.getString(0);
            }
        } else {
            returnValue = "error";
        }

        c.close();

        return returnValue;
    }

    public int getLatestId (String tableName) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c;
        String query;
        int id = -1;

        query = "select max(id) from " + tableName;
        c = db.rawQuery(query, null, null);
        if (c.getCount() == 1) {
            while (c.moveToNext()) {
                id = c.getInt(0);
            }
        }
        c.close();

        return id;
    }

    public Bitmap getImagenArticulo (int idArticulo) {
        SQLiteDatabase db = this.getReadableDatabase();
        Bitmap imagen = null;
        String query = "select imagen from articulos where id = " + idArticulo;
        Cursor c = db.rawQuery(query, null);

        if (c.getCount() == 1) {
            while (c.moveToNext()) {
                byte[] byteImage = c.getBlob(0);
                imagen = BitmapFactory.decodeByteArray(byteImage, 0, byteImage.length);
            }
        }

        return imagen;
    }

    public boolean insertData (String tableName, ArrayList<String> columns, ArrayList<String> columnValues) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        boolean error = false;

        //TODO Check columns[] and columnValues[] size
        for (int i = 0; i < columns.size(); i++) {
            values.put(columns.get(i), columnValues.get(i));
        }

        if (db.insert(tableName, null, values) == -1) {
            error = true;
        }

        return error;
    }
}