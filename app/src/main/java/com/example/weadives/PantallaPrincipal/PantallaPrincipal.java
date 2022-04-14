package com.example.weadives.PantallaPrincipal;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.media.midi.MidiOutputPort;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.weadives.AreaUsuario.AreaUsuario;
import com.example.weadives.ConfiguracionDePreferencias.ConfiguracionDePreferencias;
import com.example.weadives.DatabaseAdapter;
import com.example.weadives.LocaleHelper;
import com.example.weadives.PantallaDeHorarios.PantallaDeHorarios;
import com.example.weadives.PantallaLogIn.PantallaLogIn;
import com.example.weadives.R;
import com.example.weadives.SeleccionDeAjuste.SeleccionDeAjuste;

public class PantallaPrincipal extends AppCompatActivity {

    private Button btnHorario, btnAñadirNotificacion, btn_gestionarParametros;
    private ImageView btn_home, btn_social;
    private Spinner DesplegableMarcadores;

    private DatabaseAdapter dbA;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pantalla_principal);
        btnHorario = findViewById(R.id.btnHorario);
        btnAñadirNotificacion = findViewById(R.id.btnAñadirNotificacion);
        btn_gestionarParametros = findViewById(R.id.btn_gestionarParametros);
        btn_home = findViewById(R.id.btn_home);
        btn_social = findViewById(R.id.btn_social);
        DesplegableMarcadores = findViewById(R.id.spn_desplegableMarcadores);

        final Context context;
        final Resources resources;
        context = LocaleHelper.setLocale(this, cargarPreferencias());
        resources = context.getResources();
        btnHorario.setText(resources.getString(R.string.horarios));
        btnAñadirNotificacion.setText((resources.getString(R.string.AñadirNotificacion)));
        btn_gestionarParametros.setText((resources.getString(R.string.gestionar_parametros)));

        dbA = DatabaseAdapter.getInstance();

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
                //LogIn.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(LogIn);
                //finish();
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
                Intent seleccionDeAjuste = new Intent(getApplicationContext(), SeleccionDeAjuste.class);
                startActivity(seleccionDeAjuste);
                Intent pantallaHorarios = new Intent(getApplicationContext(), PantallaDeHorarios.class);
            }
        });

        btnHorario.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent pantallaHorarios = new Intent(getApplicationContext(), PantallaDeHorarios.class);
                startActivity(pantallaHorarios);
            }
        });

    }


    private String cargarPreferencias() {
        SharedPreferences preferencias = getSharedPreferences("idioma",Context.MODE_PRIVATE);
        return preferencias.getString("idioma","en");
    }
}
