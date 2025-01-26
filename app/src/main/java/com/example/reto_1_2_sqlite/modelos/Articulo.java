package com.example.reto_1_2_sqlite.modelos;

import android.graphics.Bitmap;

public class Articulo {
    private String nombre;
    private Bitmap imagen;

    public Articulo(String nombre, Bitmap imagen) {
        this.nombre = nombre;
        this.imagen = imagen;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Bitmap getImagen() {
        return imagen;
    }

    public void setImagen(Bitmap imagen) {
        this.imagen = imagen;
    }
}
