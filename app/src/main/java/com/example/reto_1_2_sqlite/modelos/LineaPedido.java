package com.example.reto_1_2_sqlite.modelos;

import java.io.Serializable;

public class LineaPedido implements Serializable {
    private int id, articuloId, cantidad, cabPedidoId;
    private float precio;

    public LineaPedido( int articuloId, int cantidad, float precio, int cabPedidoId) {
        this.articuloId = articuloId;
        this.cantidad = cantidad;
        this.precio = precio;
        this.cabPedidoId = cabPedidoId;
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

    public void setPrecio(int precio) {
        this.precio = precio;
    }

    public int getCabPedidoId() {
        return cabPedidoId;
    }

    public void setCabPedidoId(int cabPedidoId) {
        this.cabPedidoId = cabPedidoId;
    }
}
