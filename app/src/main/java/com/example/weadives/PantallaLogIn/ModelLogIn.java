package com.example.weadives.PantallaLogIn;

import android.content.Context;
import android.content.SharedPreferences;

public class ModelLogIn {

    private void recordarUser(String correo) {
        SharedPreferences preferencias = getSharedPreferences("user", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=preferencias.edit();
        editor.putString("user",correo);
        editor.commit();
    }
    private String cargarUser() {
        SharedPreferences preferencias = getSharedPreferences("user",Context.MODE_PRIVATE);
        return preferencias.getString("user","Usuario no valido");
    }

    private void recordarPassword(String contraseña) {
        SharedPreferences preferencias = getSharedPreferences("password",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=preferencias.edit();
        editor.putString("password",contraseña);
        editor.commit();
    }
    private String cargarPassword() {
        SharedPreferences preferencias = getSharedPreferences("password",Context.MODE_PRIVATE);
        return preferencias.getString("password","");
    }
}
