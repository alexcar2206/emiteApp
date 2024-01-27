package com.example.proyectoinventario;

import java.io.Serializable;

public class Productos implements Serializable {

    int id;
    String nombre;
    int familiaID;
    int[] idCluster;


    public Productos(int id, String nombre, int familiaID, int[] idCluster) {
        this.id = id;
        this.nombre = nombre;
        this.familiaID = familiaID;
        this.idCluster = idCluster;
    }

    public Productos(){

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

    public int getFamiliaID() {
        return familiaID;
    }

    public void setFamiliaID(int familiaID) {
        this.familiaID = familiaID;
    }

    public int[] getIdCluster() {
        return idCluster;
    }

    public void setIdCluster(int[] idCluster) {
        this.idCluster = idCluster;
    }
}
