package com.example.proyectoinventario;

import java.io.Serializable;

public class ProductoInventario implements Serializable {

    int idInventario;
    int idProducto;
    float precio;
    int facing;
    boolean promo;
    boolean exposicion;
    boolean rotura;
    String observaciones;


    public ProductoInventario(int idInventario, int idProducto, float precio, int facing, boolean promo, boolean exposicion, boolean rotura, String observaciones) {
        this.idInventario = idInventario;
        this.idProducto = idProducto;
        this.precio = precio;
        this.facing = facing;
        this.promo = promo;
        this.exposicion = exposicion;
        this.rotura = rotura;
        this.observaciones = observaciones;
    }

    public ProductoInventario(){

    }

    public int getIdInventario() {
        return idInventario;
    }

    public void setIdInventario(int idInventario) {
        this.idInventario = idInventario;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public float getPrecio() {
        return precio;
    }

    public void setPrecio(float precio) {
        this.precio = precio;
    }

    public int getFacing() {
        return facing;
    }

    public void setFacing(int facing) {
        this.facing = facing;
    }

    public boolean isPromo() {
        return promo;
    }

    public void setPromo(boolean promo) {
        this.promo = promo;
    }

    public boolean isExposicion() {
        return exposicion;
    }

    public void setExposicion(boolean exposicion) {
        this.exposicion = exposicion;
    }

    public boolean isRotura() {
        return rotura;
    }

    public void setRotura(boolean rotura) {
        this.rotura = rotura;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

}
