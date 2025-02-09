package com.example.reto_1_2_sqlite.modelos;

/**
 * La clase {@code Visita} representa una visita realizada a un partner.
 * Contiene información sobre el ID de la visita, el ID del usuario que realizó la visita,
 * el nombre del partner visitado, la fecha de la visita y la dirección de la visita.
 */
public class Visita {
    private int id;
    private int usuarioId;
    private String partnerName;
    private String fechaVisita;
    private String direccion;

    /**
     * Constructor de la clase {@code Visita}.
     *
     * @param id          El ID de la visita.
     * @param usuarioId   El ID del usuario que realizó la visita.
     * @param partnerName El nombre del partner visitado.
     * @param fechaVisita La fecha de la visita.
     * @param direccion   La dirección de la visita.
     */
    public Visita(int id, int usuarioId, String partnerName, String fechaVisita, String direccion) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.partnerName = partnerName;
        this.fechaVisita = fechaVisita;
        this.direccion = direccion;
    }

    /**
     * Obtiene el ID de la visita.
     *
     * @return El ID de la visita.
     */
    public int getId() {
        return id;
    }

    /**
     * Establece el ID de la visita.
     *
     * @param id El ID de la visita.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Obtiene el ID del usuario que realizó la visita.
     *
     * @return El ID del usuario.
     */
    public int getUsuarioId() {
        return usuarioId;
    }

    /**
     * Establece el ID del usuario que realizó la visita.
     *
     * @param usuarioId El ID del usuario.
     */
    public void setUsuarioId(int usuarioId) {
        this.usuarioId = usuarioId;
    }

    /**
     * Obtiene el nombre del partner visitado.
     *
     * @return El nombre del partner.
     */
    public String getPartnerName() {
        return partnerName;
    }

    /**
     * Establece el nombre del partner visitado.
     *
     * @param partnerName El nombre del partner.
     */
    public void setPartnerId(String partnerName) {
        this.partnerName = partnerName;
    }

    /**
     * Obtiene la fecha de la visita.
     *
     * @return La fecha de la visita.
     */
    public String getFechaVisita() {
        return fechaVisita;
    }

    /**
     * Establece la fecha de la visita.
     *
     * @param fechaVisita La fecha de la visita.
     */
    public void setFechaVisita(String fechaVisita) {
        this.fechaVisita = fechaVisita;
    }

    /**
     * Obtiene la dirección de la visita.
     *
     * @return La dirección de la visita.
     */
    public String getDireccion() {
        return direccion;
    }

    /**
     * Establece la dirección de la visita.
     *
     * @param direccion La dirección de la visita.
     */
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
}