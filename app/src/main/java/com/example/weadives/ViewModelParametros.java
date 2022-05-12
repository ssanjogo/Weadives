package com.example.weadives;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;

public final class ViewModelParametros {
    private static ViewModelParametros SINGLETON_INSTANCE;
    public Context c;
    public Resources r;
    private MutableLiveData<ArrayList<ParametrosClass>> mutableList;

    private ArrayList<ParametrosClass> lista;


    private ViewModelParametros() {

    }

    public ViewModelParametros(Resources r,Context c) {
        this.r=r;
        this.c=c;
        System.out.println("AQUI LLEGA\n");
        String test=cargarPreferenciasParametros();
        System.out.println(test);
        if(test.length()!=0){lista=descomprimirArray(test);}else{
            lista=new ArrayList<>();
        }

        System.out.println(lista);
    }
    public void addParametro(ParametrosClass p){
        lista.add(p);
        guardarPersistencia();
    }

    private void guardarPersistencia() {
        ArrayList<ParametrosClass> pers=new ArrayList<>();
        for(int i=0; i<lista.size();i++){
            if(lista.get(i).getIdPublicacion().equals("0")){
                pers.add(lista.get(i));
            }
        }
        if(pers.size()!=0){
            guardarPreferenciasParametros(comprimirArray(pers));
        }
    }

    public Resources getResources() {
        return r;
    }
    public void setResources(Resources r) {
        this.r= r;
    }

    public static ViewModelParametros getSingletonInstance() {
        return SINGLETON_INSTANCE;
    }
    public static ViewModelParametros getSingletonInstance(Resources r,Context c) {
        if(SINGLETON_INSTANCE == null) {
            SINGLETON_INSTANCE = new ViewModelParametros(r,c);
        }

        return SINGLETON_INSTANCE;
    }

    public ArrayList<ParametrosClass> getLista() {
        return lista;
    }

    private void guardarPreferenciasParametros(String string) {
        SharedPreferences preferencias = c.getSharedPreferences("parametros", MODE_PRIVATE);
        SharedPreferences.Editor editor=preferencias.edit();
        editor.putString("parametros",string);
        editor.commit();
    }

    private String cargarPreferenciasParametros() {
        SharedPreferences preferencias = c.getSharedPreferences("parametros",MODE_PRIVATE);
        return preferencias.getString("parametros","");
        //return preferencias.getString("parametros",comprimirArray(fillParametrosList()));
    }
    private ArrayList<ParametrosClass> fillParametrosList() {
        ArrayList<ParametrosClass> parametrosList = new ArrayList<>();
        ParametrosClass p1= new ParametrosClass("SurfLoco", 0.2f,0.1f,0.3f,0.2f,0.3f,0.3f, new DatoGradosClass(Directions.SUD),3.f,2.f,4.f,4.f,new DatoGradosClass(Directions.ESTE));
        parametrosList.add(p1);
        parametrosList.add(new ParametrosClass("Surf",  0.2f,0.1f,0.3f,0.2f,0.3f,0.3f, new DatoGradosClass(Directions.SUD),3.f,2.f,4.f,4.f,new DatoGradosClass(Directions.ESTE)));
        parametrosList.add(new ParametrosClass("Playa",  0.2f,0.1f,0.3f,0.2f,0.3f,0.3f, new DatoGradosClass(Directions.SUD),3.f,2.f,4.f,4.f,new DatoGradosClass(Directions.ESTE)));
        parametrosList.add(new ParametrosClass("Vela",  0.2f,0.1f,0.3f,0.2f,0.3f,0.3f, new DatoGradosClass(Directions.SUD),3.f,2.f,4.f,4.f,new DatoGradosClass(Directions.ESTE)));
        parametrosList.add(new ParametrosClass("Kayak",  0.2f,0.1f,0.3f,0.2f,0.3f,0.3f, new DatoGradosClass(Directions.SUD),3.f,2.f,4.f,4.f,new DatoGradosClass(Directions.ESTE)));
        parametrosList.add(new ParametrosClass("LioLegends",  0.2f,0.1f,0.3f,0.2f,0.3f,0.3f, new DatoGradosClass(Directions.SUD),3.f,2.f,4.f,4.f,new DatoGradosClass(Directions.ESTE)));
        parametrosList.add(new ParametrosClass("CallDuty",  0.2f,0.1f,0.3f,0.2f,0.3f,0.3f, new DatoGradosClass(Directions.SUD),3.f,2.f,4.f,4.f,new DatoGradosClass(Directions.ESTE)));
        parametrosList.add(new ParametrosClass("BalonPie",  0.2f,0.1f,0.3f,0.2f,0.3f,0.3f, new DatoGradosClass(Directions.SUD),3.f,2.f,4.f,4.f,new DatoGradosClass(Directions.ESTE)));
        parametrosList.add(new ParametrosClass("DokkanBattle", 0.2f,0.1f,0.3f,0.2f,0.3f,0.3f, new DatoGradosClass(Directions.SUD),3.f,2.f,4.f,4.f,new DatoGradosClass(Directions.ESTE)));
        return parametrosList;
    }
    private String comprimirArray(ArrayList<ParametrosClass> l){
        String str="";
        for(int i=0; i<l.size();i++){
            str=str+l.get(i).toSaveString()+"¿";
        }
        return str;
    }
    private ArrayList<ParametrosClass> descomprimirArray(String l){
        System.out.println("HEHHEHEHEHHEHEH");
        System.out.println(l);
        String[] parametrosStringList = l.split("¿");
        int count = l.length() - l.replace("¿", "").length();
        System.out.println(count);
        ArrayList<ParametrosClass> parametrosList = new ArrayList<>();
        String[] fixedParam;
        for (String i : parametrosStringList) {
            System.out.println(i);
            fixedParam=i.split(",");
            for (String x : fixedParam) {
                System.out.println(x);
            }
            parametrosList.add(new ParametrosClass(fixedParam[0], Float.parseFloat(fixedParam[1]),Float.parseFloat(fixedParam[2]),Float.parseFloat(fixedParam[3]),Float.parseFloat(fixedParam[4]),Float.parseFloat(fixedParam[5]),Float.parseFloat(fixedParam[6]), new DatoGradosClass(Directions.valueOf(fixedParam[7])),Float.parseFloat(fixedParam[8]),Float.parseFloat(fixedParam[9]),Float.parseFloat(fixedParam[10]),Float.parseFloat(fixedParam[11]),new DatoGradosClass(Directions.valueOf(fixedParam[12]))));
        }

        return parametrosList;
    }
}
