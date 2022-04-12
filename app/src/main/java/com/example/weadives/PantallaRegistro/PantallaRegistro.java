package com.example.weadives.PantallaRegistro;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.weadives.AreaUsuario.AreaUsuario;
import com.example.weadives.DatabaseAdapter;
import com.example.weadives.PantallaInicio.PantallaInicio;
import com.example.weadives.R;

public class PantallaRegistro extends AppCompatActivity {

    private TextView txt_correo2, txt_nombre, txt_contraseña2, txt_registro;
    private EditText etA_correo2, etP_contraseña2, etN_nombrepersona;
    private ImageView btn_home3;
    private Button btn_confirmar;

    private viewModelRegistro vmr;

    private DatabaseAdapter dbA;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pantalla_registro);
        Button btn_confirmar = findViewById(R.id.btn_confirmar);
        EditText etA_correo2 = findViewById(R.id.etA_correo2);
        EditText etP_contraseña2 = findViewById(R.id.etP_contraseña2);
        EditText etN_nombrepersona = findViewById(R.id.etN_nombrepersona);
        ImageView btn_home3 = findViewById(R.id.btn_home3);

        Intent intent = getIntent();
        vmr = new viewModelRegistro();
        DatabaseAdapter dbA = new DatabaseAdapter();

        btn_home3.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent pantallaInicio = new Intent(getApplicationContext(), PantallaInicio.class);
                startActivity(pantallaInicio);
            }
        });



        btn_confirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nombre = etN_nombrepersona.getText().toString();
                String correo = etA_correo2.getText().toString();
                String contraseña = etP_contraseña2.getText().toString();

                if (!vmr.validarEmail(correo)){
                    etA_correo2.setError("Email no válido");
                }
                String iduser = vmr.createUserID();

                if (dbA.addUser(nombre, correo, contraseña, iduser)){
                    Intent areaUsuario = new Intent(getApplicationContext(), AreaUsuario.class);
                    startActivity(areaUsuario);
                } else {
                    etA_correo2.setError("Este email ya ha sido registrado.");
                }
            }
        });
    }
}