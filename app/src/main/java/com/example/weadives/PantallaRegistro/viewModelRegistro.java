package com.example.weadives.PantallaRegistro;

import android.app.Application;
import android.util.Patterns;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.weadives.DatabaseAdapter;

import java.util.regex.Pattern;

public class viewModelRegistro {

    public viewModelRegistro() {
    }

    public String createUserID(){
        return "0001";
    }

    public boolean validarEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }
}

