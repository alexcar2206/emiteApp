package com.example.proyectoinventario;

import java.io.Serializable;

public class Super implements Serializable {

    int id;
    int cluster_id;
    String nombre;
    String localizacion;

    double coorX;
    double coorY;
    String direccion;
    String provincia;
    String ciudad;

    int codCliente;
    String personaContacto;
    String email;
    int telefono;

    String frecuencia;
    int visitasAno;
    String observaciones;


    public Super(int id, int cluster_id, String nombre, String localizacion, double coorX, double coorY, String direccion, String provincia, String ciudad, String personaContacto, String email, int telefono, String observaciones) {
        this.id = id;
        this.cluster_id = cluster_id;
        this.nombre = nombre;
        this.localizacion = localizacion;
        this.coorX = coorX;
        this.coorY = coorY;
        this.direccion = direccion;
        this.provincia = provincia;
        this.ciudad = ciudad;
        this.personaContacto = personaContacto;
        this.email = email;
        this.telefono = telefono;
        this.observaciones = observaciones;
    }

    public Super (){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCluster_id() {
        return cluster_id;
    }

    public void setCluster_id(int cluster_id) {
        this.cluster_id = cluster_id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getLocalizacion() {
        return localizacion;
    }

    public void setLocalizacion(String localizacion) {
        this.localizacion = localizacion;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getPersonaContacto() {
        return personaContacto;
    }

    public void setPersonaContacto(String personaContacto) {
        this.personaContacto = personaContacto;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getTelefono() {
        return telefono;
    }

    public void setTelefono(int telefono) {
        this.telefono = telefono;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public double getCoorX() {
        return coorX;
    }

    public void setCoorX(double coorX) {
        this.coorX = coorX;
    }

    public double getCoorY() {
        return coorY;
    }

    public void setCoorY(double coorY) {
        this.coorY = coorY;
    }

    public String getFrecuencia() {
        return frecuencia;
    }

    public void setFrecuencia(String frecuencia) {
        this.frecuencia = frecuencia;
    }

    public int getVisitasAno() {
        return visitasAno;
    }

    public void setVisitasAno(int visitasAno) {
        this.visitasAno = visitasAno;
    }

    public int getCodCliente() {
        return codCliente;
    }

    public void setCodCliente(int codCliente) {
        this.codCliente = codCliente;
    }
}
