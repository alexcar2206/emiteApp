package com.example.proyectoinventario;

import java.io.Serializable;

public class User implements Serializable {


    public int id;
    private String email;
    private String password;

    private int login;


    public User(String email, String password, int login) {
        this.email = email;
        this.password = password;
        this.login = login;
    }

    public User() {

    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getLogin() {
        return login;
    }

    public void setLogin(int login) {
        this.login = login;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
