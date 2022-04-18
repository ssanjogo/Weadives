package com.example.weadives.PantallaLogIn;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.weadives.AreaUsuario.AreaUsuario;
import com.example.weadives.DatabaseAdapter;
import com.example.weadives.LocaleHelper;
import com.example.weadives.PantallaInicio.PantallaInicio;
import com.example.weadives.PantallaRegistro.PantallaRegistro;
import com.example.weadives.R;

public class PantallaLogIn extends AppCompatActivity {

    private Button btn_registrarse, btn_login;
    private TextView txt_LogIn, txt_Correo, txt_contraseña, txt_nombre, txt_id;
    private EditText etA_correo, etP_contraseña;
    private CheckBox chkb_mantenerSession;
    private ImageView btn_home2;

    private DatabaseAdapter dbA;
    boolean login = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pantalla_login);
        btn_registrarse = findViewById(R.id.btn_registrarse);
        btn_login = findViewById(R.id.btn_login);
        txt_LogIn = findViewById(R.id.txt_LogIn);
        txt_Correo = findViewById(R.id.txt_Correo);
        txt_contraseña = findViewById(R.id.txt_contraseña);
        etA_correo = findViewById(R.id.etA_correo);
        etP_contraseña = findViewById(R.id.etP_contraseña);
        chkb_mantenerSession = findViewById(R.id.chkb_mantenerSession);
        btn_home2 = findViewById(R.id.btn_home2);

        final Context context;
        final Resources resources;
        context = LocaleHelper.setLocale(this, cargarPreferencias());
        resources = context.getResources();
        txt_Correo.setText(resources.getString(R.string.correo));
        txt_contraseña.setText(resources.getString(R.string.password2));
        chkb_mantenerSession.setText(resources.getString(R.string.mantener_sessi_n_iniciada));
        btn_registrarse.setText(resources.getString(R.string.btn_registrarse));

        dbA = DatabaseAdapter.getInstance();

        if(!dbA.currentUser()) {
            if (cargarUser().equals("true")) {
                Intent areaUsuario = new Intent(getApplicationContext(), AreaUsuario.class);
                startActivity(areaUsuario);
            }
        }else {
            dbA.singout();
            //etA_correo.setText(cargarCorreo());
            //etP_contraseña.setText(cargarContraseña());
        }

        Intent intent = getIntent();

        btn_home2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent pantallaInicio = new Intent(getApplicationContext(), PantallaInicio.class);
                startActivity(pantallaInicio);
            }
        });

        btn_registrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Animation animation= AnimationUtils.loadAnimation(context,R.anim.blink_anim2);
                btn_registrarse.startAnimation(animation);
                Intent register = new Intent(getApplicationContext(), PantallaRegistro.class);
                startActivity(register);
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Animation animation= AnimationUtils.loadAnimation(context,R.anim.blink_anim2);
                btn_login.startAnimation(animation);
                if (etA_correo.getText().toString().equals("") && etP_contraseña.getText().toString().equals("")){
                    etA_correo.setError("Campo sin rellenar");
                    etP_contraseña.setError("Campo sin rellenar");
                } else if (etA_correo.getText().toString().equals("")){
                    etA_correo.setError("Campo sin rellenar");
                } else if (etP_contraseña.getText().toString().equals("")){
                    etP_contraseña.setError("Campo sin rellenar");
                } else {
                    login = dbA.logIn(etA_correo.getText().toString(), etP_contraseña.getText().toString());
                    if (chkb_mantenerSession.isChecked()) {
                        dbA.setLogInStatus(true);
                        recordarUser();
                        recordarCorreo(etA_correo.getText().toString());
                        recordarContraseña(etP_contraseña.getText().toString());
                    }
                    if (login) {
                        Intent areaUsuario = new Intent(getApplicationContext(), AreaUsuario.class);
                        areaUsuario.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(areaUsuario);
                        finish();
                    }
                }

            }
        });
    }

    private void guardarPreferencias(String string) {
        SharedPreferences preferencias = getSharedPreferences("idioma",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=preferencias.edit();
        editor.putString("idioma",string);
        editor.commit();
    }

    private String cargarPreferencias() {
        SharedPreferences preferencias = getSharedPreferences("idioma",Context.MODE_PRIVATE);
        return preferencias.getString("idioma","en");
    }

    private void recordarCorreo(String correo) {
        SharedPreferences preferencias = getSharedPreferences("user", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=preferencias.edit();
        editor.putString("user",correo);
        editor.commit();
    }
    private String cargarCorreo() {
        SharedPreferences preferencias = getSharedPreferences("user",Context.MODE_PRIVATE);
        return preferencias.getString("user","");
    }

    private void recordarContraseña(String contraseña) {
        SharedPreferences preferencias = getSharedPreferences("contraseña", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=preferencias.edit();
        editor.putString("contraseña", contraseña);
        editor.commit();
    }
    private String cargarContraseña() {
        SharedPreferences preferencias = getSharedPreferences("contraseña",Context.MODE_PRIVATE);
        return preferencias.getString("contraseña","");
    }

    private void recordarUser() {
        SharedPreferences preferencias = getSharedPreferences("recuerda",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=preferencias.edit();
        editor.putString("recuerda", "true");
        editor.commit();
    }

    private String cargarUser() {
        SharedPreferences preferencias = getSharedPreferences("recuerda",Context.MODE_PRIVATE);
        return preferencias.getString("recuerda","false");
    }
}
