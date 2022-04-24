package com.example.weadives.PantallaMiPerfil;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.weadives.AjustesPerfil.AjustesPerfil;
import com.example.weadives.AreaUsuario.AreaUsuario;
import com.example.weadives.PantallaInicio.PantallaInicio;
import com.example.weadives.PantallaLogIn.PantallaLogIn;
import com.example.weadives.ParametrosClass;
import com.example.weadives.R;
import com.example.weadives.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class PantallaMiPerfil extends AppCompatActivity {

    private ImageView img_perfil, btn_home, btn_config;
    private TextView txt_nombrePerfil;
    private RecyclerView recyclerView;
    private Button btn_cerrarSesion;

    private ViewModel viewModel;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<ParametrosClass> parametrosList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pantalla_mi_perfil);
        txt_nombrePerfil = findViewById(R.id.txt_nombrePerfil2);
        btn_home = findViewById(R.id.btn_home7);
        img_perfil = findViewById(R.id.img_perfil2);
        recyclerView = findViewById(R.id.rv_llistaAjustes);
        btn_config = findViewById(R.id.btn_config);
        btn_cerrarSesion = findViewById(R.id.btn_cerrarSession);

        parametrosList = fillParametrosList();

        recyclerView = findViewById(R.id.rv_llistaAjustes);
        //mejorar performance
        recyclerView.hasFixedSize();
        //lineal layout
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        //especificar adapter
        mAdapter= new ParametrosPerfilAdapter(parametrosList,PantallaMiPerfil.this);
        recyclerView.setAdapter(mAdapter);

        viewModel = ViewModel.getInstance(this);
        Intent intent = getIntent();

        txt_nombrePerfil.setText(viewModel.getCurrentUser().getUsername());
        Glide.with(this).load(viewModel.getCurrentUser().getUrlImg()).into(img_perfil);

        btn_home.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if(!viewModel.getLogInStatus()){
                    viewModel.singOut();
                }
                Intent pantallaInicio = new Intent(getApplicationContext(), PantallaInicio.class);
                startActivity(pantallaInicio);
            }
        });

        btn_config.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent ajustePerfil = new Intent(getApplicationContext(), AjustesPerfil.class);
                startActivity(ajustePerfil);
            }
        });

        btn_cerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.setLogInStatus(false);
                viewModel.singOut();
                Intent pantallaLogIn = new Intent(getApplicationContext(), PantallaLogIn.class);
                startActivity(pantallaLogIn);
            }
        });
    }

    public boolean onKeyDown(int keyCode, KeyEvent event){
        if(keyCode == event.KEYCODE_BACK){
            Intent areaUsuario = new Intent(getApplicationContext(), AreaUsuario.class);
            startActivity(areaUsuario);
        }
        return super.onKeyDown(keyCode, event);
    }

    private List<ParametrosClass> fillParametrosList() {
        List<ParametrosClass> parametrosList = new ArrayList<>();
        parametrosList.add(new ParametrosClass("Surf", 0, 0.1f,0.2f,0.3f,0.4f));
        parametrosList.add(new ParametrosClass("Dia de Playa", 11, 3.1f,3.2f,3.3f,3.4f));
        parametrosList.add(new ParametrosClass("Vela",2222, 4.1f,4.2f,4.3f,4.4f));
        parametrosList.add(new ParametrosClass("Kayak",3333, 5.1f,5.2f,5.3f,5.4f));
        parametrosList.add(new ParametrosClass("LOL",6546, 5.1f,5.2f,5.3f,5.4f));
        parametrosList.add(new ParametrosClass("COD",1231, 5.1f,5.2f,5.3f,5.4f));
        parametrosList.add(new ParametrosClass("Pilota",2331, 5.1f,5.2f,5.3f,5.4f));
        parametrosList.add(new ParametrosClass("ORIND",9996, 5.1f,5.2f,5.3f,5.4f));
        return parametrosList;
    }
}