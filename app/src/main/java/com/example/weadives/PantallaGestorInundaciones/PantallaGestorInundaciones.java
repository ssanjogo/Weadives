package com.example.weadives.PantallaGestorInundaciones;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;

import com.example.weadives.R;

public class PantallaGestorInundaciones extends AppCompatActivity {


    private Button btn_confirmar;
    private ImageView btn_leave;
    private Spinner DesplegableMarcadores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pantalla_gestor_inundaciones);
        btn_confirmar=findViewById(R.id.btn_confirmarInundacion);
        btn_leave=findViewById(R.id.btn_leave);
        DesplegableMarcadores=findViewById(R.id.spn_desplegableInundaciones);

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
}