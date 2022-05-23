package com.example.weadives.PantallaPerfilAmigo;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.weadives.AjustesPerfil.AjustesPerfil;
import com.example.weadives.AreaUsuario.AreaUsuario;
import com.example.weadives.AreaUsuario.UserClass;
import com.example.weadives.DatabaseAdapter;
import com.example.weadives.DatoGradosClass;
import com.example.weadives.Directions;
import com.example.weadives.LocaleHelper;
import com.example.weadives.PantallaInicio.PantallaInicio;
import com.example.weadives.ParametrosClass;
import com.example.weadives.R;
import com.example.weadives.SingletonIdioma;
import com.example.weadives.ViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PantallaPerfilAmigo extends AppCompatActivity {

    private ImageView img_perfil, btn_home;
    private TextView txt_nombrePerfil, txt_codigo,emptyView;
    private RecyclerView recyclerView;
    private Button btn_añadirAmigo;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<PublicacionClass> publicacionList;
    private ViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pantalla_perfil_amigo);
        img_perfil = findViewById(R.id.img_perfil3);
        btn_home = findViewById(R.id.btn_home11);
        txt_nombrePerfil = findViewById(R.id.txt_nombrePerfil3);
        btn_añadirAmigo = findViewById(R.id.btn_añadirAmigo);
        emptyView= findViewById(R.id.empty_view);
        RecyclerView recyclerView = findViewById(R.id.rv_llistaAjustes3);

        //publicacionList = fillPublicacionList();

        publicacionList= new ArrayList<PublicacionClass>();



        //mejorar performance
        recyclerView.hasFixedSize();
        //lineal layout
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        //especificar adapter
        mAdapter= new PublicacionesPerfilAdapter(publicacionList, PantallaPerfilAmigo.this);
        recyclerView.setAdapter(mAdapter);

        final Context context;
        SingletonIdioma s= SingletonIdioma.getInstance();
        Resources resources=s.getResources();
        btn_añadirAmigo.setText(resources.getString(R.string.añadir_amigo));

        viewModel = ViewModel.getInstance(this);
        Intent intent = getIntent();

        String username = intent.getStringExtra("username");
        String idAmigo = intent.getStringExtra("id");
        String imagen = intent.getStringExtra("Imagen");

        txt_nombrePerfil.setText(username);
        Glide.with(this).load(imagen).into(img_perfil);

        if (viewModel.uidInListaAmigos(idAmigo)){
            btn_añadirAmigo.setText(resources.getString(R.string.añadido));
            btn_añadirAmigo.setBackground(resources.getDrawable(R.drawable.button_rounded_grey));
            btn_añadirAmigo.setTextColor(resources.getColor(R.color.black));
        } else if (viewModel.uidInListaSolicitudesEnviadas(idAmigo)){
            btn_añadirAmigo.setText(resources.getString(R.string.pendiente));
            btn_añadirAmigo.setBackground(resources.getDrawable(R.drawable.button_rounded_grey));
            btn_añadirAmigo.setTextColor(resources.getColor(R.color.black));
        }

        //CASO NO FRIEND
        if (!btn_añadirAmigo.getText().equals(resources.getString(R.string.añadido))){
            recyclerView.setVisibility(View.GONE);
            emptyView.setText(resources.getString(R.string.no_friend));
            emptyView.setVisibility(View.VISIBLE);
        }
        else if (publicacionList.isEmpty()) {
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
                if(!viewModel.getLogInStatus()){
                    viewModel.singOut();
                }
                Intent pantallaInicio = new Intent(getApplicationContext(), PantallaInicio.class);
                startActivity(pantallaInicio);
            }
        });

        btn_añadirAmigo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (btn_añadirAmigo.getText().equals(resources.getString(R.string.añadir_amigo))){
                    btn_añadirAmigo.setText(resources.getString(R.string.pendiente));
                    btn_añadirAmigo.setBackground(resources.getDrawable(R.drawable.button_rounded_grey));
                    btn_añadirAmigo.setTextColor(resources.getColor(R.color.black));
                    //FALTA
                    //Enviar solicitud
                    viewModel.enviarsolicitud(idAmigo);
                } else if (btn_añadirAmigo.getText().equals(resources.getString(R.string.pendiente))){
                    System.out.println(btn_añadirAmigo.getText());
                    System.out.println("pasa");
                    btn_añadirAmigo.setText(resources.getString(R.string.añadir_amigo));
                    btn_añadirAmigo.setBackground(resources.getDrawable(R.drawable.button_rounded));
                    btn_añadirAmigo.setTextColor(resources.getColor(R.color.white));
                    //FALTA
                    //Eliminar solicitud enviada
                    viewModel.cancelarEvioSolicitud(idAmigo);
                } else {
                    AlertDialog.Builder alerta = new AlertDialog.Builder(PantallaPerfilAmigo.this);
                    alerta.setMessage(resources.getString(R.string.alertaDejarDeSeguir)).setCancelable(true).setPositiveButton(resources.getString(R.string.afirmativo), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            viewModel.unfollow(idAmigo);
                            btn_añadirAmigo.setText(resources.getString(R.string.añadir_amigo));
                            btn_añadirAmigo.setBackground(resources.getDrawable(R.drawable.button_rounded));
                            btn_añadirAmigo.setTextColor(resources.getColor(R.color.white));
                        }
                    }).setNegativeButton(resources.getString(R.string.negativo), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });
                    AlertDialog titulo = alerta.create();
                    titulo.setTitle(resources.getString(R.string.eliminarCuenta));
                    titulo.show();
                }
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
        comentariosList1.put("Mikol","Jo parlo catalá");
        comentariosList1.put("Sara","Totorooooo");
        comentariosList1.put("Matt","Has visto como entrenar a tu dragon?");
        comentariosList1.put("Septimus","AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
        publicacionList.add(new PublicacionClass("12awq22",p1,likeList1,comentariosList1));
        ParametrosClass p2= new ParametrosClass("Surf",  0.2f,0.1f,0.3f,0.2f,0.3f,0.3f, new DatoGradosClass(Directions.SUD),3.f,2.f,4.f,4.f,new DatoGradosClass(Directions.ESTE));
        HashMap<String, Integer> likeList2=new HashMap<>();
        likeList2.put("0000",0);
        likeList2.put("0001",0);
        likeList2.put("0002",0);
        HashMap<String, String> comentariosList2=new HashMap<>();
        comentariosList2.put("0001","---");
        comentariosList2.put("0021","---");
        comentariosList2.put("0031","---");
        publicacionList.add(new PublicacionClass("AWQW",p2,likeList2,comentariosList2));
        ParametrosClass p3= new ParametrosClass("DokkanBattle", 0.2f,0.1f,0.3f,0.2f,0.3f,0.3f, new DatoGradosClass(Directions.SUD),3.f,2.f,4.f,4.f,new DatoGradosClass(Directions.ESTE));
        HashMap<String, Integer> likeList3=new HashMap<>();
        likeList3.put("0000",1);
        likeList3.put("0001",1);
        likeList3.put("0002",1);
        HashMap<String, String> comentariosList3=new HashMap<>();
        comentariosList3.put("0001","---");
        comentariosList3.put("0021","---");
        publicacionList.add(new PublicacionClass("asdqdw",p3,likeList3,comentariosList3));

        return publicacionList;
    }

    private String cargarPreferencias() {
        SharedPreferences preferencias = getSharedPreferences("idioma",Context.MODE_PRIVATE);
        return preferencias.getString("idioma","en");
    }


}