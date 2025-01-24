package com.example.reto_1_2_sqlite.modelos;

import android.graphics.Bitmap;

import java.io.Serializable;

public class LineaPedido implements Serializable {
    private String nombreArticulo;
    private int id, articuloId, cantidad, cabPedidoId, numeroLinea;
    private float precio;
    private Bitmap image;

    public LineaPedido( int articuloId, int cantidad, float precio, int cabPedidoId, String nombreArticulo, int numeroLinea, Bitmap image) {
        this.numeroLinea = numeroLinea;
        this.articuloId = articuloId;
        this.cantidad = cantidad;
        this.precio = precio;
        this.cabPedidoId = cabPedidoId;
        this.nombreArticulo = nombreArticulo;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getArticuloId() {
        return articuloId;
    }

    public void setArticuloId(int articuloId) {
        this.articuloId = articuloId;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public float getPrecio() {
        return precio;
    }

    public int getCabPedidoId() {
        return cabPedidoId;
    }

    public void setCabPedidoId(int cabPedidoId) {
        this.cabPedidoId = cabPedidoId;
    }

    public String getNombreArticulo() {
        return nombreArticulo;
    }

    public void setNombreArticulo(String nombreArticulo) {
        this.nombreArticulo = nombreArticulo;
    }

    public void setPrecio(float precio) {
        this.precio = precio;
    }

    public int getNumeroLinea() {
        return numeroLinea;
    }

    public void setNumeroLinea(int numeroLinea) {
        this.numeroLinea = numeroLinea;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }
}
