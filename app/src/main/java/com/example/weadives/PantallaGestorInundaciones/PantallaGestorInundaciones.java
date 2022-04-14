package com.example.weadives.PantallaGestorInundaciones;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.weadives.DatabaseAdapter;
import com.example.weadives.LocaleHelper;
import com.example.weadives.R;

public class PantallaGestorInundaciones extends AppCompatActivity {

    private Button btn_confirmar;
    private ImageView btn_leave;
    private Spinner DesplegableMarcadores;
    private DatabaseAdapter dbA;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pantalla_gestor_inundaciones);
        btn_confirmar=findViewById(R.id.btn_confirmarInundacion);
        btn_leave=findViewById(R.id.btn_leave);
        DesplegableMarcadores=findViewById(R.id.spn_desplegableInundaciones);
        TextView txt_gestion=findViewById(R.id.txt_gestorInundacion);
        final Context context;
        final Resources resources;
        context = LocaleHelper.setLocale(this, cargarPreferencias());
        resources = context.getResources();
        txt_gestion.setText(resources.getString(R.string.gesti_n_de_notificaciones_de_inundacion));
        btn_confirmar.setText(resources.getString(R.string.confirmar));

        dbA = DatabaseAdapter.getInstance();

        btn_leave.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                finish();
            }
        });
        btn_confirmar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                finish();
            }
        });



    }
    private String cargarPreferencias() {
        SharedPreferences preferencias = getSharedPreferences("idioma",Context.MODE_PRIVATE);
        return preferencias.getString("idioma","en");
    }
}