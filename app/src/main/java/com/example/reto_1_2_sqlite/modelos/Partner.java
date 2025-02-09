package com.example.reto_1_2_sqlite.modelos;

/**
 * La clase {@code Partner} representa a un partner o socio comercial en el sistema.
 * Contiene información sobre el ID, ID del usuario asociado, teléfono, nombre, dirección, población y email.
 */
public class Partner {
    private int id, usuarioId, telefono;
    private String nombre, direccion, poblacion, email;

    /**
     * Constructor de la clase {@code Partner}.
     *
     * @param id        El ID del partner.
     * @param usuarioId El ID del usuario asociado al partner.
     * @param telefono  El número de teléfono del partner.
     * @param nombre    El nombre del partner.
     * @param direccion La dirección del partner.
     * @param poblacion La población del partner.
     * @param email     El email del partner.
     */
    public Partner(int id, int usuarioId, int telefono, String nombre, String direccion, String poblacion, String email) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.telefono = telefono;
        this.nombre = nombre;
        this.direccion = direccion;
        this.poblacion = poblacion;
        this.email = email;
    }

    /**
     * Obtiene el ID del partner.
     *
     * @return El ID del partner.
     */
    public int getId() {
        return id;
    }

    /**
     * Establece el ID del partner.
     *
     * @param id El ID del partner.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Obtiene el ID del usuario asociado al partner.
     *
     * @return El ID del usuario.
     */
    public int getUsuarioId() {
        return usuarioId;
    }

    /**
     * Establece el ID del usuario asociado al partner.
     *
     * @param usuarioId El ID del usuario.
     */
    public void setUsuarioId(int usuarioId) {
        this.usuarioId = usuarioId;
    }

    /**
     * Obtiene el número de teléfono del partner.
     *
     * @return El número de teléfono.
     */
    public int getTelefono() {
        return telefono;
    }

    /**
     * Establece el número de teléfono del partner.
     *
     * @param telefono El número de teléfono.
     */
    public void setTelefono(int telefono) {
        this.telefono = telefono;
    }

    /**
     * Obtiene el nombre del partner.
     *
     * @return El nombre del partner.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre del partner.
     *
     * @param nombre El nombre del partner.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Obtiene la dirección del partner.
     *
     * @return La dirección del partner.
     */
    public String getDireccion() {
        return direccion;
    }

    /**
     * Establece la dirección del partner.
     *
     * @param direccion La dirección del partner.
     */
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    /**
     * Obtiene la población del partner.
     *
     * @return La población del partner.
     */
    public String getPoblacion() {
        return poblacion;
    }

    /**
     * Establece la población del partner.
     *
     * @param poblacion La población del partner.
     */
    public void setPoblacion(String poblacion) {
        this.poblacion = poblacion;
    }

    /**
     * Obtiene el email del partner.
     *
     * @return El email del partner.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Establece el email del partner.
     *
     * @param email El email del partner.
     */
    public void setEmail(String email) {
        this.email = email;
    }
}