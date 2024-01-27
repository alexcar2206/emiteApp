package com.example.proyectoinventario;

import android.media.Image;

import java.io.Serializable;

public class Imagen implements Serializable {



    public static abstract class Objeto{

        public static final String INVENTARIO = "inventario";
        public static final String IMPLEMENTACION = "implementacion";
    }

    public static abstract class fotosFinales{
        public static final String SI = "1";
        public static final String NO = "0";
    }

    int idProducto;

    String objeto;
    int idObtejo;
    String imagen;
    String fotosFin;

    public Imagen(int idProducto, String objeto, int idObtejo, String imagen) {
        this.idProducto = idProducto;
        this.objeto = objeto;
        this.idObtejo = idObtejo;
        this.imagen = imagen;
    }

    public Imagen(){

    }

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }


    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getObjeto() {
        return objeto;
    }

    public void setObjeto(String objeto) {
        this.objeto = objeto;
    }

    public int getIdObtejo() {
        return idObtejo;
    }

    public void setIdObtejo(int idObtejo) {
        this.idObtejo = idObtejo;
    }

    public String getFotosFin() {
        return fotosFin;
    }

    public void setFotosFin(String fotosFin) {
        this.fotosFin = fotosFin;
    }
}
