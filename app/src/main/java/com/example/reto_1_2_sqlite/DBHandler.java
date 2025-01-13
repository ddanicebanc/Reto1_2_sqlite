package com.example.reto_1_2_sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class DBHandler extends SQLiteOpenHelper {
    private static final String DB_NAME = "comercialesdb";
    private static final int DB_VERSION = 1;

    public DBHandler (Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String query = "create table usuarios (" +
                "id integer primary key autoincrement," +
                "nombre text unique," +
                "contrasenia text," +
                "delegacion_id integer)";
        sqLiteDatabase.execSQL(query);

        query = "create table delegaciones (" +
                "id integer primary key," +
                "nombre text)";
        sqLiteDatabase.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("drop table if exists usuarios");
        sqLiteDatabase.execSQL("drop table if exists delegaciones");
        onCreate(sqLiteDatabase);
    }

    public boolean areEmpty (String tableName) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Boolean empty = true;

        Cursor cursor = sqLiteDatabase.query(
                tableName,              // The table to query
                null,                   // The array of columns to return (pass null to get all)
                null,                   // The columns for the WHERE clause
                null,                   // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                null                    // don't sort
        );

        if (cursor.getCount() > 0) {
            empty = false;
        }

        cursor.close();

        return empty;
    }

    public boolean checkUsers (String searchField, ArrayList<String> searchValues) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        String selection;
        String[] selectionArgs = new String[searchValues.size()];
        String sortOrder = " desc";
        Boolean exists = false;

        //Set the sort order
        sortOrder = searchField + sortOrder;

        //Check received search field: if searchField is empty
        if (searchField.isEmpty()) {
            selection = "id = ?";
        } else {
            selection = searchField + " = ?";
        }

        //Check received search values: if searchValues contains something
        if (!searchValues.isEmpty()) {
            for (int i = 0; i < searchValues.size(); i++) {
                selectionArgs[i] = searchValues.get(i);
            }
        }

        //Create the cursor and execute the associated query
        Cursor cursor = sqLiteDatabase.query(
                "usuarios",        // The table to query
                null,                   // The array of columns to return (pass null to get all)
                selection,              // The columns for the WHERE clause
                selectionArgs,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                sortOrder               // The sort order
        );

        if (cursor.getCount() > 0) {
            exists = true;
        }

        cursor.close();

        return exists;
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