package com.example.reto_1_2_sqlite.modelos;

import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * La clase {@code LineaPedido} representa una línea de pedido individual dentro de un pedido.
 * Contiene información sobre el artículo, cantidad, precio, la cabecera del pedido a la que pertenece,
 * el número de línea y la imagen del artículo.
 * Esta clase implementa {@link Serializable} para poder ser pasada entre actividades.
 */
public class LineaPedido implements Serializable {
    private String nombreArticulo;
    private int id, articuloId, cantidad, cabPedidoId, numeroLinea;
    private float precio;
    private Bitmap image;

    /**
     * Constructor de la clase {@code LineaPedido}.
     *
     * @param articuloId      El ID del artículo.
     * @param cantidad        La cantidad del artículo en esta línea de pedido.
     * @param precio          El precio unitario del artículo en esta línea de pedido.
     * @param cabPedidoId     El ID de la cabecera del pedido al que pertenece esta línea.
     * @param nombreArticulo  El nombre del artículo.
     * @param numeroLinea     El número de línea de esta línea de pedido dentro del pedido.
     * @param image           La imagen del artículo en formato {@link Bitmap}.
     */
    public LineaPedido(int articuloId, int cantidad, float precio, int cabPedidoId, String nombreArticulo, int numeroLinea, Bitmap image) {
        this.numeroLinea = numeroLinea;
        this.articuloId = articuloId;
        this.cantidad = cantidad;
        this.precio = precio;
        this.cabPedidoId = cabPedidoId;
        this.nombreArticulo = nombreArticulo;
        this.image = image;
    }

    /**
     * Obtiene el ID de la línea de pedido.
     * @return El ID de la línea de pedido.
     */
    public int getId() {
        return id;
    }

    /**
     * Establece el ID de la línea de pedido.
     * @param id El ID de la línea de pedido.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Obtiene el ID del artículo.
     * @return El ID del artículo.
     */
    public int getArticuloId() {
        return articuloId;
    }

    /**
     * Establece el ID del artículo.
     * @param articuloId El ID del artículo.
     */
    public void setArticuloId(int articuloId) {
        this.articuloId = articuloId;
    }

    /**
     * Obtiene la cantidad del artículo.
     * @return La cantidad del artículo.
     */
    public int getCantidad() {
        return cantidad;
    }

    /**
     * Establece la cantidad del artículo.
     * @param cantidad La cantidad del artículo.
     */
    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    /**
     * Obtiene el precio unitario del artículo.
     * @return El precio unitario del artículo.
     */
    public float getPrecio() {
        return precio;
    }

    /**
     * Obtiene el ID de la cabecera del pedido.
     * @return El ID de la cabecera del pedido.
     */
    public int getCabPedidoId() {
        return cabPedidoId;
    }

    /**
     * Establece el ID de la cabecera del pedido.
     * @param cabPedidoId El ID de la cabecera del pedido.
     */
    public void setCabPedidoId(int cabPedidoId) {
        this.cabPedidoId = cabPedidoId;
    }

    /**
     * Obtiene el nombre del artículo.
     * @return El nombre del artículo.
     */
    public String getNombreArticulo() {
        return nombreArticulo;
    }

    /**
     * Establece el nombre del artículo.
     * @param nombreArticulo El nombre del artículo.
     */
    public void setNombreArticulo(String nombreArticulo) {
        this.nombreArticulo = nombreArticulo;
    }

    /**
     * Establece el precio unitario del artículo.
     * @param precio El precio unitario del artículo.
     */
    public void setPrecio(float precio) {
        this.precio = precio;
    }

    /**
     * Obtiene el número de línea.
     * @return El número de línea.
     */
    public int getNumeroLinea() {
        return numeroLinea;
    }

    /**
     * Establece el número de línea.
     * @param numeroLinea El número de línea.
     */
    public void setNumeroLinea(int numeroLinea) {
        this.numeroLinea = numeroLinea;
    }

    /**
     * Obtiene la imagen del artículo.
     * @return La imagen del artículo.
     */
    public Bitmap getImage() {
        return image;
    }

    /**
     * Establece la imagen del artículo.
     * @param image La imagen del artículo.
     */
    public void setImage(Bitmap image) {
        this.image = image;
    }
}