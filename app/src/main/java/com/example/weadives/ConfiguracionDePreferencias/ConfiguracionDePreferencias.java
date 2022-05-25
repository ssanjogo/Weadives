package com.example.weadives.ConfiguracionDePreferencias;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
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

import androidx.annotation.NonNull;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class ConfiguracionDePreferencias extends AppCompatActivity {

    private Spinner spinner;
    private Button btn_guardar;
    private ImageView btn_añadir2, btn_home8;
    private ScrollView scrollView2;
    private Switch sw_notificaciones, sw_mostrarEnPerfil;
    private PreferenciasViewModel preferenciasViewModel;
    private String coords = "-0.0541915894_65.4908447266";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferenciasViewModel = PreferenciasViewModel.getInstance(this);
        setContentView(R.layout.configuracion_de_preferencias);
        spinner = findViewById(R.id.spn_desplegableMarcadores2);
        btn_guardar = findViewById(R.id.btn_guardar);
        btn_añadir2 = findViewById(R.id.btn_añadir2);
        btn_home8 = findViewById(R.id.btn_home8);
        scrollView2 = findViewById(R.id.scrollView2);
        sw_notificaciones = findViewById(R.id.sw_notificaciones);
        sw_mostrarEnPerfil = findViewById(R.id.sw_mostrarEnPerfil);

        Intent intent = getIntent();

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
        //Para que no se suba cuando escribes
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        //Parte del scroll
        TextView actName=findViewById(R.id.txt_ActivityNameCP);
        TextView dirOlas=findViewById(R.id.txt_DireccionOlasCP);
        TextView dirViento=findViewById(R.id.txt_DireccionVientoCP);
        TextView presionMax=findViewById(R.id.txt_presionMaxCP);
        TextView presionMin=findViewById(R.id.txt_presionMinCP);
        TextView temperaturaMax=findViewById(R.id.txt_temperaturaMaxCP);
        TextView temperaturaMin=findViewById(R.id.txt_temperaturaMinCP);
        TextView vientoMax=findViewById(R.id.txt_vientoMaxCP);
        TextView vientoMin=findViewById(R.id.txt_vientoMinCP);
        TextView alturaOlaMax=findViewById(R.id.txt_OlaMaxCP);
        TextView alturaOlaMin=findViewById(R.id.txt_OlaMinCP);
        TextView periodoOlaMax=findViewById(R.id.txt_OlaPeriodoMaxCP);
        TextView periodoOlaMin=findViewById(R.id.txt_OlaPeriodoMinCP);

        actName.setText(resources.getString(R.string.NombreActividad));
        dirOlas.setText(resources.getString(R.string.dirOlas));
        dirViento.setText(resources.getString(R.string.dirViento));
        presionMax.setText(resources.getString(R.string.presion)+" max");
        presionMin.setText(resources.getString(R.string.presion)+" min");
        temperaturaMax.setText(resources.getString(R.string.temperatura)+" max");
        temperaturaMin.setText(resources.getString(R.string.temperatura)+" min");
        vientoMax.setText(resources.getString(R.string.viento)+" max");
        vientoMin.setText(resources.getString(R.string.viento)+" min");
        alturaOlaMax.setText(resources.getString(R.string.alturaOla)+" max");
        alturaOlaMin.setText(resources.getString(R.string.alturaOla)+" min");
        periodoOlaMax.setText(resources.getString(R.string.periodoOla)+" max");
        periodoOlaMin.setText(resources.getString(R.string.periodoOla)+" min");



        EditText editName=findViewById(R.id.etx_ActivityNameCP);

        EditText editpmax=findViewById(R.id.etx_presionMaxCP);
        editpmax.setInputType(InputType.TYPE_CLASS_NUMBER + InputType.TYPE_NUMBER_FLAG_DECIMAL);
        EditText editpmin=findViewById(R.id.etx_presionMinCP);
        editpmin.setInputType(InputType.TYPE_CLASS_NUMBER + InputType.TYPE_NUMBER_FLAG_DECIMAL);
        EditText edittmax=findViewById(R.id.etx_temperaturaMaxCP);
        edittmax.setInputType(InputType.TYPE_CLASS_NUMBER + InputType.TYPE_NUMBER_FLAG_DECIMAL);
        EditText edittmin=findViewById(R.id.etx_temperaturaMinCP);
        edittmin.setInputType(InputType.TYPE_CLASS_NUMBER + InputType.TYPE_NUMBER_FLAG_DECIMAL);
        EditText editvmax=findViewById(R.id.etx_vientoMaxCP);
        editvmax.setInputType(InputType.TYPE_CLASS_NUMBER + InputType.TYPE_NUMBER_FLAG_DECIMAL);
        EditText editvmin=findViewById(R.id.etx_vientoMinCP);
        editvmin.setInputType(InputType.TYPE_CLASS_NUMBER + InputType.TYPE_NUMBER_FLAG_DECIMAL);
        EditText editaomax=findViewById(R.id.etx_OlaMaxCP);
        editaomax.setInputType(InputType.TYPE_CLASS_NUMBER + InputType.TYPE_NUMBER_FLAG_DECIMAL);
        EditText editaomin=findViewById(R.id.etx_OlaMinCP);
        editaomin.setInputType(InputType.TYPE_CLASS_NUMBER + InputType.TYPE_NUMBER_FLAG_DECIMAL);
        EditText editpomax=findViewById(R.id.etx_OlaPeriodoMaxCP);
        editpomax.setInputType(InputType.TYPE_CLASS_NUMBER + InputType.TYPE_NUMBER_FLAG_DECIMAL);
        EditText editpomin=findViewById(R.id.etx_OlaPeriodoMinCP);
        editpomin.setInputType(InputType.TYPE_CLASS_NUMBER + InputType.TYPE_NUMBER_FLAG_DECIMAL);

        Spinner spn_dirOlas=findViewById(R.id.spn_DireccionOlasCP);
        ArrayAdapter<ParametrosClass> adapterOlas= new ArrayAdapter<>(this, R.layout.one_spinner_list, Directions.NO_DIRECTION.toArrayString());
        spn_dirOlas.setAdapter(adapterOlas);

        Spinner spn_dirViento=findViewById(R.id.spn_DireccionVientoCP);
        ArrayAdapter<ParametrosClass> adapterViento= new ArrayAdapter<>(this, R.layout.one_spinner_list, Directions.NO_DIRECTION.toArrayString());
        spn_dirViento.setAdapter(adapterViento);





        //Spinner superior
        ArrayList<ParametrosClass> test=descomprimirArray(cargarPreferenciasParametros());
        ArrayAdapter<ParametrosClass> adapter= new ArrayAdapter<>(this, R.layout.one_spinner_list,test);
        adapter.setDropDownViewResource(R.layout.one_spinner_list);
        spinner.setAdapter(adapter);



        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                mostrarAjuste((ParametrosClass) spinner.getSelectedItem());
                editpmax.setText((Float.toString(((ParametrosClass) spinner.getSelectedItem()).getPresionMax())));
                editpmin.setText((Float.toString(((ParametrosClass) spinner.getSelectedItem()).getPresionMin())));
                edittmax.setText((Float.toString(((ParametrosClass) spinner.getSelectedItem()).getTemperaturaMax())));
                edittmin.setText((Float.toString(((ParametrosClass) spinner.getSelectedItem()).getTemperaturaMin())));
                editvmax.setText((Float.toString(((ParametrosClass) spinner.getSelectedItem()).getVientoMax())));
                editvmin.setText((Float.toString(((ParametrosClass) spinner.getSelectedItem()).getVientoMin())));
                editaomax.setText((Float.toString(((ParametrosClass) spinner.getSelectedItem()).getAlturaOlaMax())));
                editaomin.setText((Float.toString(((ParametrosClass) spinner.getSelectedItem()).getAlturaOlaMin())));
                editpomax.setText((Float.toString(((ParametrosClass) spinner.getSelectedItem()).getPeriodoOlaMax())));
                editpomin.setText((Float.toString(((ParametrosClass) spinner.getSelectedItem()).getPeriodoOlaMin())));

                editName.setText(((ParametrosClass) spinner.getSelectedItem()).getNombreActividad());
                spn_dirOlas.setSelection(Directions.NO_DIRECTION.toInt(((ParametrosClass) spinner.getSelectedItem()).getDirectionOlas()));
                spn_dirViento.setSelection(Directions.NO_DIRECTION.toInt(((ParametrosClass) spinner.getSelectedItem()).getDirectionViento()));
            }


            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btn_guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ParametrosClass change = ((ParametrosClass) spinner.getSelectedItem());
                try {
                    HashMap<String, Object> data = new HashMap<>();
                    data.put("actividad", String.valueOf(editName.getText()));
                    data.put("coords", coords);
                    data.put("maxPreasure", Float.valueOf(String.valueOf(editpmax.getText())));
                    data.put("minPreasure", Float.valueOf(String.valueOf(editpmin.getText())));
                    data.put("maxTemperatura", Float.valueOf(String.valueOf(edittmax.getText())));
                    data.put("minTemperatura", Float.valueOf(String.valueOf(edittmin.getText())));
                    data.put("maxWind", Float.valueOf(String.valueOf(editvmax.getText())));
                    data.put("minWind", Float.valueOf(String.valueOf(editvmin.getText())));
                    data.put("windDirection", ((ParametrosClass) spinner.getSelectedItem()).getDirectionViento().toString());
                    System.out.println("Aqui: " + ((ParametrosClass) spinner.getSelectedItem()).getDirectionViento().toString());
                    data.put("maxWaveHeight", Float.valueOf(String.valueOf(editaomax.getText())));
                    data.put("minWaveHeight", Float.valueOf(String.valueOf(editaomin.getText())));
                    data.put("maxWavePeriod", Float.valueOf(String.valueOf(editpomax.getText())));
                    data.put("minWavePeriod", Float.valueOf(String.valueOf(editpomin.getText())));
                    data.put("waveDirection", ((ParametrosClass) spinner.getSelectedItem()).getDirectionOlas().toString());
                    FirebaseMessaging.getInstance().getToken()
                            .addOnCompleteListener(new OnCompleteListener<String>() {
                                @Override
                                public void onComplete(@NonNull Task<String> task) {
                                    if (!task.isSuccessful()) {
                                        return;
                                    }
                                    // Get new FCM registration token
                                    data.put("token", task.getResult());
                                    preferenciasViewModel.createCoordsNotification(coords, data);

                                }
                            });
                    change.setNombreActividad(String.valueOf(editName.getText()));
                    change.setPresionMax(Float.valueOf(String.valueOf(editpmax.getText())));
                    change.setPresionMin(Float.valueOf(String.valueOf(editpmin.getText())));
                    change.setTemperaturaMax(Float.valueOf(String.valueOf(edittmax.getText())));
                    change.setTemperaturaMin(Float.valueOf(String.valueOf(edittmin.getText())));
                    change.setVientoMax(Float.valueOf(String.valueOf(editvmax.getText())));
                    change.setVientoMin(Float.valueOf(String.valueOf(editvmin.getText())));
                    change.setDirectionViento(((ParametrosClass) spinner.getSelectedItem()).getDirectionViento());
                    change.setAlturaOlaMax(Float.valueOf(String.valueOf(editaomax.getText())));
                    change.setAlturaOlaMin(Float.valueOf(String.valueOf(editaomin.getText())));
                    change.setPeriodoOlaMax(Float.valueOf(String.valueOf(editpomax.getText())));
                    change.setPeriodoOlaMin(Float.valueOf(String.valueOf(editpomin.getText())));
                    change.setDirectionOlas(((ParametrosClass) spinner.getSelectedItem()).getDirectionOlas());
                    spinner.setAdapter(updateAdapter(test));



                }catch (Exception e){

                }

                //Intent seleccionDeAjuste = new Intent(getApplicationContext(), SeleccionDeAjuste.class);
                //startActivity(seleccionDeAjuste);
            }
        });

        btn_añadir2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editName.setText("");
                editpmax.setText("");
                editpmin.setText("");
                edittmax.setText("");
                edittmin.setText("");
                editvmax.setText("");
                editvmin.setText("");
                editaomax.setText("");
                editaomin.setText("");
                editpomax.setText("");
                editpomin.setText("");
                spn_dirOlas.setSelection(0);
                spn_dirViento.setSelection(0);
                ParametrosClass newParametro=new ParametrosClass();


            }
        });


    }
    private String comprimirArray(ArrayList<ParametrosClass> l){
        String str="";
        for(int i=0; i<l.size();i++){
            str=str+l.get(i).toSaveString()+"¿";
        }
        return str;
    }
    private ArrayList<ParametrosClass> descomprimirArray(String l){
        System.out.println(l);
        String[] parametrosStringList = l.split("¿");
        int count = l.length() - l.replace("¿", "").length();
        System.out.println(count);
        ArrayList<ParametrosClass> parametrosList = new ArrayList<>();
        String[] fixedParam;
        for (String i : parametrosStringList) {
            System.out.println(i);
            fixedParam=i.split(",");
            for (String x : fixedParam) {
                System.out.println(x);
            }

            parametrosList.add(new ParametrosClass(fixedParam[0], Float.parseFloat(fixedParam[1]),Float.parseFloat(fixedParam[2]),Float.parseFloat(fixedParam[3]),Float.parseFloat(fixedParam[4]),Float.parseFloat(fixedParam[5]),Float.parseFloat(fixedParam[6]), new DatoGradosClass(Directions.valueOf(fixedParam[7])),Float.parseFloat(fixedParam[8]),Float.parseFloat(fixedParam[9]),Float.parseFloat(fixedParam[10]),Float.parseFloat(fixedParam[11]),new DatoGradosClass(Directions.valueOf(fixedParam[12]))));
        }

        return parametrosList;
    }

    private void guardarPreferenciasParametros(String string) {
        SharedPreferences preferencias = getSharedPreferences("parametros",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=preferencias.edit();
        editor.putString("parametros",string);
        editor.commit();
    }

    private String cargarPreferenciasParametros() {
        SharedPreferences preferencias = getSharedPreferences("parametros",Context.MODE_PRIVATE);
        return preferencias.getString("parametros",comprimirArray(fillParametrosList()));
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
        ParametrosClass p1= new ParametrosClass("SurfLoco", 0.2f,0.1f,0.3f,0.2f,0.3f,0.3f, new DatoGradosClass(Directions.SUD),3.f,2.f,4.f,4.f,new DatoGradosClass(Directions.ESTE));
        parametrosList.add(p1);
        parametrosList.add(new ParametrosClass("Surf",  0.2f,0.1f,0.3f,0.2f,0.3f,0.3f, new DatoGradosClass(Directions.SUD),3.f,2.f,4.f,4.f,new DatoGradosClass(Directions.ESTE)));
        parametrosList.add(new ParametrosClass("Playa",  0.2f,0.1f,0.3f,0.2f,0.3f,0.3f, new DatoGradosClass(Directions.SUD),3.f,2.f,4.f,4.f,new DatoGradosClass(Directions.ESTE)));
        parametrosList.add(new ParametrosClass("Vela",  0.2f,0.1f,0.3f,0.2f,0.3f,0.3f, new DatoGradosClass(Directions.SUD),3.f,2.f,4.f,4.f,new DatoGradosClass(Directions.ESTE)));
        parametrosList.add(new ParametrosClass("Kayak",  0.2f,0.1f,0.3f,0.2f,0.3f,0.3f, new DatoGradosClass(Directions.SUD),3.f,2.f,4.f,4.f,new DatoGradosClass(Directions.ESTE)));
        parametrosList.add(new ParametrosClass("LioLegends",  0.2f,0.1f,0.3f,0.2f,0.3f,0.3f, new DatoGradosClass(Directions.SUD),3.f,2.f,4.f,4.f,new DatoGradosClass(Directions.ESTE)));
        parametrosList.add(new ParametrosClass("CallDuty",  0.2f,0.1f,0.3f,0.2f,0.3f,0.3f, new DatoGradosClass(Directions.SUD),3.f,2.f,4.f,4.f,new DatoGradosClass(Directions.ESTE)));
        parametrosList.add(new ParametrosClass("BalonPie",  0.2f,0.1f,0.3f,0.2f,0.3f,0.3f, new DatoGradosClass(Directions.SUD),3.f,2.f,4.f,4.f,new DatoGradosClass(Directions.ESTE)));
        parametrosList.add(new ParametrosClass("DokkanBattle", 0.2f,0.1f,0.3f,0.2f,0.3f,0.3f, new DatoGradosClass(Directions.SUD),3.f,2.f,4.f,4.f,new DatoGradosClass(Directions.ESTE)));
        return parametrosList;
    }


    private ArrayAdapter updateAdapter(ArrayList<ParametrosClass> list){
        ArrayAdapter<ParametrosClass> adapter= new ArrayAdapter<>(this, R.layout.one_spinner_list,list);
        adapter.setDropDownViewResource(R.layout.one_spinner_list);
        guardarPreferenciasParametros(comprimirArray(list));
        return adapter;


    }
}