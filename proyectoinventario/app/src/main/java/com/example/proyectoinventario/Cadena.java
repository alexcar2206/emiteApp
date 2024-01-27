package com.example.proyectoinventario;

import java.util.UUID;

public class Cadena {
    int id;
    String nombre;

    public Cadena(String nombre) {
        this.id =  Integer.parseInt(UUID.randomUUID().toString()) ;
        this.nombre = nombre;
    }

    public  Cadena(){

    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
