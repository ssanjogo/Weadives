package com.example.weadives.PantallaDeHorarios;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.weadives.PantallaInicio.PantallaInicio;
import com.example.weadives.R;

public class PantallaDeHorarios extends AppCompatActivity {

    private ImageView btn_home10;
    private EditText editTextDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pantalla_horarios);
        ImageView btn_home10 = findViewById(R.id.btn_home10);
        EditText editTextDate = findViewById(R.id.editTextDate);

        Intent intent = getIntent();

        btn_home10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent pantallaInicio = new Intent(getApplicationContext(), PantallaInicio.class);
                startActivity(pantallaInicio);
            }
        });

    }
}
