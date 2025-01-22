package com.example.reto_1_2_sqlite.modelos;

import java.io.Serializable;

public class User implements Serializable {
    private String name;
    private int id;

    public User (String name, int id) {
        this.name = name;
        this.id = id;
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
}
