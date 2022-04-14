package com.example.weadives.SeleccionDeAjuste;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;

import com.example.weadives.ConfiguracionDePreferencias.ConfiguracionDePreferencias;
import com.example.weadives.DatabaseAdapter;
import com.example.weadives.PantallaInicio.PantallaInicio;
import com.example.weadives.R;

public class SeleccionDeAjuste extends AppCompatActivity {

    private Spinner spn_desplegableMarcadores3;
    private Button btn_confirmarParametros, btn_gestionarParametros2;
    private ImageView btn_home9;
    private DatabaseAdapter dbA;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seleccion_de_ajuste);
        Spinner spn_desplegableMarcadores3 = findViewById(R.id.spn_desplegableMarcadores3);
        Button btn_gestionarParametros2 = findViewById(R.id.btn_gestionarParametros2);
        Button btn_confirmarParametros = findViewById(R.id.btn_confirmarParametros);
        ImageView btn_home9 = findViewById(R.id.btn_home9);
        Intent intent = getIntent();

        dbA = DatabaseAdapter.getInstance();

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