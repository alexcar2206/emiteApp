package com.example.proyectoinventario;

import java.io.Serializable;

public class Familia implements Serializable {

    int id;
    String nombre;

    int[] idSubfamilias;

    int[] idProductos;



    public Familia(int id, String nombre, int[] idSubfamilias, int[] idProductos) {
        this.id = id;
        this.nombre = nombre;
        this.idSubfamilias = idSubfamilias;
        this.idProductos = idProductos;
    }

    public Familia(){

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

    public int[] getIdSubfamilias() {
        return idSubfamilias;
    }

    public void setIdSubfamilias(int[] idSubfamilias) {
        this.idSubfamilias = idSubfamilias;
    }

    public int[] getIdProductos() {
        return idProductos;
    }

    public void setIdProductos(int[] idProductos) {
        this.idProductos = idProductos;
    }
}
