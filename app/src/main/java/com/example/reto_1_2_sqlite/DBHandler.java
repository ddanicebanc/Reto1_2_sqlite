package com.example.reto_1_2_sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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
        String query = "create table delegaciones (" +
                "id integer primary key," +
                "nombre text)";
        sqLiteDatabase.execSQL(query);

        query = "create table usuarios (" +
                "id integer primary key autoincrement," +
                "nombre text unique," +
                "contrasenia text," +
                "delegacionId integer," +
                "foreign key (delegacionId) references delegaciones (id))";
        sqLiteDatabase.execSQL(query);

        query = "create table partners (" +
                "id integer primary key autoincrement," +
                "nombre text," +
                "direccion text," +
                "poblacion text," +
                "telefono integer," +
                "email text," +
                "usuarioId integer," +
                "foreign key (usuarioId) references usuarios (id))";
        sqLiteDatabase.execSQL(query);

        query = "create table articulos (" +
                "id int primary key autoincrement," +
                "nombre varchar(50)," +
                "tipo varchar(50)," +
                "delegacion_id integer," +
                "foreign key (delegacion_id) references delegaciones (id_delegacion))";
        sqLiteDatabase.execSQL(query);

        query = "create table visitas (" +
                "id integer primary key autoincrement," +
                "usuarioId integer," +
                "partnerId integer," +
                "fechaVisita date," +
                "direccion text," +
                "foreign key (usuarioId) references usuarios (id)," +
                "foreign key (partnerId) references partners (id))";
        sqLiteDatabase.execSQL(query);

        query = "create table cab_pedidos (" +
                "id integer primary key autoincrement," +
                "fechaPedido date," +
                "fechaPago date," +
                "fechaEnvio date," +
                "usuarioId integer," +
                "delegacionId integer," +
                "partnerId integer," +
                "foreign key (usuarioId) references usuarios (id)," +
                "foreign key (delegacionId) references delegaciones (id)," +
                "foreign key (partnerId) references partners (id))";
        sqLiteDatabase.execSQL(query);

        query = "create table lin_pedidos (" +
                "id integer primary key autoincrement," +
                "articuloId integer," +
                "cantidad integer," +
                "precio real," +
                "cab_pedido_id integer," +
                "foreign key (cab_pedido_id) references articulos (id)," +
                "foreign key (cab_pedido_id) references cab_pedidos (id))";
        sqLiteDatabase.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("drop table if exists lin_pedidos");
        sqLiteDatabase.execSQL("drop table if exists cab_pedidos");
        sqLiteDatabase.execSQL("drop table if exists visitas");
        sqLiteDatabase.execSQL("drop table if exists articulos");
        sqLiteDatabase.execSQL("drop table if exists partners");
        sqLiteDatabase.execSQL("drop table if exists usuarios");
        sqLiteDatabase.execSQL("drop table if exists delegaciones");
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
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        String[] selectionArgs = {searchValue};
        boolean exists = false;

        //Must have "= ?" to use it in the query
        searchField = searchField + "= ?";

        //Create the cursor and execute the associated query
        Cursor cursor = sqLiteDatabase.query(
                tableName,       // The table to query
                null,                   // The array of columns to return (pass null to get all)
                searchField,            // The columns for the WHERE clause
                selectionArgs,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                null                    // The sort order
        );

        if (cursor.getCount() > 0) {
            exists = true;
        }

        cursor.close();

        return exists;
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

    public boolean checkMatchingStringField (String tableName, String searchField, String searchValue) {
        boolean matches = false;
        SQLiteDatabase db = this.getReadableDatabase();
        String query;

        query = "select " + searchField +
                " from " + tableName +
                " where upper(" + searchField + ") = '" + searchValue.toUpperCase() + "'";

        Cursor c = db.rawQuery(query, null, null);
        if (c.getCount() == 1) {
            matches = true;
        }

        c.close();

        return matches;
    }

    public User loadUser (String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] selectionArgs = {username};
        String sUsername = "";
        int id = -1, delegationId = -1;
        User user = null;

        Cursor cursor = db.query(
                "usuarios",
                null,
                "nombre = ?",
                selectionArgs,
                null,
                null,
                null
        );

        if (cursor.getCount() == 1) {
            while (cursor.moveToNext()) {
                sUsername = cursor.getString(1);
                id = cursor.getInt(0);
                delegationId = cursor.getInt(2);
            }

            user = new User(sUsername, id, delegationId);
        }

        cursor.close();

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


        if (partnerId!= -1 ){
            query=query + " and id = "+partnerId;
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