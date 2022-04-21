package com.example.weadives;

public class DatoGradosClass {
    float valor;
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

    public DatoGradosClass(float valor) {
        if (valor<360 && valor>=0){
            this.valor = valor;
        }
        else{
            this.valor = valor%360;
        }
    }

    public float getValor() {
        return valor;
    }

    public void setValor(float valor) {
        this.valor = valor%360;
    }
    public boolean inRange(direction direction){
        switch (direction){
            case NORTE:
                if (this.valor<=22.5 || this.valor>=337.5){
                    return true;
                }return false;
            case NORDESTE:
                if (this.valor<=67.5 || this.valor>=22.5){
                    return true;
                }return false;
            case ESTE:
                if (this.valor<=112.5|| this.valor>=67.5){
                    return true;
                }return false;
            case SUDESTE:
                if (this.valor<=157.5 || this.valor>=112.5){
                    return true;
                }return false;
            case SUD:
                if (this.valor<=202.5 || this.valor>=157.5){
                    return true;
                }return false;
            case SUDOESTE:
                if (this.valor<=247.5 || this.valor>=202.5){
                    return true;
                }return false;
            case OESTE:
                if (this.valor<=292.5 || this.valor>=247.5){
                    return true;
                }return false;
            case NORDOESTE:
                if (this.valor<=337.5 || this.valor>=292.5){
                    return true;
                }return false;
            default:
                return false;
        }

    }
    public direction getDirection(){
        switch ((this.valor<=22.5 || this.valor>=337.5) ? 0 :
                (this.valor<=67.5 || this.valor>=22.5) ? 1 :(this.valor<=112.5|| this.valor>=67.5) ? 2:(this.valor<=157.5 || this.valor>=112.5)?3:(this.valor<=202.5 || this.valor>=157.5)?4:(this.valor<=247.5 || this.valor>=202.5)?5:(this.valor<=292.5 || this.valor>=247.5)?6:(this.valor<=337.5 || this.valor>=292.5)?7:8){
            case 0:
                return direction.NORTE;
            case 1:
                return direction.NORDESTE;
            case 2:
                return direction.ESTE;
            case 3:
                return direction.SUDESTE;
            case 4:
                return direction.SUD;
            case 5:
                return direction.SUDOESTE;
            case 6:
                return direction.OESTE;
            case 7:
                return direction.NORDOESTE;
            case 8:
                return direction.NO_DIRECTION;
        }
        return direction.NO_DIRECTION;
    }
}
