package com.example.weadives.AreaUsuario;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weadives.LocaleHelper;
import com.example.weadives.PantallaInicio.PantallaInicio;
import com.example.weadives.PantallaMiPerfil.PantallaMiPerfil;
import com.example.weadives.R;
import com.example.weadives.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class AreaUsuario extends AppCompatActivity {

    private ImageView img_perfil, btn_home4, btn_buscar;
    private TextView txt_nombrePerfil, txt_noAmigos;
    private EditText etT_buscarPorNombre;
    private RecyclerView rv_llistaUsuarios;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private ConstraintLayout constraintLayout;

    private List<UserClass> userList;
    private ViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.area_usuario);
        txt_nombrePerfil = findViewById(R.id.txt_nombrePerfil);
        txt_noAmigos = findViewById(R.id.txt_noAmigos);
        etT_buscarPorNombre = findViewById(R.id.etT_buscarPorNombre);
        btn_home4 = findViewById(R.id.btn_home4);
        btn_buscar = findViewById(R.id.btn_buscar);
        img_perfil = findViewById(R.id.img_perfil);
        rv_llistaUsuarios = findViewById(R.id.rv_llistaUsuarios);
        constraintLayout = findViewById(R.id.constraintLayout);

        viewModel = ViewModel.getInstance(this);
        Intent intent = getIntent();

        final Context context;
        final Resources resources;
        context = LocaleHelper.setLocale(this, cargarPreferencias());
        resources = context.getResources();
        txt_noAmigos.setText(resources.getString(R.string.noAmigos));
        txt_nombrePerfil.setText(viewModel.getCurrentUser().getUsername());

        userList = fillUserList();
        System.out.println(userList);

        if (!userList.isEmpty()){
            txt_noAmigos.setVisibility(View.INVISIBLE);
            //mejorar performance
            rv_llistaUsuarios.hasFixedSize();
            //lineal layout
            layoutManager = new LinearLayoutManager(this);
            rv_llistaUsuarios.setLayoutManager(layoutManager);
            //especificar adapter
            mAdapter= new UserListAdapter(userList,AreaUsuario.this);
            rv_llistaUsuarios.setAdapter(mAdapter);
        } else {
            txt_noAmigos.setVisibility(View.VISIBLE);
        }

        btn_home4.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if(!viewModel.getLogInStatus()){
                    viewModel.singOut();
                    recordarUser("false");
                }
                Intent pantallaInicio = new Intent(getApplicationContext(), PantallaInicio.class);
                startActivity(pantallaInicio);
            }
        });

        btn_buscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!etT_buscarPorNombre.getText().toString().equals("")){
                    userList = viewModel.buscarPorNombre(etT_buscarPorNombre.getText().toString());
                }
            }
        });

        constraintLayout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                txt_noAmigos.setVisibility(View.INVISIBLE);
                Intent pantallaMiperfil = new Intent(getApplicationContext(), PantallaMiPerfil.class);
                startActivity(pantallaMiperfil);
            }
        });
    }

    private List<UserClass> fillUserList() {//String id, String username, String correo, String urlImg)
        UserClass user;
        List<UserClass> listaUsers = new ArrayList<>();

        if (viewModel.getCurrentUser().getListaSolicitudesRecibidas().size() > 0) {
            for (String uid : viewModel.getCurrentUser().getListaSolicitudesRecibidas()) {
                user = viewModel.getUserByUID(uid);
                if (user != null) {
                    listaUsers.add(user);
                }
            }
        }

        if (viewModel.getCurrentUser().getListaAmigos().size() > 0) {
            for (String uid : viewModel.getCurrentUser().getListaAmigos()) {
                user = viewModel.getUserByUID(uid);
                if (user != null) {
                    listaUsers.add(user);
                }
            }
        }
        return listaUsers;
    }

    private void recordarUser(String s) {
        SharedPreferences preferencias = getSharedPreferences("recuerda", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=preferencias.edit();
        editor.putString("recuerda", s);
        editor.commit();
    }

    private String cargarPreferencias() {
        SharedPreferences preferencias = getSharedPreferences("idioma",Context.MODE_PRIVATE);
        return preferencias.getString("idioma","en");
    }
}
