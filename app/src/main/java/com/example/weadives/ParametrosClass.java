package com.example.weadives;

import java.util.ArrayList;

public class ParametrosClass {

    enum direction {
        NORTE,
        NORDESTE,
        ESTE,
        SUDESTE,
        SUD,
        SUDOESTE,
        OESTE,
        NORDOESTE,
        NO_DIRECTION
    }
    /*
        Presión
        Temperatura
        Viento(Valor absoluto)
        Dirección de viento(grados)
        Altura Ola
        Periodo Ola
        Dirección ola(Grados)
         */
    private String nombreActividad;
    private float presionMax;
    private float presionMin;

    private float temperaturaMax;
    private float temperaturaMin;

    private float vientoMax;
    private float vientoMin;

    private DatoGradosClass directionViento;

    private float alturaOlaMax;
    private float alturaOlaMin;

    private float periodoOlaMax;
    private float periodoOlaMin;

    private DatoGradosClass directionOlas;


    public ParametrosClass() {
        this.nombreActividad = "New";

    }



    public ParametrosClass(String nombreActividad, float presionMax, float presionMin, float temperaturaMax, float temperaturaMin, float vientoMax, float vientoMin, DatoGradosClass directionViento, float alturaOlaMax, float alturaOlaMin, float periodoOlaMax, float periodoOlaMin, DatoGradosClass directionOlas) {
        this.nombreActividad = nombreActividad;
        this.presionMax = presionMax;
        this.presionMin = presionMin;
        this.temperaturaMax = temperaturaMax;
        this.temperaturaMin = temperaturaMin;
        this.vientoMax = vientoMax;
        this.vientoMin = vientoMin;
        this.directionViento = directionViento;
        this.alturaOlaMax = alturaOlaMax;
        this.alturaOlaMin = alturaOlaMin;
        this.periodoOlaMax = periodoOlaMax;
        this.periodoOlaMin = periodoOlaMin;
        this.directionOlas = directionOlas;
    }



    @Override
    public String toString() {
        return this.nombreActividad;
    }
    public String toString2() {
        return "ParametrosClass{" +
                "nombreActividad='" + nombreActividad + '\'' +
                ", presionMax=" + presionMax +
                ", presionMin=" + presionMin +
                ", temperaturaMax=" + temperaturaMax +
                ", temperaturaMin=" + temperaturaMin +
                ", vientoMax=" + vientoMax +
                ", vientoMin=" + vientoMin +
                ", directionViento=" + directionViento +
                ", alturaOlaMax=" + alturaOlaMax +
                ", alturaOlaMin=" + alturaOlaMin +
                ", periodoOlaMax=" + periodoOlaMax +
                ", periodoOlaMin=" + periodoOlaMin +
                ", directionOlas=" + directionOlas +
                '}';
    }
    public String toSaveString() {
        return nombreActividad +
                "," + presionMax +
                "," + presionMin +
                "," + temperaturaMax +
                "," + temperaturaMin +
                "," + vientoMax +
                "," + vientoMin +
                "," + directionViento.getDir() +
                "," + alturaOlaMax +
                "," + alturaOlaMin +
                "," + periodoOlaMax +
                "," + periodoOlaMin +
                "," + directionOlas.getDir();
    }

    public float getViento() {
        return (vientoMax+vientoMin)/2;
    }
    public float getPresion() { return (presionMax+presionMin)/2;}
    public float getTemperatura() {
        return (temperaturaMax+temperaturaMin)/2;
    }
    public float getAlturaOla() {
        return (alturaOlaMax+alturaOlaMin)/2;
    }
    public float getPeriodoOla() {
        return (periodoOlaMax+periodoOlaMin)/2;
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

    public Directions getDirectionViento() {
        if (directionViento==null){
            return Directions.NO_DIRECTION;
        }
        return directionViento.getDir();
    }

    public void setDirectionViento(Directions directionViento) {
        this.directionViento.setDir(directionViento);
    }

    public float getPeriodoOlaMax() {
        return periodoOlaMax;
    }

    public void setPeriodoOlaMax(float periodoOlaMax) {
        this.periodoOlaMax = periodoOlaMax;
    }

    public float getPeriodoOlaMin() {
        return periodoOlaMin;
    }

    public void setPeriodoOlaMin(float periodoOlaMin) {
        this.periodoOlaMin = periodoOlaMin;
    }

    public Directions getDirectionOlas() {
        if (directionOlas==null){
            return Directions.NO_DIRECTION;
        }
        return directionOlas.getDir();
    }

    public void setDirectionOlas(Directions directionOlas) {
        this.directionOlas.setDir(directionOlas);
    }

    static ArrayList<ParametrosClass> descomprimir(String l){
        //System.out.println(l);
        String[] parametrosStringList = l.split("¿");
        int count = l.length() - l.replace("¿", "").length();
        //System.out.println(count);
        ArrayList<ParametrosClass> parametrosList = new ArrayList<>();
        String[] fixedParam;
        for (String i : parametrosStringList) {
            //System.out.println(i);
            fixedParam=i.split(",");
            for (String x : fixedParam) {
                //System.out.println(x);
            }

            parametrosList.add(new ParametrosClass(fixedParam[0], Float.parseFloat(fixedParam[1]),Float.parseFloat(fixedParam[2]),Float.parseFloat(fixedParam[3]),Float.parseFloat(fixedParam[4]),Float.parseFloat(fixedParam[5]),Float.parseFloat(fixedParam[6]), new DatoGradosClass(Directions.valueOf(fixedParam[7])),Float.parseFloat(fixedParam[8]),Float.parseFloat(fixedParam[9]),Float.parseFloat(fixedParam[10]),Float.parseFloat(fixedParam[11]),new DatoGradosClass(Directions.valueOf(fixedParam[12]))));
        }

        return parametrosList;
    }
}
