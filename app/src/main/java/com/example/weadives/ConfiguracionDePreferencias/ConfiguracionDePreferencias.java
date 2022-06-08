package com.example.weadives.ConfiguracionDePreferencias;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;

import com.example.weadives.Model.Directions;
import com.example.weadives.PantallaInicio.PantallaInicio;
import com.example.weadives.Model.ParametrosClass;
import com.example.weadives.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.example.weadives.ViewModelAndExtras.SingletonIdioma;
import com.example.weadives.ViewModelAndExtras.ViewModelParametros;


import java.util.ArrayList;
import java.util.HashMap;


public class ConfiguracionDePreferencias extends AppCompatActivity {

    private Spinner spinner;
    private Button btn_guardar;
    private ImageView btn_añadir2, btn_home8,btn_basura,btn_Interrogante;
    private ScrollView scrollView2;
    private Switch sw_notificaciones, sw_mostrarEnPerfil;
    private String coords;
    private TextView txt_coord;

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
        btn_basura=findViewById(R.id.btn_basura);
        btn_Interrogante=findViewById(R.id.btn_Interrogante);
        txt_coord = findViewById(R.id.txt_coord);
        Intent intent = getIntent();

        //get Marcador
        coords = ViewModelParametros.getSingletonInstance().getMarcador().getLocation();

        //sigue
        final Context context=this;
        SingletonIdioma s= SingletonIdioma.getInstance();
        Resources resources=s.getResources();
        btn_guardar.setText(resources.getString(R.string.guardar));
        sw_notificaciones.setText(resources.getString(R.string.notificaciones));
        sw_mostrarEnPerfil.setText(resources.getString(R.string.mostrar_en_perfil));
        Animation animation= AnimationUtils.loadAnimation(context,R.anim.blink_anim2);

        txt_coord.setText(intent.getStringExtra("Waypoint"));

        if (intent.getStringExtra("Waypoint").equals(resources.getString(R.string.marcador_vacio))){
            sw_notificaciones.setEnabled(false);
        }

