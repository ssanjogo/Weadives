package com.example.weadives.AreaUsuario;

import com.example.weadives.DatabaseAdapter;

import java.util.Arrays;
import java.util.List;

public class UserClass {

    private final DatabaseAdapter dbA = DatabaseAdapter.getInstance();
    private String id, username, correo, urlImg, amigos, solicitudes_recibidas, solicitudes_enviadas;
    private int solicitudRecibida = 0;


    public UserClass(String id, String username, String correo, String urlImg, String amigos, String solicitudes_recibidas, String solicitudes_enviadas) {
        this.id = id;
        this.username = username;
        this.correo = correo;
        this.urlImg = urlImg;
        this.amigos = amigos;
        this.solicitudes_recibidas = solicitudes_recibidas;
        this.solicitudes_enviadas = solicitudes_enviadas;
    }

    public UserClass(String id, String username, String correo) {
        this.id = id;
        this.username = username;
        this.correo = correo;
        this.urlImg = null;
        this.amigos = null;
        this.solicitudes_recibidas = null;
        this.solicitudes_enviadas = null;
    }

    public void sentSolicitud(){
        this.solicitudRecibida=1;
    }
    public int hasSolicitud(){
        return this.solicitudRecibida;
    }
    public void acceptSolicitud(){
        this.solicitudRecibida=0;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getUrlImg() {
        if (urlImg.equals("NULL")){
            return "https://www.kindpng.com/picc/m/24-248253_user-profile-default-image-png-clipart-png-download.png";
        } else {
            return urlImg;
        }
    }

    public void setUrlImg(String urlImg) {
        this.urlImg = urlImg;
    }

    public List<String> getListaAmigos(){
        List<String> amigos = Arrays.asList(this.amigos.split(","));
        return amigos;
    }

    public List<String> getListaSolicitudesRecibidas(){
        List<String> solicitudes_recibidas = Arrays.asList(this.solicitudes_recibidas.split(","));
        return solicitudes_recibidas;
    }

    public List<String> getListaSolicitudesEnviadas(){
        List<String> solicitudes_enviadas = Arrays.asList(this.solicitudes_enviadas.split(","));
        return solicitudes_enviadas;
    }

    public void saveUser(){
        dbA.addUser(this.username, this.correo, "");
    }
}
