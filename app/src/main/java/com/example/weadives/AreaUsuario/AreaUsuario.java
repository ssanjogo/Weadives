package com.example.weadives.AreaUsuario;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weadives.PantallaInicio.PantallaInicio;
import com.example.weadives.R;

public class AreaUsuario extends AppCompatActivity {

    private ImageView img_perfil, btn_home4, btn_añadir;
    private TextView txt_nombrePerfil, txt_codigo;
    private EditText etT_buscarPorCodigo;
    private RecyclerView rv_llistaUsuarios;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.area_usuario);
        TextView txt_nombrePerfil = findViewById(R.id.txt_nombrePerfil);
        TextView txt_codigo = findViewById(R.id.txt_codigo);
        EditText etT_buscarPorCodigo = findViewById(R.id.etT_buscarPorCodigo);
        ImageView btn_home4 = findViewById(R.id.btn_home4);
        ImageView btn_añadir = findViewById(R.id.btn_añadir);
        ImageView img_perfil = findViewById(R.id.img_perfil);
        RecyclerView rv_llistaUsuarios = findViewById(R.id.rv_llistaUsuarios);

        Intent intent = getIntent();

        btn_home4.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent pantallaInicio = new Intent(getApplicationContext(), PantallaInicio.class);
                startActivity(pantallaInicio);
            }
        });

    }

}
