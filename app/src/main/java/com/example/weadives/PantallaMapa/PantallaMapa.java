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
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

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

import java.util.ArrayList;

public class PantallaMapa extends FragmentActivity implements OnMapReadyCallback {

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
    // Maps
    private LatLng coordsMarcador;
    private Marker tempMarcador;
    private GroundOverlay imagenClima;
    //ViewModel
    ViewModelMapa viewModelMapa;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModelMapa = ViewModelMapa.getInstance(this);

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

        btn_gestorNotificaciones = findViewById(R.id.btn_gestorNotificaciones);
        btn_gestorNotificaciones.setText(resources.getString(R.string.gestion_notificaciones));

        btn_home20=findViewById(R.id.btn_home20);
        btn_home20.bringToFront();

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
                cargarImagenClima(getImagenClima(i));
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

        // Limites
        LatLngBounds maldivesBounds = new LatLngBounds( new LatLng(-0.938178, 71.118660), new LatLng(7.181926, 75.214051));
        // Mapa satelite
        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        // Centrar camara
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(maldivesBounds.getCenter(), 7.5f));
        // Marcador limites
        mMap.setLatLngBoundsForCameraTarget(maldivesBounds);
        // Limitar zoom
        mMap.setMinZoomPreference(7.5f);
        // Actualizamos marcadores
        mMap.getUiSettings().setMapToolbarEnabled(false);
        actualizarMarcadores();
        cargarImagenClima(getImagenClima(skb_seleccionarHora.getProgress()));


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

    private int getImagenClima(int tipo){
        switch(tipo){
            case 0: return R.drawable.imagen_clima0;
            case 1: return R.drawable.imagen_clima1;
            default: return R.drawable.imagen_clima0;
        }
    }
    private void cargarImagenClima(int resourceId){
        if (imagenClima == null){
            LatLngBounds maldivesBounds = new LatLngBounds( new LatLng(-5.467415, 65.490845), new LatLng(10.97052, 79.48093));
            GroundOverlayOptions imagen = new GroundOverlayOptions()
                    .image(BitmapDescriptorFactory.fromResource(resourceId))
                    .positionFromBounds(maldivesBounds)
                    .transparency(0.5f);
            imagenClima = mMap.addGroundOverlay(imagen);
        } else {
            imagenClima.setImage(BitmapDescriptorFactory.fromResource(resourceId));
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
}