package com.example.weadives.PantallaDeHorarios;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.cleveroad.adaptivetablelayout.AdaptiveTableLayout;
import com.cleveroad.adaptivetablelayout.LinkedAdaptiveTableAdapter;
import com.example.weadives.PantallaInicio.PantallaInicio;
import com.example.weadives.R;
import com.example.weadives.ViewModel;

import java.io.FileNotFoundException;
import java.io.IOException;

public class PantallaDeHorarios extends AppCompatActivity {

    private ImageView btn_home10;
    private EditText editTextDate;
    private AdaptiveTableLayout tableLayout;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pantalla_horarios);
        btn_home10 = findViewById(R.id.btn_home10);
        editTextDate = findViewById(R.id.editTextDate);
        tableLayout = (AdaptiveTableLayout) findViewById(R.id.tableLayout);
        Intent intent = getIntent();
        TableDataSource dataSource = null;
        try {
            dataSource = new TableDataSource(this, "TEST.csv");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        AdapterTablaHorario adapterTablaHorario = new AdapterTablaHorario(this, dataSource);
        tableLayout.setAdapter(adapterTablaHorario);


        btn_home10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent pantallaInicio = new Intent(getApplicationContext(), PantallaInicio.class);
                startActivity(pantallaInicio);
            }
        });
    }
}
