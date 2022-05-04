package com.example.weadives.SeleccionDeAjuste;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.example.weadives.ConfiguracionDePreferencias.ConfiguracionDePreferencias;
import com.example.weadives.PantallaInicio.PantallaInicio;
import com.example.weadives.R;

public class SeleccionDeAjuste extends AppCompatActivity {

    private Spinner spn_desplegableMarcadores3;
    private Button btn_confirmarParametros, btn_gestionarParametros2;
    private ImageView btn_home9;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seleccion_de_ajuste);
        spn_desplegableMarcadores3 = findViewById(R.id.spn_desplegableMarcadores3);
        btn_gestionarParametros2 = findViewById(R.id.btn_gestionarParametros2);
        btn_confirmarParametros = findViewById(R.id.btn_confirmarParametros);
        btn_home9 = findViewById(R.id.btn_home9);
        Intent intent = getIntent();

        btn_home9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent pantallaInicio = new Intent(getApplicationContext(), PantallaInicio.class);
                startActivity(pantallaInicio);
            }
        });

        btn_gestionarParametros2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent configuracionDePrefernecias = new Intent(getApplicationContext(), ConfiguracionDePreferencias.class);
                startActivity(configuracionDePrefernecias);
            }
        });

        btn_confirmarParametros.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent pantallaInicio = new Intent(getApplicationContext(), PantallaInicio.class);
                startActivity(pantallaInicio);
            }
        });
    }
}