package com.example.weadives.AjustesPerfil;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.example.weadives.AreaUsuario.AreaUsuario;
import com.example.weadives.ViewModelAndExtras.LocaleHelper;
import com.example.weadives.PantallaInicio.PantallaInicio;
import com.example.weadives.PantallaMiPerfil.PantallaMiPerfil;
import com.example.weadives.R;
import com.example.weadives.ViewModelAndExtras.ViewModel;
import com.squareup.picasso.Picasso;

public class AjustesPerfil extends AppCompatActivity {

    private ViewModel viewModel;

    private TextView txt_correo, txt_nombre2, txt_contraseña3;
    private EditText etA_correo3, etP_contraseña3, etN_nombrepersona2;
    private ImageView btn_home6, img_perfil;
    private Button btn_guardarCambios, btn_eliminarCuenta, btn_eliminarImagen;

    private Uri imageUri;
    private static final int REQUEST_CODE = 200;
    private static final int PICK_IMAGE = 100;

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
        btn_eliminarImagen = findViewById(R.id.btn_deleteImage);

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
        Picasso.with(this).load(viewModel.getUrl()).into(img_perfil);
        Animation animation= AnimationUtils.loadAnimation(context,R.anim.blink_anim2);
        btn_home6.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                btn_home6.startAnimation(animation);
                Intent pantallaInicio = new Intent(getApplicationContext(), PantallaInicio.class);
                startActivity(pantallaInicio);
                finish();
            }
        });

        btn_eliminarImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn_eliminarImagen.startAnimation(animation);
                AlertDialog.Builder alerta = new AlertDialog.Builder(AjustesPerfil.this);
                alerta.setMessage(resources.getString(R.string.alertaEliminarImagen)).setCancelable(true).setPositiveButton(resources.getString(R.string.afirmativo), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Picasso.with(getApplicationContext()).load("https://firebasestorage.googleapis.com/v0/b/weadives.appspot.com/o/Imagenes_Perfil%2FprofillePicBase.png?alt=media&token=544d8b5c-11de-4acb-9bdd-54bb5f5297af").into(img_perfil);
                        viewModel.setImage("https://firebasestorage.googleapis.com/v0/b/weadives.appspot.com/o/Imagenes_Perfil%2FprofillePicBase.png?alt=media&token=544d8b5c-11de-4acb-9bdd-54bb5f5297af");
                        viewModel.cambiarImagen2("https://firebasestorage.googleapis.com/v0/b/weadives.appspot.com/o/Imagenes_Perfil%2FprofillePicBase.png?alt=media&token=544d8b5c-11de-4acb-9bdd-54bb5f5297af");
                    }
                }).setNegativeButton(resources.getString(R.string.negativo), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                AlertDialog titulo = alerta.create();
                titulo.setTitle(resources.getString(R.string.eliminarImagen));
                titulo.show();
            }
        });

        btn_guardarCambios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn_guardarCambios.startAnimation(animation);
                if (etA_correo3.getText() != null && etN_nombrepersona2.getText() != null) {
                    if (!etA_correo3.getText().toString().equals(viewModel.getCurrentUser().getCorreo())) {
                        viewModel.cambiarCorreo(etA_correo3.getText().toString());
                    }
                    if (!etN_nombrepersona2.getText().toString().equals(viewModel.getCurrentUser().getUsername())) {
                        viewModel.cambiarNombre(etN_nombrepersona2.getText().toString());
                    }
                }

                if (!etP_contraseña3.getText().toString().equals("")) {
                    AlertDialog.Builder alertaS = new AlertDialog.Builder(AjustesPerfil.this);
                    alertaS.setMessage(resources.getString(R.string.alertaCambiarContraseña)).setCancelable(true).setPositiveButton(resources.getString(R.string.afirmativo), new DialogInterface.OnClickListener() {
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
                    AlertDialog titulo = alertaS.create();
                    titulo.setTitle(resources.getString(R.string.cambiarContraseña));
                    titulo.show();
                } else {
                    Intent pantallaMiperfil = new Intent(getApplicationContext(), PantallaMiPerfil.class);
                    pantallaMiperfil.setAction(Intent.ACTION_OPEN_DOCUMENT);
                    startActivity(pantallaMiperfil);
                    finish();
                }
            }
        });

        btn_eliminarCuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn_eliminarCuenta.startAnimation(animation);
                AlertDialog.Builder alertaE = new AlertDialog.Builder(AjustesPerfil.this);
                alertaE.setMessage(resources.getString(R.string.alertaEliminarCuenta)).setCancelable(true).setPositiveButton(resources.getString(R.string.afirmativo), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        viewModel.deleteAccount();
                        viewModel.singOut();
                        viewModel.setLogInStatus(false);
                        Intent pantallaInicio = new Intent(getApplicationContext(), PantallaInicio.class);
                        startActivity(pantallaInicio);
                        finish();
                    }
                }).setNegativeButton(resources.getString(R.string.negativo), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                AlertDialog titulo = alertaE.create();
                titulo.setTitle(resources.getString(R.string.eliminarCuenta));
                titulo.show();
            }
        });

        img_perfil.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                img_perfil.startAnimation(animation);
                verificarPermisos();
            }
        });
    }

    private void openGallery(){
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE) {
            imageUri = data.getData();
            img_perfil.setImageURI(imageUri);
            viewModel.setImage(imageUri.toString());
            viewModel.subirImagen(imageUri);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void verificarPermisos(){
        int permisoGallery = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);

        if (permisoGallery == PackageManager.PERMISSION_GRANTED){
            openGallery();
        } else {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE);
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event){
        if(keyCode == event.KEYCODE_BACK){
            Intent areaUsuario = new Intent(getApplicationContext(), AreaUsuario.class);
            startActivity(areaUsuario);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    private String cargarPreferencias() {
        SharedPreferences preferencias = getSharedPreferences("idioma", Context.MODE_PRIVATE);
        return preferencias.getString("idioma","en");
    }

}
