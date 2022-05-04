package com.example.weadives.AjustesPerfil;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.weadives.DatabaseAdapter;
import com.example.weadives.LocaleHelper;
import com.example.weadives.PantallaInicio.PantallaInicio;
import com.example.weadives.PantallaMiPerfil.PantallaMiPerfil;
import com.example.weadives.PantallaPerfilAmigo.PantallaPerfilAmigo;
import com.example.weadives.R;
import com.example.weadives.ViewModel;

public class AjustesPerfil extends AppCompatActivity {

    private ViewModel viewModel;

    private TextView txt_correo, txt_nombre2, txt_contraseña3;
    private EditText etA_correo3, etP_contraseña3, etN_nombrepersona2;
    private ImageView btn_home6, img_perfil;
    private Button btn_guardarCambios, btn_eliminarCuenta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ajustes_perfil);
        btn_guardarCambios = findViewById(R.id.btn_guardarCambios);
        btn_eliminarCuenta = findViewById(R.id.btn_eliminarCuenta);
        txt_correo = findViewById(R.id.txt_correo);
        txt_contraseña3 = findViewById(R.id.txt_contraseña3);
        txt_nombre2 = findViewById(R.id.txt_nombre2);
        etA_correo3 = findViewById(R.id.etA_correo3);
        etP_contraseña3 = findViewById(R.id.etP_contraseña3);
        etN_nombrepersona2 = findViewById(R.id.etN_nombrepersona2);;
        btn_home6 = findViewById(R.id.btn_home6);
        img_perfil = findViewById(R.id.img_perfil);

        final Context context;
        final Resources resources;
        context = LocaleHelper.setLocale(this, cargarPreferencias());
        resources = context.getResources();
        txt_nombre2.setText(resources.getString(R.string.nombre));
        txt_contraseña3.setText(resources.getString(R.string.password2));
        txt_correo.setText(resources.getString(R.string.correo));
        btn_guardarCambios.setText(resources.getString(R.string.guardar_cambios));
        btn_eliminarCuenta.setText(resources.getString(R.string.eliminarCuenta));

        viewModel = ViewModel.getInstance(this);
        Intent intent = getIntent();

        etN_nombrepersona2.setText(viewModel.getCurrentUser().getUsername());
        etA_correo3.setText(viewModel.getCurrentUser().getCorreo());


        btn_home6.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if(!viewModel.getLogInStatus()){
                    viewModel.singOut();
                }
                Intent pantallaInicio = new Intent(getApplicationContext(), PantallaInicio.class);
                startActivity(pantallaInicio);
                finish();
            }
        });

        btn_guardarCambios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etA_correo3.getText() != null && etN_nombrepersona2.getText() != null){
                    if(!etA_correo3.getText().toString().equals(viewModel.getCurrentUser().getCorreo())){
                        viewModel.cambiarCorreo(etA_correo3.getText().toString());
                    }
                    if(!etN_nombrepersona2.getText().toString().equals(viewModel.getCurrentUser().getUsername())){
                        viewModel.cambiarNombre(etN_nombrepersona2.getText().toString());
                    }
                    if (etP_contraseña3.getText().toString() != null){
                        System.out.println(etP_contraseña3.getText().toString());
                        AlertDialog.Builder alerta = new AlertDialog.Builder(AjustesPerfil.this);
                        alerta.setMessage(resources.getString(R.string.alertaCambiarContraseña)).setCancelable(true).setPositiveButton(resources.getString(R.string.afirmativo), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                viewModel.cambiarContraseña(etP_contraseña3.getText().toString());
                                finish();
                            }
                        }).setNegativeButton(resources.getString(R.string.negativo), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });
                        AlertDialog titulo = alerta.create();
                        titulo.setTitle(resources.getString(R.string.cambiarContraseña));
                        titulo.show();
                    }
                }
            }
        });

        btn_eliminarCuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alerta = new AlertDialog.Builder(AjustesPerfil.this);
                alerta.setMessage(resources.getString(R.string.alertaEliminarCuenta)).setCancelable(true).setPositiveButton(resources.getString(R.string.afirmativo), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        viewModel.deleteAccount();
                    }
                }).setNegativeButton(resources.getString(R.string.negativo), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                AlertDialog titulo = alerta.create();
                titulo.setTitle(resources.getString(R.string.eliminarCuenta));
                titulo.show();

                Intent pantallaInicio = new Intent(getApplicationContext(), PantallaInicio.class);
                startActivity(pantallaInicio);
            }
        });

        img_perfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent imagenPerfil = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                imagenPerfil.setType("image");
                startActivityForResult(Intent.createChooser(imagenPerfil, "Seleccionar apliccación"), 10);

            }
        });
    }

    private String cargarPreferencias() {
        SharedPreferences preferencias = getSharedPreferences("idioma", Context.MODE_PRIVATE);
        return preferencias.getString("idioma","en");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            Uri path = data.getData();
            img_perfil.setImageURI(path);
        }
    }
}
