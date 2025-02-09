package com.example.reto_1_2_sqlite.modelos;

import java.io.Serializable;

/**
 * La clase {@code User} representa a un usuario en el sistema.
 * Contiene información sobre el nombre, ID y el ID de la delegación del usuario.
 * Esta clase implementa {@link Serializable} para poder ser pasada entre actividades.
 */
public class User implements Serializable {
    private String name;
    private int id, delegationId;

    /**
     * Constructor de la clase {@code User}.
     *
     * @param name        El nombre del usuario.
     * @param id          El ID del usuario.
     * @param delegationId El ID de la delegación del usuario.
     */
    public User(String name, int id, int delegationId) {
        this.name = name;
        this.id = id;
        this.delegationId = delegationId;
    }

    /**
     * Obtiene el ID del usuario.
     *
     * @return El ID del usuario.
     */
    public int getId() {
        return id;
    }

    /**
     * Establece el ID del usuario.
     *
     * @param id El ID del usuario.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Obtiene el nombre del usuario.
     *
     * @return El nombre del usuario.
     */
    public String getName() {
        return name;
    }

    /**
     * Establece el nombre del usuario.
     *
     * @param name El nombre del usuario.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Obtiene el ID de la delegación del usuario.
     *
     * @return El ID de la delegación.
     */
    public int getDelegationId() {
        return delegationId;
    }

    /**
     * Establece el ID de la delegación del usuario.
     *
     * @param delegationId El ID de la delegación.
     */
    public void setDelegationId(int delegationId) {
        this.delegationId = delegationId;
    }
}