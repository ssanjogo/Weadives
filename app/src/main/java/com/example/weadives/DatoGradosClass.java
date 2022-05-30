package com.example.weadives;

import android.graphics.Path;

public class DatoGradosClass {
    Directions dir;

    public DatoGradosClass(Directions dir) {
        this.dir=dir;
    }

    public Directions getDir() {
        if(dir==null){
            return Directions.NO_DIRECTION;
        }
        return dir;

    }

    public void setDir(Directions dir) {
        this.dir = dir;
    }
    public String toString2(){
        return dir.name();
    }
    public void setDirFromInt(int i) {
        this.dir=Directions.fromInt(i);
    }

}
