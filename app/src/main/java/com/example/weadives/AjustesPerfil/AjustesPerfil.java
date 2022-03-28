package com.example.weadives.AjustesPerfil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.weadives.AreaUsuario.AreaUsuario;
import com.example.weadives.PantallaInicio.PantallaInicio;
import com.example.weadives.R;

public class AjustesPerfil extends AppCompatActivity {

    private TextView txt_correo, txt_nombre2, txt_contraseña3, txt_nombrePerfil2, txt_codigo2;
    private EditText etA_correo3, etP_contraseña3, etN_nombrepersona2;
    private ImageView btn_home6, img_perfil, btn_edtitarN, btn_editarC, btn_editarP;
    private Button btn_guardarCambios;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ajustes_perfil);
        Button btn_guardarCambios = findViewById(R.id.btn_guardarCambios);
        ImageView btn_home6 = findViewById(R.id.btn_home6);

        Intent intent = getIntent();

        btn_home6.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent pantallaInicio = new Intent(getApplicationContext(), PantallaInicio.class);
                startActivity(pantallaInicio);
            }
        });

        btn_guardarCambios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent areaUsuario = new Intent(getApplicationContext(), AreaUsuario.class);
                startActivity(areaUsuario);
            }
        });

    }
}
