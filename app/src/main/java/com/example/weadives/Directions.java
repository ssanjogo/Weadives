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

}
