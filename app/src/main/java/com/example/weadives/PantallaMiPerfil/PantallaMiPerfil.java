package com.example.weadives.PantallaMiPerfil;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.weadives.PantallaInicio.PantallaInicio;
import com.example.weadives.R;

import java.util.ArrayList;

public class PantallaMiPerfil extends AppCompatActivity implements MyRecyclerViewAdapter.ItemClickListener {


    private ImageView img_perfil, btn_home, btn_config;
    private TextView txt_nombrePerfil, txt_codigo;
    private RecyclerView recyclerView;
    MyRecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pantalla_mi_perfil);
        btn_home = findViewById(R.id.btn_home7);
        ImageView img_perfil = findViewById(R.id.img_perfil2);
        RecyclerView recyclerView = findViewById(R.id.rv_llistaAjustes);
        btn_config = findViewById(R.id.btn_config);


        btn_home.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent pantallaInicio = new Intent(getApplicationContext(), PantallaInicio.class);
                startActivity(pantallaInicio);
            }
        });
        btn_config.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                //TODO
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