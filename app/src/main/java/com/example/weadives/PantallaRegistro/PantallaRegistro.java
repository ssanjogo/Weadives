package com.example.weadives.PantallaRegistro;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.weadives.AreaUsuario.AreaUsuario;
import com.example.weadives.LocaleHelper;
import com.example.weadives.PantallaInicio.PantallaInicio;
import com.example.weadives.R;
import com.example.weadives.ViewModel;

import java.util.regex.Pattern;

public class PantallaRegistro extends AppCompatActivity {

    private ViewModel viewModel;

    private TextView txt_correo2, txt_nombre, txt_contraseña2, txt_registro;
    private EditText etA_correo2, etP_contraseña2, etN_nombrepersona;
    private ImageView btn_home3;
    private Button btn_confirmar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pantalla_registro);
        btn_confirmar = findViewById(R.id.btn_confirmar);
        etA_correo2 = findViewById(R.id.etA_correo2);
        etP_contraseña2 = findViewById(R.id.etP_contraseña2);
        etN_nombrepersona = findViewById(R.id.etN_nombrepersona);
        btn_home3 = findViewById(R.id.btn_home3);
        txt_correo2 = findViewById(R.id.txt_correo2);
        txt_contraseña2 = findViewById(R.id.txt_contraseña2);
        txt_nombre = findViewById(R.id.txt_nombre);
        etN_nombrepersona = findViewById(R.id.etN_nombrepersona);
        btn_confirmar = findViewById(R.id.btn_confirmar);
        txt_registro =findViewById(R.id.txt_registro);

        final Context context;
        final Resources resources;
        context = LocaleHelper.setLocale(this, cargarPreferencias());
        resources = context.getResources();
        txt_registro.setText(resources.getString(R.string.registro));
        txt_correo2.setText(resources.getString(R.string.correo));
        txt_contraseña2.setText(resources.getString(R.string.password2));
        txt_nombre.setText(resources.getString(R.string.nombre));
        etN_nombrepersona.setHint(resources.getString(R.string.nombre2));
        btn_confirmar.setText(resources.getString(R.string.confirmar));

        viewModel = ViewModel.getInstance(this);
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
                Animation animation= AnimationUtils.loadAnimation(context,R.anim.blink_anim2);
                btn_confirmar.startAnimation(animation);

                String nombre = etN_nombrepersona.getText().toString();
                String correo = etA_correo2.getText().toString();
                String contraseña = etP_contraseña2.getText().toString();

                if (!validarEmail(correo)){
                    etA_correo2.setError("Email no válido");
                }
                if(viewModel.correoRepetido(correo)){
                    etA_correo2.setError("Correo existente");
                } else {
                    viewModel.register(nombre, correo, contraseña);
                    //if (viewModel.getCurrentUser().getCorreo().equals(correo) && viewModel.getCurrentUser().getUsername().equals(nombre)) {
                        recordarUser();
                        recordarCorreo(correo);
                        Intent areaUsuario = new Intent(getApplicationContext(), AreaUsuario.class);
                        areaUsuario.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(areaUsuario);
                    //}
                }
            }
        });
    }

    private boolean validarEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }

    private String cargarPreferencias() {
        SharedPreferences preferencias = getSharedPreferences("idioma", Context.MODE_PRIVATE);
        return preferencias.getString("idioma","en");
    }

    private void recordarCorreo(String correo) {
        SharedPreferences preferencias = getSharedPreferences("user", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferencias.edit();
        editor.putString("user", correo);
        editor.commit();
    }

    private void recordarUser() {
        SharedPreferences preferencias = getSharedPreferences("recuerda",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=preferencias.edit();
        editor.putString("recuerda", "true");
        editor.commit();
    }
}