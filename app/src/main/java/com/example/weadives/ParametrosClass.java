package com.example.weadives;

public class ParametrosClass {
    private String nombreActividad;
    private float vientoMax;
    private float vientoMin;

    private float presionMax;
    private float presionMin;

    private float alturaOlaMax;
    private float alturaOlaMin;

    private float temperaturaMax;
    private float temperaturaMin;

    private int userID;

    public ParametrosClass(String nombreActividad,int userID, float vientoMax, float vientoMin, float presionMax, float presionMin, float alturaOlaMax, float alturaOlaMin, float temperaturaMax, float temperaturaMin) {
        this.nombreActividad = nombreActividad;
        this.vientoMax = vientoMax;
        this.vientoMin = vientoMin;
        this.presionMax = presionMax;
        this.presionMin = presionMin;
        this.alturaOlaMax = alturaOlaMax;
        this.alturaOlaMin = alturaOlaMin;
        this.temperaturaMax = temperaturaMax;
        this.temperaturaMin = temperaturaMin;
        this.userID=userID;
    }

    public ParametrosClass(String nombreActividad,int userID, float viento, float presion, float alturaOla, float temperatura) {
        this.nombreActividad = nombreActividad;
        this.vientoMax = viento;
        this.vientoMin = viento;
        this.presionMax = presion;
        this.presionMin = presion;
        this.alturaOlaMax = alturaOla;
        this.alturaOlaMin = alturaOla;
        this.temperaturaMax = temperatura;
        this.temperaturaMin = temperatura;
        this.userID=userID;
    }
    @Override
    public String toString() {
        return "ParametrosClass{" +
                "nombreActividad='" + nombreActividad + '\'' +
                ", vientoMax=" + vientoMax +
                ", vientoMin=" + vientoMin +
                ", presionMax=" + presionMax +
                ", presionMin=" + presionMin +
                ", alturaOlaMax=" + alturaOlaMax +
                ", alturaOlaMin=" + alturaOlaMin +
                ", temperaturaMax=" + temperaturaMax +
                ", temperaturaMin=" + temperaturaMin +
                ", userID=" + userID +
                '}';
    }

    public float getViento() {
        return (vientoMax+vientoMin)/2;
    }

    public String getNombreActividad() {
        return nombreActividad;
    }

    public void setNombreActividad(String nombreActividad) {
        this.nombreActividad = nombreActividad;
    }


    public float getVientoMax() {
        return vientoMax;
    }

    public void setVientoMax(float vientoMax) {
        this.vientoMax = vientoMax;
    }

    public float getVientoMin() {
        return vientoMin;
    }

    public void setVientoMin(float vientoMin) {
        this.vientoMin = vientoMin;
    }

    public float getPresionMax() {
        return presionMax;
    }

    public void setPresionMax(float presionMax) {
        this.presionMax = presionMax;
    }

    public float getPresionMin() {
        return presionMin;
    }

    public void setPresionMin(float presionMin) {
        this.presionMin = presionMin;
    }

    public float getAlturaOlaMax() {
        return alturaOlaMax;
    }

    public void setAlturaOlaMax(float alturaOlaMax) {
        this.alturaOlaMax = alturaOlaMax;
    }

    public float getAlturaOlaMin() {
        return alturaOlaMin;
    }

    public void setAlturaOlaMin(float alturaOlaMin) {
        this.alturaOlaMin = alturaOlaMin;
    }

    public float getTemperaturaMax() {
        return temperaturaMax;
    }

    public void setTemperaturaMax(float temperaturaMax) {
        this.temperaturaMax = temperaturaMax;
    }

    public float getTemperaturaMin() {
        return temperaturaMin;
    }

    public void setTemperaturaMin(float temperaturaMin) {
        this.temperaturaMin = temperaturaMin;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }
}
