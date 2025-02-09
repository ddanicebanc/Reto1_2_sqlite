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
import com.example.reto_1_2_sqlite.modelos.LinPedido;
import com.example.reto_1_2_sqlite.modelos.Partner;
import com.example.reto_1_2_sqlite.modelos.User;
import com.example.reto_1_2_sqlite.modelos.Visita;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Base64;

/**
 * {@code DBHandler} es una clase que gestiona la base de datos SQLite de la aplicación.
 * Proporciona métodos para crear, actualizar y consultar datos de las diferentes tablas.
 * Esta clase hereda de {@link SQLiteOpenHelper} y se encarga de la creación y gestión de la base de datos.
 */
public class DBHandler extends SQLiteOpenHelper {
    private static final String DB_NAME = "comercialesdb";
    public static int DB_VERSION = 1;
    /**
     * Constructor de la clase {@code DBHandler}.
     * @param context El contexto de la aplicación.
     */
    public DBHandler (Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        SQLiteDatabase db = this.getReadableDatabase();
        db.rawQuery("pragma foreign_keys = on", null, null).close();
    }
    /**
     * Constructor de la clase {@code DBHandler}. Permite especificar la versión de la base de datos.
     * @param context El contexto de la aplicación.
     * @param version La versión de la base de datos.
     */
    public DBHandler (Context context, int version) {
        super (context, DB_NAME, null, version);
        SQLiteDatabase db = this.getReadableDatabase();
        db.rawQuery("pragma foreign_keys = on", null, null).close();
    }
    /**
     * Se llama cuando se crea la base de datos por primera vez.
     * @param sqLiteDatabase La base de datos.
     */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String query;

        query = "create table delegaciones (\n" +
                "id_delegacion intenger primary key,\n" +
                "nombre text," +
                "longitud real," +
                "latitud real" +
                ")";
        sqLiteDatabase.execSQL(query);

        query = "create table comerciales (\n" +
                "id integer primary key autoincrement,\n" +
                "nombre text,\n" +
                "telefono integer,\n" +
                "email text,\n" +
                "delegacion_id integer,\n" +
                "foreign key (delegacion_id) references delegaciones (id_delegacion) on delete cascade" +
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
                    "imagen text)";
        sqLiteDatabase.execSQL(query);

