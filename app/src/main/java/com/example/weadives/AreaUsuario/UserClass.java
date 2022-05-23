package com.example.weadives.AreaUsuario;

import com.example.weadives.DatabaseAdapter;
import com.example.weadives.PantallaPerfilAmigo.PublicacionClass;
import com.example.weadives.ParametrosClass;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class UserClass {

    private String id, username, correo, urlImg, amigos, solicitudes_recibidas, solicitudes_enviadas;
    private int solicitudRecibida = 0;
    private List<PublicacionClass> publicacionList;


    public UserClass(String id, String username, String correo, String urlImg, String amigos, String solicitudes_recibidas, String solicitudes_enviadas) {
        this.id = id;
        this.username = username;
        this.correo = correo;
        if (urlImg == ""){
            this.urlImg = "https://www.pngmart.com/files/21/Account-User-PNG-Photo.png";
        } else {
            this.urlImg = urlImg;
        }
        this.amigos = amigos;
        this.solicitudes_recibidas = solicitudes_recibidas;
        this.solicitudes_enviadas = solicitudes_enviadas;
    }
    //Hay que usar este
    public UserClass(String id, String username, String correo, String urlImg, String amigos, String solicitudes_recibidas, String solicitudes_enviadas,List<PublicacionClass> pl) {
        this.id = id;
        this.username = username;
        this.correo = correo;
        if (urlImg == ""){
            this.urlImg = "https://www.pngmart.com/files/21/Account-User-PNG-Photo.png";
        } else {
            this.urlImg = urlImg;
        }
        this.amigos = amigos;
        this.solicitudes_recibidas = solicitudes_recibidas;
        this.solicitudes_enviadas = solicitudes_enviadas;
        this.publicacionList=pl;
    }

    public List<PublicacionClass> getPublicacionList() {
        return publicacionList;
    }

    public void setPublicacionList(List<PublicacionClass> publicacionList) {
        this.publicacionList = publicacionList;
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
        if (urlImg == null){
            return "https://www.kindpng.com/picc/m/24-248253_user-profile-default-image-png-clipart-png-download.png";
        } else {
            return urlImg;
        }
    }

    public void setUrlImg(String urlImg) {
        this.urlImg = urlImg;
    }

    public String getStringAmigos(){
        return this.amigos;
    }

    public void setStringAmigos(String amigos){
        this.amigos = amigos;
    }

    public List<String> getListaAmigos(){
        List<String> amigos = Arrays.asList(this.amigos.split(","));
        return amigos;
    }

    public List<String> getListaSolicitudesRecibidas(){
        List<String> solicitudes_recibidas = Arrays.asList(this.solicitudes_recibidas.split(","));
        System.out.println(solicitudes_recibidas);
        return solicitudes_recibidas;
    }

    public String getStringSolicitudesRecibidas(){
        return this.solicitudes_recibidas;
    }

    public void setStringSolicitudesRecibidas(String solR){
        this.solicitudes_recibidas = solR;
    }

    public List<String> getListaSolicitudesEnviadas(){
        List<String> solicitudes_enviadas = Arrays.asList(this.solicitudes_enviadas.split(","));
        return solicitudes_enviadas;
    }

    public String getStringSolicitudesEnviadas(){
        return this.solicitudes_enviadas;
    }

    public void setStringSolicitudesEnviadas(String solE){
        this.solicitudes_enviadas = solE;
    }

    @Override
    public String toString() {
        return "UserClass{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", correo='" + correo + '\'' +
                ", urlImg='" + urlImg + '\'' +
                ", amigos='" + amigos + '\'' +
                ", solicitudes_recibidas='" + solicitudes_recibidas + '\'' +
                ", solicitudes_enviadas='" + solicitudes_enviadas + '\'' +
                '}';
    }
}
