package com.example.reto_1_2_sqlite;

public class Visita {
    private int id;
    private int usuarioId;
    private String partnerName;
    private String fechaVisita;
    private String direccion;

    public Visita (int id, int usuarioId, String partnerName, String fechaVisita, String direccion) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.partnerName = partnerName;
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

    public String getPartnerName() {
        return partnerName;
    }

    public void setPartnerId(String partnerName) {
        this.partnerName = partnerName;
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
