package com.example.reto_1_2_sqlite;

public class Visita {
    private int id;
    private int usuarioId;
    private int partnerId;
    private String fechaVisita;
    private String direccion;

    public Visita (int id, int usuarioId, int partnerId, String fechaVisita, String direccion) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.partnerId = partnerId;
        this.fechaVisita = fechaVisita;
        this.direccion = direccion;
    }

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

    public int getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(int partnerId) {
        this.partnerId = partnerId;
    }

    public String getFechaVisita() {
        return fechaVisita;
    }

    public void setFechaVisita(String fechaVisita) {
        this.fechaVisita = fechaVisita;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
}
