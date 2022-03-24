package com.example.weadives.PantallaRegistro;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.weadives.AreaUsuario.AreaUsuario;
import com.example.weadives.PantallaInicio.PantallaInicio;
import com.example.weadives.R;

public class PantallaRegistro extends AppCompatActivity {

    private TextView txt_correo2, txt_nombre, txt_contraseña2, txt_registro;
    private EditText etA_correo2, etP_contraseña2, etN_nombrepersona;
    private ImageView btn_home3;
    private Button btn_confirmar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pantalla_registro);
        Button btn_confirmar = findViewById(R.id.btn_confirmar);
        TextView txt_correo2 = findViewById(R.id.txt_correo2);
        TextView txt_nombre = findViewById(R.id.txt_nombre);
        TextView txt_contraseña2 = findViewById(R.id.txt_contraseña2);
        TextView txt_registro = findViewById(R.id.txt_registro);
        EditText etA_correo2 = findViewById(R.id.etA_correo2);
        EditText etP_contraseña2 = findViewById(R.id.etP_contraseña2);
        EditText etN_nombrepersona = findViewById(R.id.etN_nombrepersona);
        ImageView btn_home3 = findViewById(R.id.btn_home3);

        Intent intent = getIntent();

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
                Intent areaUsuario = new Intent(getApplicationContext(), AreaUsuario.class);
                startActivity(areaUsuario);
            }
        });

    }
}