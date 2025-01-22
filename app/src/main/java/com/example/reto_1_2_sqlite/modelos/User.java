package com.example.reto_1_2_sqlite.modelos;

import java.io.Serializable;

public class User implements Serializable {
    private String name;
    private int id, delegationId;

    public User (String name, int id, int delegationId) {
        this.name = name;
        this.id = id;
        this.delegationId = delegationId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDelegationId() { return delegationId;}

    public void setDelegationId(int delegationId) {this.delegationId = delegationId;}
}
