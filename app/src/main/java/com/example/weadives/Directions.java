package com.example.weadives;

import java.util.ArrayList;

public enum Directions {
        NORTE,
        NORDESTE,
        ESTE,
        SUDESTE,
        SUD,
        SUDOESTE,
        OESTE,
        NORDOESTE,
        NO_DIRECTION;

       public ArrayList toArrayString(){
                ArrayList<String> list=new ArrayList<>();
                list.add(NORTE.toString());
                list.add(NORDESTE.toString());
                list.add(ESTE.toString());
                list.add(SUDESTE.toString());
                list.add(SUD.toString());
                list.add(SUDESTE.toString());
                list.add(OESTE.toString());
                list.add(NORDOESTE.toString());
                list.add(NO_DIRECTION.toString());
                return list;
        }
        public int toInt(Directions dir){
           return (dir==NORTE) ? 0 : (dir==NORDESTE) ? 1 :(dir==ESTE) ? 2:(dir==SUDESTE)?3:(dir==SUD)?4:(dir==SUDOESTE)?5:(dir==OESTE)?6:(dir==NORDOESTE)?7:8;
        }
    public static Directions fromInt(int dir){
        return (dir==0) ? NORTE : (dir==1) ? NORDESTE :(dir==2) ? ESTE:(dir==3)?SUDESTE:(dir==4)?SUD:(dir==5)?SUDOESTE:(dir==6)?OESTE:(dir==7)?NORDOESTE:NO_DIRECTION;
    }

}
