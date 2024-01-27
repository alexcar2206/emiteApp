package com.example.proyectoinventario;

import java.io.Serializable;

public class  Cluster implements Serializable {

    int id;
    int cad_id;
    String nombre;

    public Cluster(int cad_id, String nombre) {
        this.cad_id = cad_id;
        this.nombre = nombre;
    }

    public Cluster(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCad_id() {
        return cad_id;
    }

    public void setCad_id(int cad_id) {
        this.cad_id = cad_id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
