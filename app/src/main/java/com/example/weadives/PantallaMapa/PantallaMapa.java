package com.example.weadives.PantallaMapa;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;

import com.example.weadives.ViewModelAndExtras.LocaleHelper;
import com.example.weadives.PantallaGestorInundaciones.PantallaGestorInundaciones;
import com.example.weadives.PantallaInicio.PantallaInicio;
import com.example.weadives.R;
import com.example.weadives.ViewModelAndExtras.SingletonIdioma;
import com.example.weadives.ViewModelAndExtras.ViewModelMapa;
import com.example.weadives.databinding.PantallaMapaBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.File;
import java.util.ArrayList;

public class PantallaMapa extends FragmentActivity implements OnMapReadyCallback{

    private GoogleMap mMap;
    private PantallaMapaBinding binding;
    private ImageButton btn_home20;
    private Button btn_gestorNotificaciones;
    private LinearLayout lay_layoutMarcador;
    private Button btn_aceptar;
    private Button btn_cancelar;
    private EditText txt_nombreMarcador;
    private LinearLayout lay_layoutMarcadorEliminar;
    private Button btn_eliminar;
    private Button btn_cancelarEliminar;
    private TextView txt_nombreMarcadorEliminar;
    private SeekBar skb_seleccionarHora;
    private Button btn_day0;
    private Button btn_day1;
    private Button btn_day2;
    private ImageView btn_hs;
    private ImageView btn_psl;
    private ImageView btn_wind;
    private ImageView btn_ayuda;
    private TextView txt_hora;
    // Maps
    private LatLng coordsMarcador;
    private Marker tempMarcador;
    private GroundOverlay imagenClima;
    private ArrayList<File> imageList;
    private int sel;
    private LatLngBounds maldivesBounds;
    private GroundOverlayOptions imagen;
    //ViewModel
    ViewModelMapa viewModelMapa;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_Weadives);
        viewModelMapa = ViewModelMapa.getInstance(this);
        sel = 0;

        final Observer<ArrayList<File>> observer = new Observer<ArrayList<File>>() {
            @Override
            public void onChanged(ArrayList<File> files) {
                imageList = files;
                skb_seleccionarHora.setVisibility(View.VISIBLE);
                btn_day0.setVisibility(View.VISIBLE);
                btn_day1.setVisibility(View.VISIBLE);
                btn_day2.setVisibility(View.VISIBLE);
                txt_hora.setVisibility(View.VISIBLE);
            }
        };
        viewModelMapa.getFileData().observe(this, observer);

        ImageButton btn_home20;
        binding = PantallaMapaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);


        mapFragment.getMapAsync(this);


        final Context context;
        final Resources resources;
        SingletonIdioma s= SingletonIdioma.getInstance();
        resources=s.getResources();
        context = LocaleHelper.setLocale(this, cargarPreferencias());
        setTheme(R.style.Theme_Weadives);

        btn_gestorNotificaciones = findViewById(R.id.btn_gestorNotificaciones);
        btn_gestorNotificaciones.setText(resources.getString(R.string.gestion_notificaciones));
        btn_gestorNotificaciones.setVisibility(View.INVISIBLE);

        btn_home20=findViewById(R.id.btn_home20);
        btn_home20.bringToFront();

        txt_hora = findViewById(R.id.txt_hora);
        txt_hora.setVisibility(View.INVISIBLE);

        lay_layoutMarcador = findViewById(R.id.LinearLayoutMarcador);
        btn_aceptar = findViewById(R.id.btn_aceptar);
        btn_cancelar = findViewById(R.id.btn_cancelar);
        txt_nombreMarcador = findViewById(R.id.txt_nombreMarcador);
        lay_layoutMarcador.setVisibility(View.INVISIBLE);

        lay_layoutMarcadorEliminar = findViewById(R.id.LinearLayoutEliminarMarcador);
        btn_eliminar = findViewById(R.id.btn_eliminar);
        btn_cancelarEliminar = findViewById(R.id.btn_cancelarEliminar);
        lay_layoutMarcadorEliminar.setVisibility(View.INVISIBLE);


        skb_seleccionarHora = findViewById(R.id.skb_seleccionarHora);
        skb_seleccionarHora.setProgress(0);
        skb_seleccionarHora.setVisibility(View.INVISIBLE);

        btn_hs = findViewById(R.id.btn_hs);
        btn_psl = findViewById(R.id.btn_psl);
        btn_wind = findViewById(R.id.btn_wind);
        btn_ayuda = findViewById(R.id.btn_ayudaMapa);

        btn_day0 = findViewById(R.id.btn_day0);
        btn_day0.setVisibility(View.INVISIBLE);
        btn_day1 = findViewById(R.id.btn_day1);
        btn_day1.setVisibility(View.INVISIBLE);
        btn_day2 = findViewById(R.id.btn_day2);
        btn_day2.setVisibility(View.INVISIBLE);

        Animation animation= AnimationUtils.loadAnimation(PantallaMapa.this,R.anim.blink_anim2);
        btn_ayuda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn_ayuda.startAnimation(animation);
                AlertDialog dialogBuilder= new AlertDialog.Builder(PantallaMapa.this)
                        .setTitle(resources.getString(R.string.ayuda))
                        .setMessage(resources.getString(R.string.ayudapreferencias)+"\n\n"+resources.getString(R.string.ayudapreferencias2))
                        .setIcon(R.drawable.logo_weadives_copiarande)
                        .show();
            }
        });


        btn_hs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imagenClima.setImage(BitmapDescriptorFactory.fromResource(R.drawable.noimage));
                skb_seleccionarHora.setVisibility(View.INVISIBLE);
                btn_day0.setVisibility(View.INVISIBLE);
                btn_day1.setVisibility(View.INVISIBLE);
                btn_day2.setVisibility(View.INVISIBLE);
                txt_hora.setVisibility(View.INVISIBLE);
                viewModelMapa.getWeatherImage("HS");
                sel = 0;
                skb_seleccionarHora.setProgress(0);
            }
        });

        btn_psl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imagenClima.setImage(BitmapDescriptorFactory.fromResource(R.drawable.noimage));
                skb_seleccionarHora.setVisibility(View.INVISIBLE);
                btn_day0.setVisibility(View.INVISIBLE);
                btn_day1.setVisibility(View.INVISIBLE);
                btn_day2.setVisibility(View.INVISIBLE);
                txt_hora.setVisibility(View.INVISIBLE);
                viewModelMapa.getWeatherImage("PSL");
                sel = 0;
                skb_seleccionarHora.setProgress(0);
            }
        });

        btn_wind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imagenClima.setImage(BitmapDescriptorFactory.fromResource(R.drawable.noimage));
                skb_seleccionarHora.setVisibility(View.INVISIBLE);
                btn_day0.setVisibility(View.INVISIBLE);
                btn_day1.setVisibility(View.INVISIBLE);
                btn_day2.setVisibility(View.INVISIBLE);
                txt_hora.setVisibility(View.INVISIBLE);
                viewModelMapa.getWeatherImage("WIND");
                sel = 0;
                skb_seleccionarHora.setProgress(0);
            }
        });

        btn_day0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sel = 0;
                skb_seleccionarHora.setProgress(0);
                cargarImagenClima(skb_seleccionarHora.getProgress() + sel);
                txt_hora.setText(changeHora(skb_seleccionarHora.getProgress() + sel));
            }
        });

        btn_day1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sel = 24;
                skb_seleccionarHora.setProgress(0);
                cargarImagenClima(skb_seleccionarHora.getProgress() + sel);
                txt_hora.setText(changeHora(skb_seleccionarHora.getProgress() + sel));
            }
        });

        btn_day2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sel = 48;
                skb_seleccionarHora.setProgress(0);
                cargarImagenClima(skb_seleccionarHora.getProgress() + sel);
                txt_hora.setText(changeHora(skb_seleccionarHora.getProgress() + sel));
            }
        });


        btn_gestorNotificaciones.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Animation animation= AnimationUtils.loadAnimation(context,R.anim.blink_anim2);
                btn_gestorNotificaciones.startAnimation(animation);
                Intent testIntent = new Intent(getApplicationContext(), PantallaGestorInundaciones.class);
                startActivity(testIntent);
            }
        });

        btn_home20.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent pantallaInicio = new Intent(getApplicationContext(), PantallaInicio.class);
                startActivity(pantallaInicio);
            }
        });

        btn_cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lay_layoutMarcador.setVisibility(View.INVISIBLE);
            }
        });

        btn_aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModelMapa.guardarMarcador(txt_nombreMarcador.getText().toString(), coordsMarcador);
                mMap.addMarker(new MarkerOptions().title(txt_nombreMarcador.getText().toString()).position(coordsMarcador));
                viewModelMapa.guardarPersistencia();
                lay_layoutMarcador.setVisibility(View.INVISIBLE);
                txt_nombreMarcador.setText("");
            }
        });

        btn_eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModelMapa.eliminarMarcador(tempMarcador.getTitle(), tempMarcador.getPosition());
                tempMarcador.remove();
                viewModelMapa.guardarPersistencia();
                lay_layoutMarcadorEliminar.setVisibility(View.INVISIBLE);
            }
        });

        btn_cancelarEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lay_layoutMarcadorEliminar.setVisibility(View.INVISIBLE);
            }
        });

        skb_seleccionarHora.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                cargarImagenClima(skb_seleccionarHora.getProgress() + sel);
                txt_hora.setText(changeHora(skb_seleccionarHora.getProgress() + sel));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //Creación Overlay Options
        maldivesBounds = new LatLngBounds( new LatLng(-5.467415, 65.490845), new LatLng(10.97052, 79.48093));
        imagen = new GroundOverlayOptions()
                .image(BitmapDescriptorFactory.fromResource(R.drawable.noimage))
                .positionFromBounds(maldivesBounds)
                .transparency(0.5f);
        //Aplicamos el Overlay y lo guardamos
        imagenClima = mMap.addGroundOverlay(imagen);
        // Limites
        LatLngBounds camera = new LatLngBounds( new LatLng(-0.938178, 71.118660), new LatLng(7.181926, 75.214051));
        // Mapa satelite
        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        // Centrar camara
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(camera.getCenter(), 7.5f));
        // Marcador limites
        mMap.setLatLngBoundsForCameraTarget(camera);
        // Limitar zoom
        mMap.setMinZoomPreference(7.5f);
        // Actualizamos marcadores
        mMap.getUiSettings().setMapToolbarEnabled(false);
        actualizarMarcadores();


        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull LatLng latLng) {
                coordsMarcador = latLng;
                lay_layoutMarcador.setVisibility(View.VISIBLE);
            }
        });
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {
                tempMarcador = marker;
                lay_layoutMarcadorEliminar.setVisibility(View.VISIBLE);
                //No tocar (Importante)
                return false;
            }
        });
    }

    private void cargarImagenClima(int sel){
        try {
            imagenClima.setImage(BitmapDescriptorFactory.fromFile(imageList.get(sel).getName()));
        }catch(Exception exception){
            System.err.println(exception);
            imagenClima.setImage(BitmapDescriptorFactory.fromResource(R.drawable.noimage));
        }
    }

    private void actualizarMarcadores(){
        ArrayList<MarkerOptions> markerList = viewModelMapa.getMarkerOptionsList();
        for(MarkerOptions marker : markerList){
            mMap.addMarker(marker);
        }
    }

    private String cargarPreferencias() {
        SharedPreferences preferencias = getSharedPreferences("idioma", Context.MODE_PRIVATE);
        return preferencias.getString("idioma","en");
    }

    private String changeHora(int sel){
        int temp = 0;
        if(sel < 24){
            temp = 0;
        }else if(sel < 48){
            temp = 24;
        }else {
            temp = 48;
        }
        switch(sel - temp){
            case 0:
                return "00:00";
            case 1:
                return "01:00";
            case 2:
                return "02:00";
            case 3:
                return "03:00";
            case 4:
                return "04:00";
            case 5:
                return "05:00";
            case 6:
                return "06:00";
            case 7:
                return "07:00";
            case 8:
                return "08:00";
            case 9:
                return "09:00";
            case 10:
                return "10:00";
            case 11:
                return "11:00";
            case 12:
                return "12:00";
            case 13:
                return "13:00";
            case 14:
                return "14:00";
            case 15:
                return "15:00";
            case 16:
                return "16:00";
            case 17:
                return "17:00";
            case 18:
                return "18:00";
            case 19:
                return "19:00";
            case 20:
                return "20:00";
            case 21:
                return "21:00";
            case 22:
                return "22:00";
            case 23:
                return "23:00";
            default:
                return "00:00";
        }
    }
}