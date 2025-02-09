package com.example.reto_1_2_sqlite.modelos;

import android.graphics.Bitmap;

/**
 * La clase {@code Articulo} representa un artículo en el sistema.
 * Contiene información sobre el nombre, categoría, precio, stock e imagen del artículo.
 */
public class Articulo {
    private String nombre, categoria, precio, stock;
    private Bitmap imagen;
    /**
     * Constructor de la clase {@code Articulo}.
     *
     * @param nombre El nombre del artículo.
     * @param imagen La imagen del artículo en formato {@link Bitmap}.
     */
    public Articulo(String nombre, Bitmap imagen) {
        this.nombre = nombre;
        this.imagen = imagen;
    }
    /**
     * Constructor de la clase {@code Articulo}.
     *
     * @param nombre    El nombre del artículo.
     * @param categoria La categoría del artículo.
     * @param precio    El precio del artículo.
     * @param stock     El stock del artículo.
     * @param imagen    La imagen del artículo en formato {@link Bitmap}.
     */
    public Articulo(String nombre, String categoria, String precio, String stock, Bitmap imagen) {
        this.nombre = nombre;
        this.categoria = categoria;
        this.precio = precio;
        this.stock = stock;
        this.imagen = imagen;
    }

    /**
     * Obtiene el nombre del artículo.
     *
     * @return El nombre del artículo.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre del artículo.
     *
     * @param nombre El nombre del artículo.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Obtiene la imagen del artículo.
     *
     * @return La imagen del artículo en formato {@link Bitmap}.
     */
    public Bitmap getImagen() {
        return imagen;
    }

    /**
     * Establece la imagen del artículo.
     *
     * @param imagen La imagen del artículo en formato {@link Bitmap}.
     */
    public void setImagen(Bitmap imagen) {
        this.imagen = imagen;
    }

    /**
     * Obtiene la categoría del artículo.
     *
     * @return La categoría del artículo.
     */
    public String getCategoria() {
        return categoria;
    }

    /**
     * Establece la categoría del artículo.
     *
     * @param categoria La categoría del artículo.
     */
    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    /**
     * Obtiene el precio del artículo.
     *
     * @return El precio del artículo.
     */
    public String getPrecio() {
        return precio;
    }

    /**
     * Establece el precio del artículo.
     *
     * @param precio El precio del artículo.
     */
    public void setPrecio(String precio) {
        this.precio = precio;
    }

    /**
     * Obtiene el stock del artículo.
     *
     * @return El stock del artículo.
     */
    public String getStock() {
        return stock;
    }

    /**
     * Establece el stock del artículo.
     *
     * @param stock El stock del artículo.
     */
    public void setStock(String stock) {
        this.stock = stock;
    }
}
