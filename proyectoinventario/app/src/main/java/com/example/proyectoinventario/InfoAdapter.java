package com.example.proyectoinventario;

public class InfoAdapter {

    public String id;
    public String nombre;
    public String superName;
    public int color;
    public int colorTextID;
    public int colorTextName;

    public int colorSuper;


    public InfoAdapter(String id, String nombre, int color, int colorTextID, int colorTextName) {
        this.id = id;
        this.nombre = nombre;
        this.color = color;
        this.colorTextID = colorTextID;
        this.colorTextName = colorTextName;
    }

    public InfoAdapter(String id, String nombre, String superName, int color, int colorTextID, int colorTextName) {
        this.id = id;
        this.nombre = nombre;
        this.superName = superName;
        this.color = color;
        this.colorTextID = colorTextID;
        this.colorTextName = colorTextName;
    }

    public InfoAdapter(String id, String nombre, String superName, int color, int colorTextID, int colorTextName, int colorSuper) {
        this.id = id;
        this.nombre = nombre;
        this.superName = superName;
        this.color = color;
        this.colorTextID = colorTextID;
        this.colorTextName = colorTextName;
        this.colorSuper = colorSuper;
    }

    public String getId() {
        return id;
    }


    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getColorTextID() {
        return colorTextID;
    }

    public void setColorTextID(int colorTextID) {
        this.colorTextID = colorTextID;
    }

    public int getColorTextName() {
        return colorTextName;
    }

    public void setColorTextName(int colorTextName) {
        this.colorTextName = colorTextName;
    }

    public String getSuperName() {
        return superName;
    }

    public void setSuperName(String superName) {
        this.superName = superName;
    }

    public int getColorSuper() {
        return colorSuper;
    }

    public void setColorSuper(int colorSuper) {
        this.colorSuper = colorSuper;
    }
}
