package com.example.weadives.PantallaMiPerfil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weadives.AjustesPerfil.AjustesPerfil;
import com.example.weadives.PantallaInicio.PantallaInicio;
import com.example.weadives.ParametrosClass;
import com.example.weadives.R;

import java.util.ArrayList;
import java.util.List;

public class PantallaMiPerfil extends AppCompatActivity implements MyRecyclerViewAdapter.ItemClickListener {

    private ImageView img_perfil, btn_home, btn_config;
    private TextView txt_nombrePerfil, txt_codigo;
    private RecyclerView recyclerView;
    MyRecyclerViewAdapter adapter;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<ParametrosClass> parametrosList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pantalla_mi_perfil);
        ImageView btn_home = findViewById(R.id.btn_home7);
        ImageView img_perfil = findViewById(R.id.img_perfil2);
        ImageView btn_config = findViewById(R.id.btn_config);

        parametrosList = fillParametrosList();


        RecyclerView recyclerView = findViewById(R.id.rv_llistaAjustes);
        //mejorar performance
        recyclerView.hasFixedSize();
        //lineal layout
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        //especificar adapter
        mAdapter= new ParametrosPerfilAdapter(parametrosList,PantallaMiPerfil.this);
        recyclerView.setAdapter(mAdapter);

        Intent intent = getIntent();


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
                Intent ajustePerfil = new Intent(getApplicationContext(), AjustesPerfil.class);
                startActivity(ajustePerfil);
            }
        });









        //ANTIGUO
        //recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //adapter = new MyRecyclerViewAdapter(this, parametrosList);
        //adapter.setClickListener(this);
        //recyclerView.setAdapter(adapter);


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

    @Override
    public void onItemClick(View view, int position) {
        Toast.makeText(this, "You clicked " + adapter.getItem(position) + " on row number " + position, Toast.LENGTH_SHORT).show();
    }
}