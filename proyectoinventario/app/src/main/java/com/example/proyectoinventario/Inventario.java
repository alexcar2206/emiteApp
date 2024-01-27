package com.example.proyectoinventario;

import java.io.Serializable;

public class Inventario implements Serializable {


    int id;
    int id_super;
    String fecha;
    String incio;
    String fin;

    int pendiente;

    String obsFin;

    int idUser;



    public Inventario(int id, int id_super, String fecha, String incio, String fin, int pendiente, String obsFin, int idUser) {
        this.id = id;
        this.id_super = id_super;
        this.fecha = fecha;
        this.incio = incio;
        this.fin = fin;
        this.pendiente = pendiente;
        this.obsFin = obsFin;
        this.idUser = idUser;
    }

    public Inventario() {

    }

    public int getId_super() {
        return id_super;
    }

    public void setId_super(int id_super) {
        this.id_super = id_super;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getIncio() {
        return incio;
    }

    public void setIncio(String incio) {
        this.incio = incio;
    }

    public String getFin() {
        return fin;
    }

    public void setFin(String fin) {
        this.fin = fin;
    }

    public int getPendiente() {
        return pendiente;
    }

    public void setPendiente(int pendiente) {
        this.pendiente = pendiente;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getObsFin() {
        return obsFin;
    }

    public void setObsFin(String obsFin) {
        this.obsFin = obsFin;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }
}
