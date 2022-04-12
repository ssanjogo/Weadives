package com.example.weadives.PantallaRegistro;

import java.util.UUID;

public class User {

    private String nombre, correo, contraseña, userid;
    private String noteId;


    // Description, uri, duration, format, owner
    public User(String nombre, String correo, String contraseña, String userid) {
        this.nombre = nombre;
        this.correo = correo;
        this.contraseña = contraseña;
        this.userid = userid;
        UUID uuid = UUID.randomUUID();
        this.noteId = uuid.toString();
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

    public String getContraseña() {
        return contraseña;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getNoteId() {
        return noteId;
    }

    public void setNoteId(String noteId) {
        this.noteId = noteId;
    }

}
