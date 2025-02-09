package com.example.reto_1_2_sqlite.modelos;

public class LinPedido {
    private int idLinea, idPedido, idArticulo, idDelegacion, cantidad;
    private float precio;

    public LinPedido(int idLinea, int idPedido, int idArticulo, int idDelegacion, int cantidad, float precio) {
        this.idLinea = idLinea;
        this.idPedido = idPedido;
        this.idArticulo = idArticulo;
        this.idDelegacion = idDelegacion;
        this.cantidad = cantidad;
        this.precio = precio;
    }

    public int getIdLinea() {
        return idLinea;
    }

    public void setIdLinea(int idLinea) {
        this.idLinea = idLinea;
    }

    public int getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(int idPedido) {
        this.idPedido = idPedido;
    }

    public int getIdArticulo() {
        return idArticulo;
    }

    public void setIdArticulo(int idArticulo) {
        this.idArticulo = idArticulo;
    }

    public int getIdDelegacion() {
        return idDelegacion;
    }

    public void setIdDelegacion(int idDelegacion) {
        this.idDelegacion = idDelegacion;
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

    public void setPrecio(float precio) {
        this.precio = precio;
    }
}