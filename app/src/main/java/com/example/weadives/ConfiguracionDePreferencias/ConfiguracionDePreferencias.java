package com.example.weadives.ConfiguracionDePreferencias;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.weadives.DatabaseAdapter;
import com.example.weadives.DatoGradosClass;
import com.example.weadives.Directions;
import com.example.weadives.LocaleHelper;
import com.example.weadives.PantallaInicio.PantallaInicio;
import com.example.weadives.PantallaPrincipal.PantallaPrincipal;
import com.example.weadives.ParametrosClass;
import com.example.weadives.R;
import com.example.weadives.SeleccionDeAjuste.SeleccionDeAjuste;

import java.util.ArrayList;
import java.util.List;


public class ConfiguracionDePreferencias extends AppCompatActivity {

    private Spinner spinner;
    private Button btn_guardar;
    private ImageView btn_añadir2, btn_home8;
    private ScrollView scrollView2;
    private Switch sw_notificaciones, sw_mostrarEnPerfil;
    private DatabaseAdapter dbA;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.configuracion_de_preferencias);
        spinner = findViewById(R.id.spn_desplegableMarcadores2);
        btn_guardar = findViewById(R.id.btn_guardar);
        btn_añadir2 = findViewById(R.id.btn_añadir2);
        btn_home8 = findViewById(R.id.btn_home8);
        scrollView2 = findViewById(R.id.scrollView2);
        sw_notificaciones = findViewById(R.id.sw_notificaciones);
        sw_mostrarEnPerfil = findViewById(R.id.sw_mostrarEnPerfil);

        Intent intent = getIntent();

        dbA = DatabaseAdapter.getInstance();

        final Context context;
        final Resources resources;
        context = LocaleHelper.setLocale(this, cargarPreferencias());
        resources = context.getResources();
        btn_guardar.setText(resources.getString(R.string.guardar));
        sw_notificaciones.setText(resources.getString(R.string.notificaciones));
        sw_mostrarEnPerfil.setText(resources.getString(R.string.mostrar_en_perfil));


        btn_home8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent pantallaInicio = new Intent(getApplicationContext(), PantallaInicio.class);
                startActivity(pantallaInicio);
            }
        });

        btn_guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent seleccionDeAjuste = new Intent(getApplicationContext(), SeleccionDeAjuste.class);
                startActivity(seleccionDeAjuste);
            }
        });

        //Parte del scroll
        TextView actName=findViewById(R.id.txt_ActivityNameCP);
        EditText editName=findViewById(R.id.etx_ActivityNameCP);
        TextView dirOlas=findViewById(R.id.txt_DireccionOlasCP);
        Spinner spn_dirOlas=findViewById(R.id.spn_DireccionOlasCP);
        ArrayAdapter<ParametrosClass> adapterOlas= new ArrayAdapter<>(this, R.layout.one_spinner_list, Directions.NO_DIRECTION.toArrayString());
        spn_dirOlas.setAdapter(adapterOlas);




        //Spinner superior
        ArrayList<ParametrosClass> test=fillParametrosList();
        ArrayAdapter<ParametrosClass> adapter= new ArrayAdapter<>(this, R.layout.one_spinner_list,test);
        adapter.setDropDownViewResource(R.layout.one_spinner_list);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                mostrarAjuste((ParametrosClass) spinner.getSelectedItem());
                editName.setText(((ParametrosClass) spinner.getSelectedItem()).getNombreActividad());
                spn_dirOlas.setSelection(Directions.NO_DIRECTION.toInt(((ParametrosClass) spinner.getSelectedItem()).getDirectionOlas()));

            }


            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });






    }

    private void mostrarAjuste(ParametrosClass parametro) {
        Toast.makeText(this,parametro.getNombreActividad(),Toast.LENGTH_SHORT).show();
    }


    private String cargarPreferencias() {
        SharedPreferences preferencias = getSharedPreferences("idioma", Context.MODE_PRIVATE);
        return preferencias.getString("idioma","en");
    }
    private ArrayList<ParametrosClass> fillParametrosList() {
        ArrayList<ParametrosClass> parametrosList = new ArrayList<>();
        ParametrosClass p1= new ParametrosClass("SurfLoco", 0123, 0.2f,0.1f,0.3f,0.2f,0.3f,0.3f, new DatoGradosClass(20),3.f,2.f,4.f,4.f,new DatoGradosClass(0));
        parametrosList.add(p1);
        parametrosList.add(new ParametrosClass("Surf", 0, 0.1f,0.2f,0.3f,0.4f));
        parametrosList.add(new ParametrosClass("Dia de Playa", 11, 3.1f,3.2f,3.3f,3.4f));
        parametrosList.add(new ParametrosClass("Vela",2222, 4.1f,4.2f,4.3f,4.4f));
        parametrosList.add(new ParametrosClass("Kayak",3333, 5.1f,5.2f,5.3f,5.4f));
        parametrosList.add(new ParametrosClass("LOL",6546, 5.1f,5.2f,5.3f,5.4f));
        parametrosList.add(new ParametrosClass("COD",1231, 5.1f,5.2f,5.3f,5.4f));
        parametrosList.add(new ParametrosClass("Pilota",2331, 5.1f,5.2f,5.3f,5.4f));
        parametrosList.add(new ParametrosClass("ORIND",9996, 5.1f,5.2f,5.3f,5.4f));
        return parametrosList;
    }

}