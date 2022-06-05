package com.example.weadives.PantallaDeHorarios;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.cleveroad.adaptivetablelayout.AdaptiveTableLayout;
import com.example.weadives.Model.TableDataSource;
import com.example.weadives.PantallaInicio.PantallaInicio;
import com.example.weadives.R;
import com.example.weadives.ViewModelAndExtras.SingletonIdioma;
import com.example.weadives.ViewModelAndExtras.ViewModel;

import java.io.FileNotFoundException;
import java.io.IOException;

public class PantallaDeHorarios extends AppCompatActivity {

    private ImageView btn_home10;
    private ImageView btn_help;
    private AdaptiveTableLayout tableLayout;
    private ViewModelHorario viewModelHorario;


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pantalla_horarios);
        btn_home10 = findViewById(R.id.btn_home10);
        btn_help=findViewById(R.id.btn_help);
        tableLayout = (AdaptiveTableLayout) findViewById(R.id.tableLayout);
        viewModelHorario = ViewModelHorario.getInstance(this);
        final Context context=this;

        viewModelHorario.getDataLocation();

        SingletonIdioma s= SingletonIdioma.getInstance();
        Resources resources=s.getResources();

        Intent intent = getIntent();
        TableDataSource dataSource = new TableDataSource(context,viewModelHorario.getCsvRef());

        System.out.println(dataSource == null);
        for (int i = 0; i < dataSource.getColumnsCount(); i++){
            System.out.println("DATA TEST (7," + i + "): " + dataSource.getItemData(7,i));
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


        Animation animation= AnimationUtils.loadAnimation(context,R.anim.blink_anim2);
        btn_help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn_help.startAnimation(animation);
                AlertDialog dialogBuilder= new AlertDialog.Builder(context)
                        .setTitle(resources.getString(R.string.ayuda))
                        .setMessage(resources.getString(R.string.ayudaHorario_1)+"\n"+resources.getString(R.string.ayudaHorario_2))
                        .setIcon(R.drawable.logo_weadives_copiarande)
                        .show();
            }
        });

    }
}