        query = "create table catalogo (\n" +
                "articulo_id integer,\n" +
                "delegacion_id integer,\n" +
                "stock integer,\n" +
                "precio real,\n" +
                "primary key (articulo_id, delegacion_id),\n" +
                "foreign key (articulo_id) references articulos (id) on delete cascade,\n" +
                "foreign key (delegacion_id) references delegaciones (id_delegacion) on delete cascade\n" +
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
                "delegacion_id integer," +
                "cantidad integer," +
                "precio real," +
                "cab_pedido_id integer," +
                "foreign key (articulo_id, delegacion_id) references catalogo (articulo_id, delegacion_id) on delete cascade," +
                "foreign key (cab_pedido_id) references cab_pedidos (id) on delete cascade)";
        sqLiteDatabase.execSQL(query);
    }
    /**
     * Se llama cuando la base de datos necesita ser actualizada.
     * @param sqLiteDatabase La base de datos.
     * @param i La versión antigua de la base de datos.
     * @param i1 La nueva versión de la base de datos.
     */
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
        sqLiteDatabase.execSQL("drop table if exists delegaciones");

        onCreate(sqLiteDatabase);
    }
    /**
     * Comprueba si una tabla está vacía.
     * @param tableName El nombre de la tabla.
     * @return {@code true} si la tabla está vacía, {@code false} en caso contrario.
     */
    public boolean isEmpty (String tableName) {
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
    /**
     * Busca un valor en una tabla por nombre de campo.
     *
     * @param tableName El nombre de la tabla.
     * @param searchField El nombre del campo en el que se va a buscar.
     * @param searchValue El valor que se va a buscar.
     * @return {@code true} si se encuentra el valor en la tabla, {@code false} en caso contrario.
     */
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
    /**
     * Verifica si la contraseña coincide con el nombre de usuario almacenado en la base de datos.
     * El valor de búsqueda (`searchValue`) debe estar en el formato "nombre_usuario;contraseña".
     *
     * @param searchValue El nombre de usuario y la contraseña separados por un punto y coma (";").
     * @return {@code true} si la contraseña coincide con el nombre de usuario, {@code false} en caso contrario.
     */
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
    /**
     * Comprueba si existe un partner con el nombre especificado para el usuario y delegación dados.
     * La búsqueda no distingue entre mayúsculas y minúsculas.
     *
     * @param nombrePartner El nombre del partner a buscar.
     * @param user          El usuario para el que se realiza la búsqueda. Se utiliza para obtener el ID de la delegación.
     * @return {@code true} si existe un partner con el nombre especificado para el usuario y delegación dados, {@code false} en caso contrario.
     */
    public boolean comprobarNombrePartner (String nombrePartner, User user) {
        boolean matches = false;
        SQLiteDatabase db = this.getReadableDatabase();
        String query;

        query = "select partners.nombre " +
                "from partners " +
                "inner join usuarios on (usuarios.id = partners.usuario_id) " +
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
    /**
     * Carga los datos de un usuario a partir de su nombre de usuario.
     *
     * @param username El nombre de usuario del usuario a cargar.
     * @return Un objeto {@link User} con los datos del usuario, o {@code null} si no se encuentra el usuario.
     */
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

        c.close();

        return user;
    }
    /**
     * Obtiene un arraylist de visitas para un usuario, opcionalmente filtrando por visitas futuras o históricas.
     *
     * @param usuario   El usuario para el que se obtienen las visitas.
     * @param historico Si es {@code true}, se obtienen todas las visitas (históricas y futuras).
     *                  Si es {@code false}, se obtienen solo las visitas futuras (a partir de la fecha actual).
     * @return Un {@link ArrayList} de objetos {@link Visita} con las visitas del usuario.
     */
    public ArrayList<Visita> getArrayVisitas (User usuario, boolean historico) {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Visita> visitas = new ArrayList<>();
        String query;
        Cursor cursor;
        int visitaId, usuarioId, partnerId, activeUserId;
        String fechaVisita, direccion, partnerName = "", fechaHoy;
        LocalDate today = LocalDate.now();
        String partes[];

        fechaHoy = today.toString();
        partes = fechaHoy.split("-");

        //Se guarda la fecha con el formato MM-dd-yyyy
        fechaHoy = partes[1] + "-" + partes[2] + "-" + partes[0];

        activeUserId = usuario.getId();

        query = "select * from visitas where usuario_id = " + activeUserId;

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
            fechaVisita = partesFecha[1] + "-" + partesFecha[0] + "-" + partesFecha[2];

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
    /**
     * Obtiene un arraylist de partners, opcionalmente filtrando por ID y excluyendo IDs específicos.
     *
     * @param user      El usuario para el que se obtienen los partners. Se utiliza para filtrar por ID de usuario.
     * @param partnerId El ID del partner a buscar. Si es -1, no se aplica este filtro.
     * @param hilo      Una cadena con IDs de partners separados por comas. Se excluyen los partners con estos IDs.
     *                  Si está vacía, no se aplica este filtro.
     * @return Un {@link ArrayList} de objetos {@link Partner} con los partners que cumplen los criterios de filtrado.
     */
    public ArrayList<Partner> getArrayPartners (User user, int partnerId, String hilo) {
        ArrayList<Partner> partners = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "select * from partners";
        int id,telefono,usuarioId;
        String nombre,direccion,poblacion,email;

        if (!hilo.equals("")) {
            query += " where id not in (" + hilo + ")";
        } else {
            query += " where usuario_id = " + user.getId();
        }

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
    /**
     * Obtiene un arraylist de cabeceras de pedido, opcionalmente excluyendo IDs específicos.
     *
     * @param user El usuario para el que se obtienen las cabeceras de pedido. Se utiliza para filtrar por ID de usuario.
     * @param hilo Una cadena con IDs de cabeceras de pedido separados por comas. Se excluyen las cabeceras con estos IDs.
     *             Si está vacía, no se aplica este filtro.
     * @return Un {@link ArrayList} de objetos {@link CabeceraPedido} con las cabeceras de pedido que cumplen los criterios de filtrado.
     */
    public ArrayList<CabeceraPedido> getArrayPedidos (User user, String hilo) {
        ArrayList<CabeceraPedido> pedidos = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "select * from cab_pedidos";
        int id,usuarioId,delegacionId, partnerId;
        String fechaPedido,fechaEnvio,fechaPago;

        if (!hilo.equals("")) {
            query += " where id not in (" + hilo + ")";
        } else {
            query += " where usuario_id = " + user.getId();
        }

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
    /**
     * Obtiene un arraylist de artículos para un usuario, filtrando por la delegación del usuario.
     * Los artículos se obtienen a través de una unión entre las tablas "catalogo" y "articulos".
     *
     * @param user El usuario para el que se obtienen los artículos. Se utiliza para filtrar por ID de delegación.
     * @return Un {@link ArrayList} de objetos {@link Articulo} con los artículos de la delegación del usuario.
     */
    public ArrayList<Articulo> getArrayArticulos (User user) {
        ArrayList<Articulo> articulos = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "select articulos.nombre, articulos.imagen, catalogo.precio, catalogo.stock, articulos.tipo" +
                        " from catalogo " +
                        " inner join articulos on (articulos.id = catalogo.articulo_id)" +
                        " where catalogo.delegacion_id = " + user.getDelegationId();
        Cursor c;

        c = db.rawQuery(query, null);

        while (c.moveToNext()) {
            byte[] imageByteArray;
            imageByteArray = Base64.getDecoder().decode(c.getString(1));
            Bitmap imagen = BitmapFactory.decodeByteArray(imageByteArray, 0, imageByteArray.length);

            articulos.add(new Articulo(
                    c.getString(0),
                    c.getString(4),
                    c.getString(2),
                    c.getString(3),
                    imagen
            ));
        }

        c.close();

        return articulos;
    }
    /**
     * Obtiene una lista de nombres de partners para un usuario y su delegación.
     *
     * @param user El usuario para el que se obtienen los nombres de partners.
     * @return Un {@link ArrayList} de {@link String} con los nombres de los partners asociados al usuario y su delegación.
     */
    public ArrayList<String> getNombrePartners (User user) {
        ArrayList<String> datos = new ArrayList<>();
        String query = "select partners.nombre" +
                " from partners " +
                " inner join usuarios on (usuarios.id = partners.usuario_id)" +
                " inner join comerciales on (comerciales.id = usuarios.comercial_id)" +
                " where comerciales.delegacion_id = " + user.getDelegationId() +
                " and usuarios.id = " + user.getId();
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
    /**
     * Obtiene una lista de IDs de partners para un usuario y su delegación.
     *
     * @param user El usuario para el que se obtienen los IDs de partners.
     * @return Un {@link ArrayList} de {@link Integer} con los IDs de los partners asociados al usuario y su delegación.
     */
    public ArrayList<Integer> getIdPartners (User user) {
        ArrayList<Integer> datos = new ArrayList<>();
        String query = "select partners.id" +
                " from partners " +
                " inner join usuarios on (usuarios.id = partners.usuario_id)" +
                " inner join comerciales on (comerciales.id = usuarios.comercial_id)" +
                " where comerciales.delegacion_id = " + user.getDelegationId() +
                " and usuarios.id = " + user.getId();
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
    /**
     * Obtiene un arraylist de valores de una columna de la tabla "articulos", filtrando por ID de delegación.
     * Los datos se obtienen a través de una unión entre las tablas "articulos" y "catalogo".
     *
     * @param nombreColumna El nombre de la columna cuyos valores se van a obtener.
     * @param idDel          El ID de la delegación por la que se va a filtrar.
     * @return Un {@link ArrayList} de {@link String} con los valores de la columna especificada para la delegación dada.
     */
    public ArrayList<String> getArticuloStringArray(String nombreColumna, int idDel) {
        ArrayList<String> datos = new ArrayList<>();
        String query = "select " + nombreColumna +
                " from articulos " +
                " inner join catalogo on (articulos.id = catalogo.articulo_id)" +
                " where catalogo.delegacion_id = " + idDel;
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
    /**
     * Obtiene un arraylist de valores enteros de una columna de la tabla "articulos", opcionalmente filtrando por ID de delegación.
     * Los datos se obtienen a través de una unión entre las tablas "articulos" y "catalogo".
     *
     * @param nombreColumna El nombre de la columna cuyos valores se van a obtener.
     * @param idDel          El ID de la delegación por la que se va a filtrar. Si es -1, no se aplica este filtro.
     * @return Un {@link ArrayList} de {@link Integer} con los valores de la columna especificada, filtrados por la delegación dada (si se especifica).
     */
    public ArrayList<Integer> getArticuloIntArray(String nombreColumna, int idDel) {
        ArrayList<Integer> datos = new ArrayList<>();
        String query = "select " + nombreColumna +
                " from articulos" +
                " inner join catalogo on (articulos.id = catalogo.articulo_id) ";
        SQLiteDatabase db = this.getReadableDatabase();

        if (idDel != -1) {
            query = query + " where delegacion_id = " + idDel;
        }

        Cursor c = db.rawQuery(query, null, null);

        if (c.getCount() > 0) {
            while (c.moveToNext()) {
                datos.add(c.getInt(0));
            }
        }

        c.close();

        return datos;
    }
    /**
     * Obtiene un arraylist de valores de tipo String de una columna de la tabla "comerciales"
     * para aquellos comerciales que no tienen un usuario asociado.
     *
     * @param nombreColumna El nombre de la columna cuyos valores se van a obtener.
     * @return Un {@link ArrayList} de {@link String} con los valores de la columna especificada
     *         para los comerciales sin usuario asociado.
     */
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
    /**
     * Obtiene un arraylist de valores enteros de una columna de la tabla "comerciales"
     * para aquellos comerciales que no tienen un usuario asociado.
     *
     * @param nombreColumna El nombre de la columna cuyos valores se van a obtener.
     * @return Un {@link ArrayList} de {@link Integer} con los valores de la columna especificada
     *         para los comerciales sin usuario asociado.
     */
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
    /**
     * Obtiene un arraylist de valores enteros de una columna específica de una tabla.
     *
     * @param tableName    El nombre de la tabla de la que se van a obtener los datos.
     * @param searchColumn El nombre de la columna cuyos valores se van a obtener.
     * @return Un {@link ArrayList} de {@link Integer} con los valores de la columna especificada.
     */
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
    /**
     * Obtiene el último ID (el valor máximo) de la columna 'id' en una tabla específica.
     *
     * @param tableName El nombre de la tabla de la que se va a obtener el último ID.
     * @return El valor máximo de la columna 'id' en la tabla especificada, o -1 si la tabla está vacía o no tiene columna 'id'.
     */
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
    /**
     * Obtiene la imagen de un artículo a partir de su ID.
     *
     * @param idArticulo El ID del artículo cuya imagen se va a obtener.
     * @return Un objeto {@link Bitmap} con la imagen del artículo, o {@code null} si no se encuentra el artículo o no tiene imagen.
     */
    public Bitmap getImagenArticulo (int idArticulo) {
        SQLiteDatabase db = this.getReadableDatabase();
        Bitmap imageBM = null;
        String query = "select imagen from articulos where id = " + idArticulo;
        Cursor c = db.rawQuery(query, null);

        if (c.getCount() == 1) {
            while (c.moveToNext()) {
                byte[] imageByteArray = Base64.getDecoder().decode(c.getString(0));
                imageBM = BitmapFactory.decodeByteArray(imageByteArray, 0, imageByteArray.length);
            }
        }

        c.close();

        return imageBM;
    }
    /**
     * Inserta datos en una tabla específica.
     *
     * @param tableName    El nombre de la tabla en la que se van a insertar los datos.
     * @param columns      Un {@link ArrayList} de {@link String} con los nombres de las columnas en las que se van a insertar los datos.
     * @param columnValues Un {@link ArrayList} de {@link String} con los valores correspondientes a las columnas especificadas.
     *                     Debe tener la misma cantidad de elementos que `columns`.
     * @return {@code true} si la inserción falla, {@code false} si la inserción se realiza correctamente.
     */
    public boolean insertData (String tableName, ArrayList<String> columns, ArrayList<String> columnValues) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        boolean error = false;

        for (int i = 0; i < columns.size(); i++) {
            values.put(columns.get(i), columnValues.get(i));
        }

        if (db.insert(tableName, null, values) == -1) {
            error = true;
        }

        return error;
    }
    /**
     * Sincroniza el catálogo de la base de datos SQLite con un conjunto de resultados (ResultSet) proveniente de otra fuente (posiblemente una base de datos MySQL).
     * Este método compara los datos del ResultSet con los datos existentes en la tabla "catalogo" de la base de datos SQLite.
     * Si un registro del ResultSet no existe en la tabla "catalogo", se inserta.
     *
     * @param result El {@link ResultSet} que contiene los datos del catálogo a sincronizar.  Se espera que este ResultSet
     *               contenga al menos las columnas "id_articulo", "id_delegacion", "stock" y "precio".
     */
    public void getCatalogo(ResultSet result) {
        ArrayList<String> datos = new ArrayList<>();
        ArrayList<String> columnas = new ArrayList<>();
        int mysqlArticulo, mysqlDelegacion, sqliteArticulo, sqliteDelegacion;
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "select * from catalogo";

        columnas.add("articulo_id");
        columnas.add("delegacion_id");
        columnas.add("stock");
        columnas.add("precio");

        Cursor c = db.rawQuery(query, null);

        try {
            while (result.next()) {
                mysqlArticulo = result.getInt("id_articulo");
                mysqlDelegacion = result.getInt("id_delegacion");

                if (c.getCount() > 0) {
                    while (c.moveToNext()) {
                        sqliteArticulo = c.getInt(0);
                        sqliteDelegacion = c.getInt(1);

                        if (mysqlArticulo == sqliteArticulo && mysqlDelegacion == sqliteDelegacion) {

                        } else {
                            datos.add(String.valueOf(mysqlArticulo));
                            datos.add(String.valueOf(mysqlDelegacion));
                            datos.add(String.valueOf(result.getInt("stock")));
                            datos.add(String.valueOf(result.getFloat("precio")));

                            this.insertData("catalogo", columnas, datos);

                            datos.clear();
                        }
                    }
                } else {
                    datos.add(String.valueOf(mysqlArticulo));
                    datos.add(String.valueOf(mysqlDelegacion));
                    datos.add(String.valueOf(result.getInt("stock")));
                    datos.add(String.valueOf(result.getFloat("precio")));

                    this.insertData("catalogo", columnas, datos);

                    datos.clear();
                }
            }
        } catch (SQLException sqle) {

        }
    }
    /**
     * Obtiene un arraylist de líneas de pedido (LinPedido), opcionalmente excluyendo IDs específicos.
     *
     * @param user El usuario (no se utiliza directamente en la consulta, pero se incluye por consistencia).
     * @param hilo Una cadena con IDs de líneas de pedido separados por comas. Se excluyen las líneas con estos IDs.
     *             Si está vacía, no se aplica este filtro.
     * @return Un {@link ArrayList} de objetos {@link LinPedido} con las líneas de pedido que cumplen los criterios de filtrado.
     */
    public ArrayList<LinPedido> getArrayLineas(User user, String hilo) {
        ArrayList<LinPedido> lineas = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "select * from lin_pedidos";

        if (!hilo.equals("")) {
            query += " where id not in (" + hilo + ")";
        }

        Cursor c = db.rawQuery(query, null);
        while (c.moveToNext()){
            lineas.add(new LinPedido(
                    c.getInt(0),
                    c.getInt(6),
                    c.getInt(2),
                    c.getInt(3),
                    c.getInt(4),
                    c.getFloat(5)
            ));
        }
        c.close();

        return lineas;
    }
    /**
     * Obtiene la dirección de un partner a partir de su ID.
     *
     * @param partnerId El ID del partner cuya dirección se va a obtener.
     * @return La dirección del partner como una cadena de texto.
     *         Si no se encuentra el partner o no tiene dirección, devuelve "Dirección".
     */
    public String getPartnerAddress (int partnerId) {
        String direccion = "";
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "select direccion from partners where id = " + partnerId;
        Cursor c = db.rawQuery(query, null);

        while (c.moveToNext()) {
            direccion = c.getString(0);
        }
        c.close();

        if (direccion.equals("")) {
            direccion = "Dirección";
        }

        return direccion;
    }
}