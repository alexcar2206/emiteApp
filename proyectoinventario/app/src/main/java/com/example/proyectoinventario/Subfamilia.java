package com.example.proyectoinventario;

public class Subfamilia {

    int id;
    String nombre;
    int idFamilia;

    public Subfamilia(int id, String nombre, int idFamilia) {
        this.id = id;
        this.nombre = nombre;
        this.idFamilia = idFamilia;
    }

    public  Subfamilia(){

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

    public int getIdFamilia() {
        return idFamilia;
    }

    public void setIdFamilia(int idFamilia) {
        this.idFamilia = idFamilia;
    }
}
