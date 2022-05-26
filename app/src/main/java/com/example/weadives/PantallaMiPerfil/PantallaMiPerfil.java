package com.example.weadives.PantallaMiPerfil;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.weadives.AjustesPerfil.AjustesPerfil;
import com.example.weadives.AreaUsuario.AreaUsuario;
import com.example.weadives.DatoGradosClass;
import com.example.weadives.Directions;
import com.example.weadives.PantallaInicio.PantallaInicio;
import com.example.weadives.PantallaLogIn.PantallaLogIn;
import com.example.weadives.PantallaPerfilAmigo.PublicacionClass;
import com.example.weadives.PantallaPerfilAmigo.PublicacionesPerfilAdapter;
import com.example.weadives.PantallaPrincipal.PantallaPrincipal;
import com.example.weadives.ParametrosClass;
import com.example.weadives.R;
import com.example.weadives.SingletonIdioma;
import com.example.weadives.ViewModel;
import com.example.weadives.ViewModelParametros;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PantallaMiPerfil extends AppCompatActivity {

    private ImageView img_perfil, btn_home, btn_config;
    private TextView txt_nombrePerfil,emptyView;
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
        emptyView  = findViewById(R.id.empty_view);

        //parametrosList = fillParametrosList();
        //List<PublicacionClass> publicacionList= fillPublicacionList();
        List<PublicacionClass> publicacionList= new ArrayList<>();

        publicacionList=ViewModelParametros.getSingletonInstance().getPublications();
        
        recyclerView = findViewById(R.id.rv_llistaAjustes);
        //mejorar performance
        recyclerView.hasFixedSize();
        //lineal layout
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        //especificar adapter
        if(publicacionList==null){
            publicacionList=new ArrayList<>();
        }
        System.out.println(publicacionList);
        mAdapter= new PublicacionesPerfilAdapter(publicacionList,PantallaMiPerfil.this);
        recyclerView.setAdapter(mAdapter);

        viewModel = ViewModel.getInstance(this);
        Intent intent = getIntent();

        txt_nombrePerfil.setText(viewModel.getCurrentUser().getUsername());
        Glide.with(this).load(viewModel.getCurrentUser().getUrlImg()).into(img_perfil);

        final Context context;
        SingletonIdioma s= SingletonIdioma.getInstance();
        Resources resources=s.getResources();

        if (publicacionList.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            emptyView.setText(resources.getString(R.string.no_public_available));
            emptyView.setVisibility(View.VISIBLE);
        }
        else {
            recyclerView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        }

        btn_home.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent pantallaInicio = new Intent(getApplicationContext(), PantallaInicio.class);
                startActivity(pantallaInicio);
                if(!viewModel.getLogInStatus()){
                    viewModel.singOut();
                }

                finish();
                System.out.println("TESTING LIMITS");
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

                viewModel.keepSession(false);
                viewModel.singOut();
                finish();
            }
        });

        btn_cerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.singOut();
                recordarContraseña("");
                Intent pantallaLogIn = new Intent(getApplicationContext(), PantallaLogIn.class);
                startActivity(pantallaLogIn);
                finish();
            }
        });
    }

    public boolean onKeyDown(int keyCode, KeyEvent event){
        if(keyCode == event.KEYCODE_BACK){
            Intent areaUsuario = new Intent(getApplicationContext(), AreaUsuario.class);
            startActivity(areaUsuario);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    private ArrayList<ParametrosClass> fillParametrosList() {
        ArrayList<ParametrosClass> parametrosList = new ArrayList<>();
        ParametrosClass p1= new ParametrosClass("SurfLoco",  0.2f,0.1f,0.3f,0.2f,0.3f,0.3f, new DatoGradosClass(Directions.SUD),3.f,2.f,4.f,4.f,new DatoGradosClass(Directions.ESTE));
        parametrosList.add(p1);
        parametrosList.add(new ParametrosClass("Surf",  0.2f,0.1f,0.3f,0.2f,0.3f,0.3f, new DatoGradosClass(Directions.SUD),3.f,2.f,4.f,4.f,new DatoGradosClass(Directions.ESTE)));
        parametrosList.add(new ParametrosClass("Playa",  0.2f,0.1f,0.3f,0.2f,0.3f,0.3f, new DatoGradosClass(Directions.SUD),3.f,2.f,4.f,4.f,new DatoGradosClass(Directions.ESTE)));
        parametrosList.add(new ParametrosClass("Vela",  0.2f,0.1f,0.3f,0.2f,0.3f,0.3f, new DatoGradosClass(Directions.SUD),3.f,2.f,4.f,4.f,new DatoGradosClass(Directions.ESTE)));
        parametrosList.add(new ParametrosClass("Kayak",  0.2f,0.1f,0.3f,0.2f,0.3f,0.3f, new DatoGradosClass(Directions.SUD),3.f,2.f,4.f,4.f,new DatoGradosClass(Directions.ESTE)));
        parametrosList.add(new ParametrosClass("LioLegends",  0.2f,0.1f,0.3f,0.2f,0.3f,0.3f, new DatoGradosClass(Directions.SUD),3.f,2.f,4.f,4.f,new DatoGradosClass(Directions.ESTE)));
        parametrosList.add(new ParametrosClass("CallDuty",  0.2f,0.1f,0.3f,0.2f,0.3f,0.3f, new DatoGradosClass(Directions.SUD),3.f,2.f,4.f,4.f,new DatoGradosClass(Directions.ESTE)));
        parametrosList.add(new ParametrosClass("BalonPie",  0.2f,0.1f,0.3f,0.2f,0.3f,0.3f, new DatoGradosClass(Directions.SUD),3.f,2.f,4.f,4.f,new DatoGradosClass(Directions.ESTE)));
        parametrosList.add(new ParametrosClass("DokkanBattle",  0.2f,0.1f,0.3f,0.2f,0.3f,0.3f, new DatoGradosClass(Directions.SUD),3.f,2.f,4.f,4.f,new DatoGradosClass(Directions.ESTE)));
        return parametrosList;
    }
    private List<PublicacionClass> fillPublicacionList() {
        List<PublicacionClass> publicacionList= new ArrayList<PublicacionClass>();
        //String nombreActividad, int userID, float presionMax, float presionMin, float temperaturaMax, float temperaturaMin, float vientoMax, float vientoMin, DatoGradosClass directionViento, float alturaOlaMax, float alturaOlaMin, float periodoOlaMax, float periodoOlaMin, DatoGradosClass directionOlas
        ParametrosClass p1= new ParametrosClass("SurfLoco",  0.2f,0.1f,0.3f,0.2f,0.3f,0.3f, new DatoGradosClass(Directions.NORTE),3.f,2.f,4.f,4.f,new DatoGradosClass(Directions.SUD));
        HashMap<String, Integer> likeList1=new HashMap<>();
        likeList1.put("0000",1);
        likeList1.put("0001",1);
        likeList1.put("0002",0);
        HashMap<String, String> comentariosList1=new HashMap<>();
        comentariosList1.put("Carlos Chun","Oye, ta guapo etooo");
        comentariosList1.put("Oscaroca","Esta guay para disfrutar del restaurante que hay al lado.");
        comentariosList1.put("Mi pana miguel","Viva er beti");
        comentariosList1.put("Racsor","Fumas?");
        comentariosList1.put("Mikol","Jo parlo catalÃ¡");
        comentariosList1.put("Sara","Totorooooo");
        comentariosList1.put("Matt","Has visto como entrenar a tu dragon?");
        comentariosList1.put("Septimus","AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
        publicacionList.add(new PublicacionClass("123131",p1,likeList1,comentariosList1));
        ParametrosClass p2= new ParametrosClass("Surf",  0.2f,0.1f,0.3f,0.2f,0.3f,0.3f, new DatoGradosClass(Directions.SUD),3.f,2.f,4.f,4.f,new DatoGradosClass(Directions.ESTE));
        HashMap<String, Integer> likeList2=new HashMap<>();
        likeList2.put("0000",0);
        likeList2.put("0001",0);
        likeList2.put("0002",0);
        HashMap<String, String> comentariosList2=new HashMap<>();
        comentariosList2.put("0001","---");
        comentariosList2.put("0021","---");
        comentariosList2.put("0031","---");
        publicacionList.add(new PublicacionClass("12131",p2,likeList2,comentariosList2));
        ParametrosClass p3= new ParametrosClass("DokkanBattle", 0.2f,0.1f,0.3f,0.2f,0.3f,0.3f, new DatoGradosClass(Directions.SUD),3.f,2.f,4.f,4.f,new DatoGradosClass(Directions.ESTE));
        HashMap<String, Integer> likeList3=new HashMap<>();
        likeList3.put("0000",1);
        likeList3.put("0001",1);
        likeList3.put("0002",1);
        HashMap<String, String> comentariosList3=new HashMap<>();
        comentariosList3.put("0001","---");
        comentariosList3.put("0021","---");
        publicacionList.add(new PublicacionClass("qawsqwe",p3,likeList3,comentariosList3));

        return publicacionList;
    }

    private void recordarContraseña(String contraseña) {
        SharedPreferences preferencias = getSharedPreferences("contraseña", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=preferencias.edit();
        editor.putString("contraseña", contraseña);
        editor.commit();
    }
}