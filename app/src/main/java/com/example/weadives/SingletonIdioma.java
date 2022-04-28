package com.example.weadives;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public final class SingletonIdioma extends AppCompatActivity {
    private static SingletonIdioma instance;
    public Traducciones value;
    public Context c;
    public Resources r;

    @Override
    public Resources getResources() {
        return r;
    }
    public void setResources(Resources r) {
        this.r= r;
    }

    private SingletonIdioma() {
        // The following code emulates slow initialization.
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            ex.printStackTrace(); }
        if(value==null){
        }
        //this.value = Traducciones.valueOf(cargarPreferencias());
    }

    private SingletonIdioma(Context c) {
        // The following code emulates slow initialization.
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            ex.printStackTrace(); }
        if(value==null){
        }
        this.c=c;
        this.value = Traducciones.valueOf(cargarPreferencias());

    }

    public static SingletonIdioma init(Context c){
        if (instance == null) {
            instance = new SingletonIdioma(c);
        }
        return instance;
    }

    public static SingletonIdioma getInstance() {
        if (instance == null) {
            instance = new SingletonIdioma();
        }
        return instance;
    }
    public String getValue()
    {return value.toString().toLowerCase(Locale.ROOT);
    }
    public void setValue(Traducciones value)
    {this.value=value;
        guardarPreferencias(value.toString());
    }
    private void guardarPreferencias(String string) {
        SharedPreferences preferencias = getSharedPreferences("idioma", MODE_PRIVATE);
        SharedPreferences.Editor editor=preferencias.edit();
        editor.putString("idioma",string);
        editor.commit();
    }

    private String cargarPreferencias() {
        SharedPreferences preferencias = getSharedPreferences("idioma", MODE_PRIVATE);
        return preferencias.getString("idioma","en");
    }




}