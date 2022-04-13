package com.example.weadives.PantallaMiPerfil;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.weadives.AjustesPerfil.AjustesPerfil;
import com.example.weadives.DatabaseAdapter;
import com.example.weadives.PantallaInicio.PantallaInicio;
import com.example.weadives.PantallaLogIn.PantallaLogIn;
import com.example.weadives.R;

import java.util.ArrayList;

public class PantallaMiPerfil extends AppCompatActivity implements MyRecyclerViewAdapter.ItemClickListener {

    private ImageView img_perfil, btn_home, btn_config;
    private TextView txt_nombrePerfil, txt_codigo;
    private RecyclerView recyclerView;
    private Button btn_cerrarSesion;
    private MyRecyclerViewAdapter adapter;
    private DatabaseAdapter dbA;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pantalla_mi_perfil);
        txt_nombrePerfil = findViewById(R.id.txt_nombrePerfil2);
        txt_codigo = findViewById(R.id.txt_codigo2);
        btn_home = findViewById(R.id.btn_home7);
        img_perfil = findViewById(R.id.img_perfil2);
        recyclerView = findViewById(R.id.rv_llistaAjustes);
        btn_config = findViewById(R.id.btn_config);
        btn_cerrarSesion = findViewById(R.id.btn_cerrarSession);
        Intent intent = getIntent();

        dbA = DatabaseAdapter.getInstance();

        dbA.setName(txt_nombrePerfil);
        dbA.setId(txt_codigo);


        btn_home.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if(!dbA.getLogInStatus()){
                    dbA.singout();
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
                dbA.singout();
                Intent pantallaLogIn = new Intent(getApplicationContext(), PantallaLogIn.class);
                startActivity(pantallaLogIn);
            }
        });

        ArrayList<String> animalNames = new ArrayList<>();
        animalNames.add("Horse");
        animalNames.add("Cow");
        animalNames.add("Camel");
        animalNames.add("Sheep");
        animalNames.add("Goat");
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MyRecyclerViewAdapter(this, animalNames);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);


    }

    @Override
    public void onItemClick(View view, int position) {
        Toast.makeText(this, "You clicked " + adapter.getItem(position) + " on row number " + position, Toast.LENGTH_SHORT).show();
    }
}