package com.example.weadives.PantallaPerfilAmigo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weadives.PantallaInicio.PantallaInicio;
import com.example.weadives.ParametrosClass;
import com.example.weadives.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PantallaPerfilAmigo extends AppCompatActivity {

    private ImageView img_perfil, btn_home;
    private TextView txt_nombrePerfil, txt_codigo;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<PublicacionClass> publicacionList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pantalla_perfil_amigo);
        ImageView img_perfil = findViewById(R.id.img_perfil3);
        ImageView btn_home = findViewById(R.id.btn_home11);
        TextView txt_nombrePerfil= findViewById(R.id.txt_nombrePerfil3);
        TextView txt_codigo = findViewById(R.id.txt_codigo3);


        publicacionList = fillPublicacionList();


        RecyclerView recyclerView = findViewById(R.id.rv_llistaAjustes3);
        //mejorar performance
        recyclerView.hasFixedSize();
        //lineal layout
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        //especificar adapter
        mAdapter= new PublicacionesPerfilAdapter(publicacionList, PantallaPerfilAmigo.this);
        recyclerView.setAdapter(mAdapter);

        Intent intent=getIntent();
        String username=intent.getStringExtra("username");
        txt_nombrePerfil.setText(username);

        btn_home.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent pantallaInicio = new Intent(getApplicationContext(), PantallaInicio.class);
                startActivity(pantallaInicio);
            }
        });

    }

    private List<PublicacionClass> fillPublicacionList() {
        List<PublicacionClass> publicacionList= new ArrayList<PublicacionClass>();
        ParametrosClass p1= new ParametrosClass("Surf", 0, 0.1f,0.2f,0.3f,0.4f);
        HashMap<String, Integer> likeList1=new HashMap<>();
        likeList1.put("0000",1);
        likeList1.put("0001",1);
        likeList1.put("0002",0);
        HashMap<String, String> comentariosList1=new HashMap<>();
        comentariosList1.put("Carlos Chun","Oye, ta guapo etooo");
        comentariosList1.put("Oscaroca","Esta guay para disfrutar del restaurante que hay al lado.");
        comentariosList1.put("Mi pana miguel","Viva er beti");
        comentariosList1.put("Racsor","Fumas?");
        comentariosList1.put("Mikol","Jo parlo catalá");
        comentariosList1.put("Sara","Totorooooo");
        comentariosList1.put("Matt","Has visto como entrenar a tu dragon?");
        comentariosList1.put("Septimus","AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
        publicacionList.add(new PublicacionClass(p1,likeList1,comentariosList1));
        ParametrosClass p2= new ParametrosClass("Dia de Playa", 11, 3.1f,3.2f,3.3f,3.4f);
        HashMap<String, Integer> likeList2=new HashMap<>();
        likeList2.put("0000",0);
        likeList2.put("0001",0);
        likeList2.put("0002",0);
        HashMap<String, String> comentariosList2=new HashMap<>();
        comentariosList2.put("0001","---");
        comentariosList2.put("0021","---");
        comentariosList2.put("0031","---");
        publicacionList.add(new PublicacionClass(p2,likeList2,comentariosList2));
        ParametrosClass p3= new ParametrosClass("Dia de Lol", 111, 3.1f,3.2f,3.3f,3.4f);
        HashMap<String, Integer> likeList3=new HashMap<>();
        likeList3.put("0000",1);
        likeList3.put("0001",1);
        likeList3.put("0002",1);
        HashMap<String, String> comentariosList3=new HashMap<>();
        comentariosList3.put("0001","---");
        comentariosList3.put("0021","---");
        publicacionList.add(new PublicacionClass(p3,likeList3,comentariosList3));



        return publicacionList;
    }


}