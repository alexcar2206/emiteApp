package com.example.proyectoinventario;

import android.graphics.drawable.Drawable;

public class InfoAdapterInventario {

    public String textProd;
    public int imageCheck;
    public int visibility;


    public InfoAdapterInventario(String textProd, int imageCheck, int visibility) {
        this.textProd = textProd;
        this.imageCheck = imageCheck;
        this.visibility = visibility;
    }

    public String getTextProd() {
        return textProd;
    }

    public void setTextProd(String textProd) {
        this.textProd = textProd;
    }

    public int getImageCheck() {
        return imageCheck;
    }

    public void setImageCheck(int imageCheck) {
        this.imageCheck = imageCheck;
    }

    public int getVisibility() {
        return visibility;
    }

    public void setVisibility(int visibility) {
        this.visibility = visibility;
    }
}