        btn_Interrogante.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                btn_Interrogante.startAnimation(animation);
                AlertDialog dialogBuilder= new AlertDialog.Builder(context)
                        .setTitle(resources.getString(R.string.ayuda))
                        .setMessage(resources.getString(R.string.ayudapreferencias)+"\n\n"+resources.getString(R.string.ayudapreferencias2))
                        .setIcon(R.drawable.logo_weadives_copiarande)
                        .show();
            }
        });

        btn_home8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent pantallaInicio = new Intent(getApplicationContext(), PantallaInicio.class);
                startActivity(pantallaInicio);
                btn_home8.startAnimation(animation);

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
        spn_dirOlas.setPrompt(resources.getString(R.string.selecciona_direccion));

        Spinner spn_dirViento=findViewById(R.id.spn_DireccionVientoCP);
        ArrayAdapter<ParametrosClass> adapterViento= new ArrayAdapter<>(this, R.layout.one_spinner_list, Directions.NO_DIRECTION.toArrayString());
        spn_dirViento.setAdapter(adapterViento);
        spn_dirViento.setPrompt(resources.getString(R.string.selecciona_direccion));




        //Spinner superior
        //ArrayList<ParametrosClass> test=descomprimirArray(cargarPreferenciasParametros());
        ArrayList<ParametrosClass> test= ViewModelParametros.getSingletonInstance().getLista();
        //System.out.println(test);
        if(test.size()==0){
            //test.add(new ParametrosClass());
            scrollView2.setVisibility(View.GONE);
            sw_mostrarEnPerfil.setVisibility(View.GONE);
            sw_notificaciones.setVisibility(View.GONE);
            btn_guardar.setVisibility(View.GONE);
            btn_basura.setVisibility(View.GONE);
            ParametrosClass newParametro=new ParametrosClass();
            newParametro.setNombreActividad(resources.getString(R.string.no_preferencias));
            newParametro.setIdPublicacion("-999");
            ViewModelParametros.getSingletonInstance().addParametro(newParametro);
            spinner.setSelection(0);
        }
        ArrayAdapter<ParametrosClass> adapter= new ArrayAdapter<>(this, R.layout.one_spinner_list,test);
        adapter.setDropDownViewResource(R.layout.one_spinner_list);
        spinner.setAdapter(adapter);
        /*
        final Observer<ArrayList<ParametrosClass>> listObserver = new Observer<ArrayList<ParametrosClass>>() {
            @Override
            public void onChanged(ArrayList<ParametrosClass> parametrosClasses) {
                ArrayList<ParametrosClass> test= parametrosClasses;
                if(test.size()==0){
                    test.add(new ParametrosClass());
                }
                ArrayAdapter<ParametrosClass> adapter= new ArrayAdapter<>(this, R.layout.one_spinner_list,test);
                adapter.setDropDownViewResource(R.layout.one_spinner_list);
                spinner.setAdapter(adapter);
            }
        };*/
        if(((ParametrosClass) spinner.getSelectedItem()).getIdPublicacion().equals("-999")){
            scrollView2.setVisibility(View.GONE);
            sw_mostrarEnPerfil.setVisibility(View.GONE);
            sw_notificaciones.setVisibility(View.GONE);
            btn_guardar.setVisibility(View.GONE);
            btn_basura.setVisibility(View.GONE);
        }


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                try{
                if(!((ParametrosClass) spinner.getSelectedItem()).getIdPublicacion().equals("-999")){
                    scrollView2.setVisibility(View.VISIBLE);
                }


                //mostrarAjuste((ParametrosClass) spinner.getSelectedItem());
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

                if(((ParametrosClass) spinner.getSelectedItem()).getIdPublicacion().equals("0")){
                    //System.out.println("Desactivado");
                    sw_mostrarEnPerfil.setChecked(false);
                }else{
                    //System.out.println("Activado");
                    sw_mostrarEnPerfil.setChecked(true);
                }

                if(((ParametrosClass) spinner.getSelectedItem()).getIdNotification(coords) == null){
                    sw_notificaciones.setChecked(false);
                }else{
                    if(((ParametrosClass) spinner.getSelectedItem()).getIdNotification(coords).equals("0")){
                        sw_notificaciones.setChecked(false);
                    }else{
                        sw_notificaciones.setChecked(true);
                    }
                }


                editName.setText(((ParametrosClass) spinner.getSelectedItem()).getNombreActividad());
                //System.out.println("DIRTESTEODIR2");

                spn_dirOlas.setSelection(Directions.NO_DIRECTION.toInt(((ParametrosClass) spinner.getSelectedItem()).getDirectionOlas()));
                spn_dirViento.setSelection(Directions.NO_DIRECTION.toInt(((ParametrosClass) spinner.getSelectedItem()).getDirectionViento()));
                /*System.out.println(((ParametrosClass) spinner.getSelectedItem()).getDirectionOlas());
                System.out.println(spn_dirOlas.getSelectedItemPosition());
                System.out.println(((ParametrosClass) spinner.getSelectedItem()).getDirectionViento());
                System.out.println(spn_dirViento.getSelectedItemPosition());*/
                }catch (Exception e){
                    System.err.println(e.getMessage());
                    Toast toast = Toast.makeText(context, "Error "+e.getMessage(), Toast.LENGTH_SHORT);
                    toast.show();}
            }


            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

                try{
                    scrollView2.setVisibility(View.GONE);
                    editName.setText("Nothing selected");
                    /*
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
                    spn_dirOlas.setSelection(Directions.NO_DIRECTION.toInt(Directions.NO_DIRECTION));
                    spn_dirViento.setSelection(Directions.NO_DIRECTION.toInt(Directions.NO_DIRECTION));
                     */
            }catch (Exception e){
                Toast toast = Toast.makeText(context, "Error "+e.getMessage(), Toast.LENGTH_SHORT);
                toast.show();}
            }
        });

        AlertDialog.Builder dialog=new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle(resources.getString(R.string.deletePar1))
                .setMessage(resources.getString(R.string.deletepar2))
                .setPositiveButton(resources.getString(R.string.afirmativo), new DialogInterface.OnClickListener()
                {@Override
                public void onClick(DialogInterface dialog, int which) {
                    try{
                    btn_basura.startAnimation(animation);
                    ViewModelParametros.getSingletonInstance().deleteParametro((ParametrosClass) spinner.getSelectedItem());
                    spinner.setSelection(0);
                    if(ViewModelParametros.getSingletonInstance().getLista().size()==0){
                        scrollView2.setVisibility(View.GONE);
                        sw_mostrarEnPerfil.setVisibility(View.GONE);
                        sw_notificaciones.setVisibility(View.GONE);
                        btn_guardar.setVisibility(View.GONE);
                        btn_basura.setVisibility(View.GONE);
                        ParametrosClass newParametro=new ParametrosClass();
                        newParametro.setNombreActividad(resources.getString(R.string.no_preferencias));
                        newParametro.setIdPublicacion("-999");
                        ViewModelParametros.getSingletonInstance().addParametro(newParametro);
                        spinner.setSelection(adapter.getPosition(newParametro));
                        spinner.setSelection(0);
                    }
                    }catch (Exception e){
                        Toast toast = Toast.makeText(context, "Error "+e.getMessage(), Toast.LENGTH_SHORT);
                        toast.show();}
                }

                })
                .setNegativeButton(resources.getString(R.string.negativo), null);

        btn_basura.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                dialog.show();



            }
        });

        btn_guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Animation animation= AnimationUtils.loadAnimation(context,R.anim.blink_anim2);
                btn_guardar.startAnimation(animation);
                ParametrosClass change = ((ParametrosClass) spinner.getSelectedItem());
                try {
                    change.setNombreActividad(String.valueOf(editName.getText()).replaceAll("[^A-Za-z0-9 ]",""));
                    if(!change.getNombreActividad().equals(editName.getText())){
                        editName.setText(change.getNombreActividad());
                    }
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

                    //System.out.println("DIRECTIONTEST");
                    //System.out.println();
                    change.setDirectionViento(Directions.fromInt(spn_dirViento.getSelectedItemPosition()));
                    change.setDirectionOlas(Directions.fromInt(spn_dirOlas.getSelectedItemPosition()));


                    //spinner.setAdapter(updateAdapter(test));
                    //System.out.println("DIRTESTEODIR");
                    //System.out.println(change.toString2());

                    HashMap<String, Object> data = createData(String.valueOf(editName.getText()),
                            coords,
                            Float.valueOf(String.valueOf(editpmax.getText())),
                            Float.valueOf(String.valueOf(editpmin.getText())),
                            Float.valueOf(String.valueOf(edittmax.getText())),
                            Float.valueOf(String.valueOf(edittmin.getText())),
                            Float.valueOf(String.valueOf(editvmax.getText())),
                            Float.valueOf(String.valueOf(editvmin.getText())),
                            ((ParametrosClass) spinner.getSelectedItem()).getDirectionViento().toString(),
                            Float.valueOf(String.valueOf(editaomax.getText())),
                            Float.valueOf(String.valueOf(editaomin.getText())),
                            Float.valueOf(String.valueOf(editpomax.getText())),
                            Float.valueOf(String.valueOf(editpomin.getText())),
                            ((ParametrosClass) spinner.getSelectedItem()).getDirectionOlas().toString());

                    ViewModelParametros.getSingletonInstance().changeParametro(change, (ParametrosClass) spinner.getSelectedItem(),sw_mostrarEnPerfil.isChecked(), sw_notificaciones.isChecked(), data, coords);
                    spinner.setSelection(adapter.getPosition(change));
                }catch (Exception e){
                    System.out.println("ERROR");
                    Toast toast = Toast.makeText(context, "Error "+e.getMessage(), Toast.LENGTH_SHORT);
                    toast.show();}

                //Intent seleccionDeAjuste = new Intent(getApplicationContext(), SeleccionDeAjuste.class);
                //startActivity(seleccionDeAjuste);
            }
        });

        btn_añadir2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                btn_añadir2.startAnimation(animation);
                scrollView2.setVisibility(View.VISIBLE);
                /*editName.setText("");
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
                spn_dirViento.setSelection(0);*/
                ParametrosClass newParametro=new ParametrosClass();
                newParametro.setNombreActividad(resources.getString(R.string.nuevo_parametro));
                ViewModelParametros.getSingletonInstance().addParametro(newParametro);
                ParametrosClass newParametro2 =ViewModelParametros.getSingletonInstance().getParametroPorId("-999");
                ViewModelParametros.getSingletonInstance().deleteParametro(newParametro2);
                spinner.setSelection(adapter.getPosition(newParametro));
                scrollView2.setVisibility(View.VISIBLE);
                sw_mostrarEnPerfil.setVisibility(View.VISIBLE);
                sw_notificaciones.setVisibility(View.VISIBLE);
                btn_guardar.setVisibility(View.VISIBLE);
                btn_basura.setVisibility(View.VISIBLE);
                }catch (Exception e){
                    Toast toast = Toast.makeText(context, "Error "+e.getMessage(), Toast.LENGTH_SHORT);
                    toast.show();}
            }
        });



        // Create the observer which updates the UI.
        final Observer<ArrayList<ParametrosClass>> nameObserver = new Observer<ArrayList<ParametrosClass>>() {
            @Override
            public void onChanged(@Nullable final ArrayList<ParametrosClass> list) {
                try{
                // Update the UI, in this case, a TextView.
                ArrayAdapter<ParametrosClass> adapter= new ArrayAdapter<ParametrosClass>(context, R.layout.one_spinner_list,list);
                adapter.setDropDownViewResource(R.layout.one_spinner_list);
                spinner.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                }catch (Exception e){
                    Toast toast = Toast.makeText(context, "Error "+e.getMessage(), Toast.LENGTH_SHORT);
                    toast.show();}


            }
        };
        // Observe the LiveData, passing in this activity as the LifecycleOwner and the observer.
        ViewModelParametros.getSingletonInstance().getMutable().observe(this, nameObserver);

    }


    private void guardarPreferenciasParametros(String string) {
        SharedPreferences preferencias = getSharedPreferences("parametros",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=preferencias.edit();
        editor.putString("parametros",string);
        editor.commit();
    }

    private void mostrarAjuste(ParametrosClass parametro) {
        Toast.makeText(this,parametro.getNombreActividad(),Toast.LENGTH_SHORT).show();
    }


    public class StartGameDialogFragment extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the Builder class for convenient dialog construction
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(R.string.ayudapreferencias+"\n"+R.string.ayudapreferencias2);
            builder.setIcon(R.drawable.logo_weadives);
            // Create the AlertDialog object and return it
            return builder.create();
        }
    }

    private HashMap<String, Object> createData(String nombreActividad, String coords, float presionMax, float presionMin, float temperaturaMax, float temperaturaMin, float vientoMax, float vientoMin, String directionViento, float alturaOlaMax, float alturaOlaMin, float periodoOlaMax, float periodoOlaMin, String directionOlas){
        HashMap<String, Object> data = new HashMap<>();
        data.put("actividad", nombreActividad);
        data.put("coords", coords);
        data.put("maxPreasure", presionMax);
        data.put("minPreasure", presionMin);
        data.put("maxTemperatura", temperaturaMax);
        data.put("minTemperatura", temperaturaMin);
        data.put("maxWind", vientoMax);
        data.put("minWind", vientoMin);
        data.put("windDirection", directionViento);
        data.put("maxWaveHeight", alturaOlaMax);
        data.put("minWaveHeight", alturaOlaMin);
        data.put("maxWavePeriod", periodoOlaMax);
        data.put("minWavePeriod", periodoOlaMin);
        data.put("waveDirection", directionOlas);
        return data;
    }

}