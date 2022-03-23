package com.example.weadives;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class PantallaLogIn extends AppCompatActivity {

    private Button btn_registrarse, btn_login;
    private TextView txt_LogIn, txt_Correo, txt_contraseña;
    private EditText etA_correo, etP_contraseña;
    private CheckBox chkb_mantenerSession;
    private ImageView btn_home2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pantalla_login);
        Button btn_registrarse = findViewById(R.id.btn_registrarse);
        Button btn_login = findViewById(R.id.btn_login);
        TextView txt_LogIn = findViewById(R.id.txt_LogIn);
        TextView txt_Correo = findViewById(R.id.txt_Correo);
        TextView txt_contraseña = findViewById(R.id.txt_contraseña);
        EditText etA_correo = findViewById(R.id.etA_correo);
        EditText etP_contraseña = findViewById(R.id.etP_contraseña);
        CheckBox chkb_mantenerSession = findViewById(R.id.chkb_mantenerSession);
        ImageView btn_home2 = findViewById(R.id.btn_home2);

        Intent intent = getIntent();

        btn_registrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent register = new Intent(getApplicationContext(), PantallaRegistro.class);
                startActivity(register);
            }
        });
    }
}
