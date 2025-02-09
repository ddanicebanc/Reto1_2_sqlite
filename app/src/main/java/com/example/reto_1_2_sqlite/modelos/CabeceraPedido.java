package com.example.reto_1_2_sqlite.modelos;

import java.io.Serializable;

/**
 * La clase {@code CabeceraPedido} representa la cabecera de un pedido en el sistema.
 * Contiene información sobre el ID, usuario, delegación, fechas de pedido, envío y pago, y el partner asociado.
 * Esta clase implementa {@link Serializable} para poder ser pasada entre actividades.
 */
public class CabeceraPedido implements Serializable {
    private int id, usuarioId, delegacionId, partnerId;
    private String fechaPedido, fechaEnvio, fechaPago;

    /**
     * Constructor de la clase {@code CabeceraPedido}.
     *
     * @param id          El ID de la cabecera del pedido.
     * @param usuarioId   El ID del usuario que realizó el pedido.
     * @param delegacionId El ID de la delegación del usuario.
     * @param fechaPedido La fecha del pedido.
     * @param fechaEnvio  La fecha de envío del pedido.
     * @param fechaPago   La fecha de pago del pedido.
     * @param partnerId   El ID del partner asociado al pedido.
     */
    public CabeceraPedido(int id, int usuarioId, int delegacionId, String fechaPedido,
                          String fechaEnvio, String fechaPago, int partnerId) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.delegacionId = delegacionId;
        this.fechaPedido = fechaPedido;
        this.fechaEnvio = fechaEnvio;
        this.fechaPago = fechaPago;
        this.partnerId = partnerId;
    }

    /**
     * Obtiene el ID de la cabecera del pedido.
     *
     * @return El ID de la cabecera del pedido.
     */
    public int getId() {
        return id;
    }

    /**
     * Establece el ID de la cabecera del pedido.
     *
     * @param id El ID de la cabecera del pedido.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Obtiene el ID del usuario que realizó el pedido.
     *
     * @return El ID del usuario.
     */
    public int getUsuarioId() {
        return usuarioId;
    }

    /**
     * Establece el ID del usuario que realizó el pedido.
     *
     * @param usuarioId El ID del usuario.
     */
    public void setUsuarioId(int usuarioId) {
        this.usuarioId = usuarioId;
    }

    /**
     * Obtiene el ID de la delegación del usuario.
     *
     * @return El ID de la delegación.
     */
    public int getDelegacionId() {
        return delegacionId;
    }

    /**
     * Establece el ID de la delegación del usuario.
     *
     * @param delegacionId El ID de la delegación.
     */
    public void setDelegacionId(int delegacionId) {
        this.delegacionId = delegacionId;
    }

    /**
     * Obtiene la fecha del pedido.
     *
     * @return La fecha del pedido.
     */
    public String getFechaPedido() {
        return fechaPedido;
    }

    /**
     * Establece la fecha del pedido.
     *
     * @param fechaPedido La fecha del pedido.
     */
    public void setFechaPedido(String fechaPedido) {
        this.fechaPedido = fechaPedido;
    }

    /**
     * Obtiene la fecha de envío del pedido.
     *
     * @return La fecha de envío.
     */
    public String getFechaEnvio() {
        return fechaEnvio;
    }

    /**
     * Establece la fecha de envío del pedido.
     *
     * @param fechaEnvio La fecha de envío.
     */
    public void setFechaEnvio(String fechaEnvio) {
        this.fechaEnvio = fechaEnvio;
    }

    /**
     * Obtiene la fecha de pago del pedido.
     *
     * @return La fecha de pago.
     */
    public String getFechaPago() {
        return fechaPago;
    }

    /**
     * Establece la fecha de pago del pedido.
     *
     * @param fechaPago La fecha de pago.
     */
    public void setFechaPago(String fechaPago) {
        this.fechaPago = fechaPago;
    }

    /**
     * Obtiene el ID del partner asociado al pedido.
     *
     * @return El ID del partner.
     */
    public int getPartnerId() {
        return partnerId;
    }

    /**
     * Establece el ID del partner asociado al pedido.
     *
     * @param partnerId El ID del partner.
     */
    public void setPartnerId(int partnerId) {
        this.partnerId = partnerId;
    }
}