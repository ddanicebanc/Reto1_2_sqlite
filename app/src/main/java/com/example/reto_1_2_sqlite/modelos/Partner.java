package com.example.reto_1_2_sqlite.modelos;

public class Partner {
    private int id, usuarioId, telefono;
    private String nombre, direccion, poblacion, email;

    public Partner(int id, int usuarioId, int telefono, String nombre, String direccion, String poblacion, String email) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.telefono = telefono;
        this.nombre = nombre;
        this.direccion = direccion;
        this.poblacion = poblacion;
        this.email = email;
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

    public int getTelefono() {
        return telefono;
    }

    public void setTelefono(int telefono) {
        this.telefono = telefono;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getPoblacion() {
        return poblacion;
    }

    public void setPoblacion(String poblacion) {
        this.poblacion = poblacion;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
