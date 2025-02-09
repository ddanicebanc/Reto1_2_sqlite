package com.example.reto_1_2_sqlite.modelos;

import android.graphics.Bitmap;

public class Articulo {
    private String nombre, categoria, precio, stock;
    private Bitmap imagen;

    public Articulo(String nombre, Bitmap imagen) {
        this.nombre = nombre;
        this.imagen = imagen;
    }

    public Articulo(String nombre, String categoria, String precio, String stock, Bitmap imagen) {
        this.nombre = nombre;
        this.categoria = categoria;
        this.precio = precio;
        this.stock = stock;
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

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }
}
