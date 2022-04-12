package com.example.weadives.PantallaLogIn;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
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
import com.example.weadives.PantallaPrincipal.PantallaPrincipal;
import com.example.weadives.PantallaRegistro.PantallaRegistro;
import com.example.weadives.R;

public class PantallaLogIn extends AppCompatActivity {

    private Button btn_registrarse, btn_login;
    private TextView txt_LogIn, txt_Correo, txt_contraseña;
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
        dbA = new DatabaseAdapter();


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
                Intent register = new Intent(getApplicationContext(), PantallaRegistro.class);
                startActivity(register);
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("Antes de llamar al metodo");
                login = dbA.logIn(etA_correo.getText().toString(), etP_contraseña.getText().toString());
                System.out.println("devuelve" + login);
                if (login){
                    Intent areaUsuario = new Intent(getApplicationContext(), AreaUsuario.class);
                    startActivity(areaUsuario);
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
}
