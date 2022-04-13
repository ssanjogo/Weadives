package com.example.weadives;

import java.util.UUID;

public class User {

    private String nombre, correo, userid;

    public User(String nombre, String correo, String contraseña, String userid) {
        this.nombre = nombre;
        this.correo = correo;
        this.userid = userid;
    }

    //GETTERS AND SETTERS
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

}
