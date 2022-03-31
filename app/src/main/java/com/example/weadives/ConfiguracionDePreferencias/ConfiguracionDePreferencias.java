package com.example.weadives.ConfiguracionDePreferencias;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;

import com.example.weadives.LocaleHelper;
import com.example.weadives.PantallaInicio.PantallaInicio;
import com.example.weadives.PantallaPrincipal.PantallaPrincipal;
import com.example.weadives.R;
import com.example.weadives.SeleccionDeAjuste.SeleccionDeAjuste;


public class ConfiguracionDePreferencias extends AppCompatActivity {

    private Spinner spn_desplegableMarcadores2;
    private Button btn_guardar;
    private ImageView btn_añadir2, btn_home8;
    private ScrollView scrollView2;
    private Switch sw_notificaciones, sw_mostrarEnPerfil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.configuracion_de_preferencias);
        Spinner spn_desplegableMarcadores2 = findViewById(R.id.spn_desplegableMarcadores2);
        Button btn_guardar = findViewById(R.id.btn_guardar);
        ImageView btn_añadir2 = findViewById(R.id.btn_añadir2);
        ImageView btn_home8 = findViewById(R.id.btn_home8);
        ScrollView scrollView2 = findViewById(R.id.scrollView2);
        Switch sw_notificaciones = findViewById(R.id.sw_notificaciones);
        Switch sw_mostrarEnPerfil = findViewById(R.id.sw_mostrarEnPerfil);

        Intent intent = getIntent();

        final Context context;
        final Resources resources;
        context = LocaleHelper.setLocale(this, cargarPreferencias());
        resources = context.getResources();
        btn_guardar.setText(resources.getString(R.string.guardar));
        sw_notificaciones.setText(resources.getString(R.string.notificaciones));
        sw_mostrarEnPerfil.setText(resources.getString(R.string.mostrar_en_perfil));

        btn_home8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent pantallaInicio = new Intent(getApplicationContext(), PantallaInicio.class);
                startActivity(pantallaInicio);
            }
        });

        btn_guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent seleccionDeAjuste = new Intent(getApplicationContext(), SeleccionDeAjuste.class);
                startActivity(seleccionDeAjuste);
            }
        });
    }
    private String cargarPreferencias() {
        SharedPreferences preferencias = getSharedPreferences("idioma", Context.MODE_PRIVATE);
        return preferencias.getString("idioma","en");
    }
}