package com.example.weadives.PantallaPrincipal;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import androidx.appcompat.app.AppCompatActivity;

import com.example.weadives.ConfiguracionDePreferencias.ConfiguracionDePreferencias;
import com.example.weadives.LocaleHelper;
import com.example.weadives.PantallaDeHorarios.PantallaDeHorarios;
import com.example.weadives.PantallaLogIn.PantallaLogIn;
import com.example.weadives.R;
import com.example.weadives.SeleccionDeAjuste.SeleccionDeAjuste;

public class PantallaPrincipal extends AppCompatActivity {

    private Button btnHorario, btnAñadirNotificacion, btn_gestionarParametros;
    private ImageView btn_home, btn_social;
    private Spinner DesplegableMarcadores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pantalla_principal);
        Button btnHorario = findViewById(R.id.btnHorario);
        Button btnAñadirNotificacion = findViewById(R.id.btnAñadirNotificacion);
        Button btn_gestionarParametros = findViewById(R.id.btn_gestionarParametros);
        ImageView btn_home = findViewById(R.id.btn_home);
        ImageView btn_social = findViewById(R.id.btn_social);
        Spinner DesplegableMarcadores = findViewById(R.id.spn_desplegableMarcadores);

        final Context[] context = new Context[1];
        final Resources[] resources = new Resources[1];

        Intent intent = getIntent();

        btn_home.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                finish();
            }
        });

        btn_social.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent LogIn = new Intent(getApplicationContext(), PantallaLogIn.class);
                startActivity(LogIn);
            }
        });



        btn_gestionarParametros.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent configuracionParametros = new Intent(getApplicationContext(), ConfiguracionDePreferencias.class);
                startActivity(configuracionParametros);
            }
        });
        btnAñadirNotificacion.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                //Intent seleccionDeAjuste = new Intent(getApplicationContext(), SeleccionDeAjuste.class);
                //startActivity(seleccionDeAjuste);
                context[0] = LocaleHelper.setLocale(PantallaPrincipal.this, "en");
                resources[0] = context[0].getResources();
                btnHorario.setText(resources[0].getString(R.string.horarios));
                //  Intent pantallaHorarios = new Intent(getApplicationContext(), PantallaDeHorarios.class);
            }
        });

        btnHorario.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                context[0] = LocaleHelper.setLocale(PantallaPrincipal.this, "es");
                resources[0] = context[0].getResources();
                btnHorario.setText(resources[0].getString(R.string.horarios));
              //  Intent pantallaHorarios = new Intent(getApplicationContext(), PantallaDeHorarios.class);
              //  startActivity(pantallaHorarios);
            }
        });

    }
}
