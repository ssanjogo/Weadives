package com.example.weadives;

public class DatoGradosClass {
    float valor;

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
    public void setValor(Directions direction){
        switch (direction){
            case NORTE:
                this.valor=0;
            case NORDESTE:
                this.valor=45;
            case ESTE:
              this.valor=90;
            case SUDESTE:
                this.valor=135;
            case SUD:
               this.valor=180;
            case SUDOESTE:
                this.valor= 225;
            case OESTE:
                this.valor=270;
            case NORDOESTE:
               this.valor=315;
            case NO_DIRECTION:
                System.out.println("ERROR NO DIRECCION");
        }

    }

    public boolean inRange(Directions direction){
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
            case NO_DIRECTION:
                break;
            default:
                return false;
        }
        return false;
    }
    public Directions getDirection(){
        switch ((this.valor<=22.5 || this.valor>=337.5) ? 0 :
                (this.valor<=67.5 || this.valor>=22.5) ? 1 :(this.valor<=112.5|| this.valor>=67.5) ? 2:(this.valor<=157.5 || this.valor>=112.5)?3:(this.valor<=202.5 || this.valor>=157.5)?4:(this.valor<=247.5 || this.valor>=202.5)?5:(this.valor<=292.5 || this.valor>=247.5)?6:(this.valor<=337.5 || this.valor>=292.5)?7:8){
            case 0:
                return Directions.NORTE;
            case 1:
                return Directions.NORDESTE;
            case 2:
                return Directions.ESTE;
            case 3:
                return Directions.SUDESTE;
            case 4:
                return Directions.SUD;
            case 5:
                return Directions.SUDOESTE;
            case 6:
                return Directions.OESTE;
            case 7:
                return Directions.NORDOESTE;
            case 8:
                return Directions.NO_DIRECTION;
        }
        return Directions.NO_DIRECTION;
    }
}
