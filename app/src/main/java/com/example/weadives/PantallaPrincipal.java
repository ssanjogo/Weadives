package com.example.weadives;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import androidx.appcompat.app.AppCompatActivity;

public class PantallaPrincipal extends AppCompatActivity {

    private Button btnHorario, btnAñadirNotificacion;
    private ImageView btn_home, btn_social, btn_ajustes;
    private Spinner DesplegableMarcadores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pantalla_principal);
        Button btnHorario = findViewById(R.id.btnHorario);
        Button btnAñadirNotificacion = findViewById(R.id.btnAñadirNotificacion);
        ImageView btn_home = findViewById(R.id.btn_home);
        ImageView btn_social = findViewById(R.id.btn_social);
        ImageView btn_ajustes = findViewById(R.id.btn_ajustes);
        Spinner DesplegableMarcadores = findViewById(R.id.DesplegableMarcadores);
    }



}
