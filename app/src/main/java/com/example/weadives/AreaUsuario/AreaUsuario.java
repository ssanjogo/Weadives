package com.example.weadives.AreaUsuario;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weadives.DatabaseAdapter;
import com.example.weadives.PantallaInicio.PantallaInicio;
import com.example.weadives.PantallaMiPerfil.PantallaMiPerfil;
import com.example.weadives.R;

public class AreaUsuario extends AppCompatActivity {

    private ImageView img_perfil, btn_home4, btn_añadir;
    private TextView txt_nombrePerfil, txt_codigo;
    private EditText etT_buscarPorCodigo;
    private RecyclerView rv_llistaUsuarios;
    private DatabaseAdapter dbA;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.area_usuario);
        txt_nombrePerfil = findViewById(R.id.txt_nombrePerfil);
        etT_buscarPorCodigo = findViewById(R.id.etT_buscarPorCodigo);
        btn_home4 = findViewById(R.id.btn_home4);
        btn_añadir = findViewById(R.id.btn_añadir);
        img_perfil = findViewById(R.id.img_perfil);
        rv_llistaUsuarios = findViewById(R.id.rv_llistaUsuarios);
        ConstraintLayout constraintLayout = findViewById(R.id.constraintLayout);

        Intent intent = getIntent();

        dbA = DatabaseAdapter.getInstance();

        dbA.setName(txt_nombrePerfil);

        btn_home4.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if(!dbA.getLogInStatus()){
                    dbA.singout();
                }
                Intent pantallaInicio = new Intent(getApplicationContext(), PantallaInicio.class);
                startActivity(pantallaInicio);
            }
        });
        constraintLayout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent pantallaMiperfil = new Intent(getApplicationContext(), PantallaMiPerfil.class);
                startActivity(pantallaMiperfil);
            }
        });

    }

}
