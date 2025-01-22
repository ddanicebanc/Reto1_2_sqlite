package com.example.reto_1_2_sqlite.modelos;

public class CabeceraPedido {
    private int id, usuarioId, delegacionId;
    private String fechaPedido, fechaEnvio, fechaPago;

    //Constructor
    public CabeceraPedido(int id, int usuarioId, int delegacionId, String fechaPedido, String fechaEnvio, String fechaPago) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.delegacionId = delegacionId;
        this.fechaPedido = fechaPedido;
        this.fechaEnvio = fechaEnvio;
        this.fechaPago = fechaPago;
    }

    //Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(int usuarioId) {
        this.usuarioId = usuarioId;
    }

    public int getDelegacionId() {
        return delegacionId;
    }

    public void setDelegacionId(int delegacionId) {
        this.delegacionId = delegacionId;
    }

    public String getFechaPedido() {
        return fechaPedido;
    }

    public void setFechaPedido(String fechaPedido) {
        this.fechaPedido = fechaPedido;
    }

    public String getFechaEnvio() {
        return fechaEnvio;
    }

    public void setFechaEnvio(String fechaEnvio) {
        this.fechaEnvio = fechaEnvio;
    }

    public String getFechaPago() {
        return fechaPago;
    }

    public void setFechaPago(String fechaPago) {
        this.fechaPago = fechaPago;
    }
}
