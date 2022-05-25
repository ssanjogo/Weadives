package com.example.weadives.AreaUsuario;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.weadives.LocaleHelper;
import com.example.weadives.PantallaInicio.PantallaInicio;
import com.example.weadives.PantallaMiPerfil.PantallaMiPerfil;
import com.example.weadives.PantallaPrincipal.PantallaPrincipal;
import com.example.weadives.R;
import com.example.weadives.ViewModel;

import java.util.List;

public class AreaUsuario extends AppCompatActivity {

    private ImageView img_perfil, btn_home4, btn_buscar;
    private TextView txt_nombrePerfil, txt_noAmigos;
    private EditText etT_buscarPorNombre;
    private RecyclerView rv_llistaUsuarios;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private ConstraintLayout constraintLayout;
    private String correo;
    private int limite;

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
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        etT_buscarPorNombre.setEnabled(false);

        viewModel = ViewModel.getInstance(this);
        Intent intent = getIntent();

        final Context context;
        final Resources resources;
        context = LocaleHelper.setLocale(this, cargarPreferencias());
        resources = context.getResources();
        txt_noAmigos.setText(resources.getString(R.string.noAmigos));


        txt_nombrePerfil.setText(viewModel.getCurrentUser().getUsername());
        Glide.with(this).load(viewModel.getCurrentUser().getUrlImg()).into(img_perfil);

        System.out.println("TOKEEEEEEEEEEEEEEEEEEEEN " + recuperarToken());

        correo = viewModel.getCurrentUser().getCorreo();
        limite = viewModel.sizelista();
        viewModel.fillUserList();
        userList = viewModel.getListaRecyclerView().getValue();
        setLiveDataObservers();

        txt_noAmigos.setVisibility(View.INVISIBLE);
        //mejorar performance
        rv_llistaUsuarios.hasFixedSize();
        //lineal layout
        layoutManager = new LinearLayoutManager(this);
        rv_llistaUsuarios.setLayoutManager(layoutManager);

        if (!userList.isEmpty()){
            mAdapter = new UserListAdapter(userList,AreaUsuario.this, viewModel, limite);
            rv_llistaUsuarios.setAdapter(mAdapter);
        } else {
            txt_noAmigos.setVisibility(View.VISIBLE);
        }

        constraintLayout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                txt_noAmigos.setVisibility(View.INVISIBLE);
                Intent pantallaMiperfil = new Intent(getApplicationContext(), PantallaMiPerfil.class);
                pantallaMiperfil.setAction(Intent.ACTION_OPEN_DOCUMENT);
                startActivity(pantallaMiperfil);
            }
        });

        btn_home4.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent pantallaInicio = new Intent(getApplicationContext(), PantallaInicio.class);
                startActivity(pantallaInicio);
                finish();
            }
        });

        btn_buscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int i = 0;
                if(etT_buscarPorNombre.isEnabled()){
                    etT_buscarPorNombre.setText("");
                    etT_buscarPorNombre.setEnabled(false);
                    btn_buscar.setImageResource(R.drawable.btn_buscar);
                    i = 0;
                } else {
                    etT_buscarPorNombre.setEnabled(true);
                    btn_buscar.setImageResource(R.drawable.cruz);
                    i = 1;
                }
                if(i == 0){
                    viewModel.fillUserList();
                    if (viewModel.getListaRecyclerView().getValue().isEmpty()){
                        txt_noAmigos.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        etT_buscarPorNombre.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                txt_noAmigos.setVisibility(View.INVISIBLE);
                if (!etT_buscarPorNombre.getText().toString().equals("")) {
                    viewModel.buscarPorNombre(etT_buscarPorNombre.getText().toString());
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    public void setLiveDataObservers() {
        //Subscribe the activity to the observable
        viewModel = new ViewModelProvider(this).get(ViewModel.class);

        final Observer<List<UserClass>> observer = new Observer<List<UserClass>>() {
            @Override
            public void onChanged(List<UserClass> ac) {
                UserListAdapter newAdapter = new UserListAdapter(viewModel.getListaRecyclerView().getValue(),AreaUsuario.this, viewModel, limite);
                rv_llistaUsuarios.swapAdapter(newAdapter, true);
                newAdapter.notifyDataSetChanged();
            }
        };
        viewModel.getListaRecyclerView().observe(this, observer);
    }

    private String cargarPreferencias() {
        SharedPreferences preferencias = getSharedPreferences("idioma",Context.MODE_PRIVATE);
        return preferencias.getString("idioma","en");
    }

    private String recuperarToken() {
        SharedPreferences preferencias = getSharedPreferences("token",Context.MODE_PRIVATE);
        return preferencias.getString("token","");
    }

    public boolean onKeyDown(int keyCode, KeyEvent event){
        if(keyCode == event.KEYCODE_BACK){
            Intent areaUsuario = new Intent(getApplicationContext(), AreaUsuario.class);
            startActivity(areaUsuario);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}
