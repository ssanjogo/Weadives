package com.example.weadives.PantallaPrincipal;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.weadives.ConfiguracionDePreferencias.ConfiguracionDePreferencias;
import com.example.weadives.LocaleHelper;
import com.example.weadives.PantallaDeHorarios.PantallaDeHorarios;
import com.example.weadives.PantallaDeHorarios.ViewModelHorario;
import com.example.weadives.PantallaLogIn.PantallaLogIn;
import com.example.weadives.PantallaMapa.MarcadorClass;
import com.example.weadives.PantallaMapa.ViewModelMapa;
import com.example.weadives.R;
import com.example.weadives.SingletonIdioma;
import com.example.weadives.ViewModel;

import java.util.ArrayList;

public class PantallaPrincipal extends AppCompatActivity {

    private Button btnHorario, btnAñadirNotificacion, btn_gestionarParametros;
    private ImageView btn_home, btn_social;
    private Spinner DesplegableMarcadores;
    private ViewModel viewModel;
    private ViewModelMapa mapViewModel;
    private ViewModelHorario horarioViewModel;
    private Boolean marcadoresEmpty;

    @Override
    public void onResume(){
        super.onResume();
        final Context context;
        SingletonIdioma s= SingletonIdioma.getInstance();
        Resources resources=s.getResources();
        context = LocaleHelper.setLocale(this, cargarPreferencias());

        btnHorario.setText(resources.getString(R.string.horarios));
        btn_gestionarParametros.setText((resources.getString(R.string.gestionar_parametros)));


    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pantalla_principal);
        btnHorario = findViewById(R.id.btnHorario);
        btn_gestionarParametros = findViewById(R.id.btn_gestionarParametros);
        btn_home = findViewById(R.id.btn_home);
        btn_social = findViewById(R.id.btn_social);
        DesplegableMarcadores = findViewById(R.id.spn_desplegableMarcadores);

        marcadoresEmpty = false;

        final Context context;
        SingletonIdioma s= SingletonIdioma.getInstance();
        Resources resources=s.getResources();
        context = LocaleHelper.setLocale(this, cargarPreferencias());

        btnHorario.setText(resources.getString(R.string.horarios));
        btn_gestionarParametros.setText((resources.getString(R.string.gestionar_parametros)));

        btnHorario.setEnabled(false);

        viewModel = ViewModel.getInstance(this);
        mapViewModel = ViewModelMapa.getInstance(this);
        horarioViewModel = ViewModelHorario.getInstance(this);
        Intent intent = getIntent();

       /* @Override
        public void onBackPressed()
        {
            super.onBackPressed();
            finish();
            startActivity(getIntent());
        }*/

        ArrayList<MarcadorClass> list = new ArrayList<>();
        list = mapViewModel.getMarcadores();

        ArrayAdapter<MarcadorClass> adapter = new ArrayAdapter<MarcadorClass>(this, R.layout.one_spinner_list, list){
            @Override
            public boolean isEnabled(int position){
                if(position == 0){
                    return false;
                }
                else{
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0){
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                }
                else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        adapter.setDropDownViewResource(R.layout.one_spinner_list);
        DesplegableMarcadores.setAdapter(adapter);

        DesplegableMarcadores.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String location = ((MarcadorClass) DesplegableMarcadores.getSelectedItem()).getLocation();
                if(!location.equals("0_0")){
                    horarioViewModel.setMarcador((MarcadorClass) DesplegableMarcadores.getSelectedItem());
                    System.out.println("AWAAWA");
                }
                System.out.println(location + "  " + i);
                if(i > 0){
                    btnHorario.setEnabled(true);
                    Toast.makeText(getApplicationContext(), "Selected : " + DesplegableMarcadores.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                System.out.println("nothing selected but" + ((MarcadorClass) DesplegableMarcadores.getSelectedItem()).getName());
            }
        });




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

        btnHorario.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Animation animation= AnimationUtils.loadAnimation(context,R.anim.blink_anim2);
                btnHorario.startAnimation(animation);

                Intent pantallaHorarios = new Intent(getApplicationContext(), PantallaDeHorarios.class);
                startActivity(pantallaHorarios);
            }
        });

        btn_gestionarParametros.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Animation animation3= AnimationUtils.loadAnimation(context,R.anim.blink_anim2);
                btn_gestionarParametros.startAnimation(animation3);
                Intent configuracionParametros = new Intent(getApplicationContext(), ConfiguracionDePreferencias.class);
                startActivity(configuracionParametros);
            }
        });

    }

    private String cargarPreferencias() {
        SharedPreferences preferencias = getSharedPreferences("idioma",Context.MODE_PRIVATE);
        return preferencias.getString("idioma","en");
    }
}
