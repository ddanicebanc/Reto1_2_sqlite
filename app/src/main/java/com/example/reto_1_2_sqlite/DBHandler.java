package com.example.reto_1_2_sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;

public class DBHandler extends SQLiteOpenHelper {
    private static final String DB_NAME = "comercialesdb";
    private static final int DB_VERSION = 1;

    public DBHandler (Context context) {
        super(context, DB_NAME, null, DB_VERSION);
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
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("drop table if exists visitas");
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

    public boolean searchUser (String searchField, String searchValue) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        String[] selectionArgs = {searchValue};
        boolean exists = false;

        //Must have "= ?" to use it in the query
        searchField = searchField + "= ?";

        //Create the cursor and execute the associated query
        Cursor cursor = sqLiteDatabase.query(
                "usuarios",       // The table to query
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

    public User loadUser (String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] selectionArgs = {username};
        String sUsername = "";
        int id = -1;
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
            }

            user = new User(sUsername, id);
        }

        cursor.close();

        return user;
    }

    public void insertData (String tableName, ArrayList<String> columns, ArrayList<String> columnValues) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        //TODO Check columns[] and columnValues[] size
        for (int i = 0; i < columns.size(); i++) {
            values.put(columns.get(i), columnValues.get(i));
        }

        db.insert(tableName, null, values);
    }
}