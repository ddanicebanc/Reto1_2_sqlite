package com.example.reto_1_2_sqlite.modelos;

/**
 * La clase {@code LinPedido} representa una línea individual dentro de un pedido.
 * Contiene información sobre el ID de la línea, el ID del pedido al que pertenece,
 * el ID del artículo, el ID de la delegación, la cantidad y el precio.
 */
public class LinPedido {
    private int idLinea, idPedido, idArticulo, idDelegacion, cantidad;
    private float precio;

    /**
     * Constructor de la clase {@code LinPedido}.
     *
     * @param idLinea    El ID de la línea de pedido.
     * @param idPedido   El ID del pedido al que pertenece esta línea.
     * @param idArticulo El ID del artículo en esta línea.
     * @param idDelegacion El ID de la delegación asociada a esta línea.
     * @param cantidad   La cantidad del artículo en esta línea.
     * @param precio     El precio del artículo en esta línea.
     */
    public LinPedido(int idLinea, int idPedido, int idArticulo, int idDelegacion, int cantidad, float precio) {
        this.idLinea = idLinea;
        this.idPedido = idPedido;
        this.idArticulo = idArticulo;
        this.idDelegacion = idDelegacion;
        this.cantidad = cantidad;
        this.precio = precio;
    }

    /**
     * Obtiene el ID de la línea de pedido.
     *
     * @return El ID de la línea de pedido.
     */
    public int getIdLinea() {
        return idLinea;
    }

    /**
     * Establece el ID de la línea de pedido.
     *
     * @param idLinea El ID de la línea de pedido.
     */
    public void setIdLinea(int idLinea) {
        this.idLinea = idLinea;
    }

    /**
     * Obtiene el ID del pedido al que pertenece esta línea.
     *
     * @return El ID del pedido.
     */
    public int getIdPedido() {
        return idPedido;
    }

    /**
     * Establece el ID del pedido al que pertenece esta línea.
     *
     * @param idPedido El ID del pedido.
     */
    public void setIdPedido(int idPedido) {
        this.idPedido = idPedido;
    }

    /**
     * Obtiene el ID del artículo en esta línea.
     *
     * @return El ID del artículo.
     */
    public int getIdArticulo() {
        return idArticulo;
    }

    /**
     * Establece el ID del artículo en esta línea.
     *
     * @param idArticulo El ID del artículo.
     */
    public void setIdArticulo(int idArticulo) {
        this.idArticulo = idArticulo;
    }

    /**
     * Obtiene el ID de la delegación asociada a esta línea.
     *
     * @return El ID de la delegación.
     */
    public int getIdDelegacion() {
        return idDelegacion;
    }

    /**
     * Establece el ID de la delegación asociada a esta línea.
     *
     * @param idDelegacion El ID de la delegación.
     */
    public void setIdDelegacion(int idDelegacion) {
        this.idDelegacion = idDelegacion;
    }

    /**
     * Obtiene la cantidad del artículo en esta línea.
     *
     * @return La cantidad del artículo.
     */
    public int getCantidad() {
        return cantidad;
    }

    /**
     * Establece la cantidad del artículo en esta línea.
     *
     * @param cantidad La cantidad del artículo.
     */
    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    /**
     * Obtiene el precio del artículo en esta línea.
     *
     * @return El precio del artículo.
     */
    public float getPrecio() {
        return precio;
    }

    /**
     * Establece el precio del artículo en esta línea.
     *
     * @param precio El precio del artículo.
     */
    public void setPrecio(float precio) {
        this.precio = precio;
    }
}