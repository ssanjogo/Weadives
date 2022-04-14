package com.example.weadives.AreaUsuario;

public class UserClass {
    private String id,username,correo,urlImg;
    private int solicitudRecibida;
    public UserClass(String id, String username, String correo, String urlImg) {
        this.id = id;
        this.username = username;
        this.correo = correo;
        this.urlImg = urlImg;
        this.solicitudRecibida =0;
    }

    public UserClass(String id, String username, String correo) {
        this.id = id;
        this.username = username;
        this.correo = correo;
        this.urlImg="NULL";
        this.solicitudRecibida =0;
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
        }else{
        return urlImg;}
    }

    public void setUrlImg(String urlImg) {
        this.urlImg = urlImg;
    }
}
