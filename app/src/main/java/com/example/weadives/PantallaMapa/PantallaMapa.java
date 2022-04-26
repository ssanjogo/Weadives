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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.example.weadives.LocaleHelper;
import com.example.weadives.PantallaGestorInundaciones.PantallaGestorInundaciones;
import com.example.weadives.R;
import com.example.weadives.databinding.PantallaMapaBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PantallaMapa extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private PantallaMapaBinding binding;
    private ImageButton btn_home;
    private Button btn_gestorNotificaciones;
    private LinearLayout lay_layoutMarcador;
    private Button btn_aceptar;
    private Button btn_cancelar;
    private EditText txt_nombreMarcador;
    private LinearLayout lay_layoutMarcadorEliminar;
    private Button btn_eliminar;
    private Button btn_cancelarEliminar;
    private TextView txt_nombreMarcadorEliminar;
    // Maps
    private LatLng coordsMarcador;
    private Marker tempMarcador;
    private GroundOverlay imagenClima;

    //Mover Modelo
    private ArrayList<MarcadorClass> marcadorList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = PantallaMapaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        btn_gestorNotificaciones =findViewById(R.id.btn_gestorNotificaciones);
        final Context context;
        final Resources resources;
        context = LocaleHelper.setLocale(this, cargarPreferencias());
        resources = context.getResources();
        btn_gestorNotificaciones.setText(resources.getString(R.string.gestion_notificaciones));

        btn_home=findViewById(R.id.btn_home5);
        btn_home.bringToFront();

        lay_layoutMarcador = findViewById(R.id.LinearLayoutMarcador);
        btn_aceptar = findViewById(R.id.btn_aceptar);
        btn_cancelar = findViewById(R.id.btn_cancelar);
        txt_nombreMarcador = findViewById(R.id.txt_nombreMarcador);
        lay_layoutMarcador.setVisibility(View.INVISIBLE);

        lay_layoutMarcadorEliminar = findViewById(R.id.LinearLayoutEliminarMarcador);
        btn_eliminar = findViewById(R.id.btn_eliminar);
        btn_cancelarEliminar = findViewById(R.id.btn_cancelarEliminar);
        lay_layoutMarcadorEliminar.setVisibility(View.INVISIBLE);

        //Pasar a modelo
        marcadorList = new ArrayList<>();

        btn_home.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                finish();
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

        btn_cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lay_layoutMarcador.setVisibility(View.INVISIBLE);
            }
        });

        btn_aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                guardarMarcador(txt_nombreMarcador.getText().toString(), coordsMarcador);
                mMap.addMarker(new MarkerOptions().title(txt_nombreMarcador.getText().toString()).position(coordsMarcador));
                lay_layoutMarcador.setVisibility(View.INVISIBLE);
                txt_nombreMarcador.setText("");
            }
        });

        btn_eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eliminarMarcador(tempMarcador.getTitle(), tempMarcador.getPosition());
                tempMarcador.remove();
                lay_layoutMarcadorEliminar.setVisibility(View.INVISIBLE);
            }
        });

        btn_cancelarEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lay_layoutMarcadorEliminar.setVisibility(View.INVISIBLE);
            }
        });




    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
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
        cargarImagenClima();


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


    private void cargarImagenClima(){
        LatLngBounds maldivesBounds = new LatLngBounds( new LatLng(-5.467415, 65.490845), new LatLng(10.97052, 79.48093));
        GroundOverlayOptions imagen = new GroundOverlayOptions()
                .image(BitmapDescriptorFactory.fromResource(R.drawable.imagen_clima))
                .positionFromBounds(maldivesBounds)
                .transparency(0.5f);
        imagenClima = mMap.addGroundOverlay(imagen);
    }

    //Sacar método pal modelo
    private void guardarMarcador(String nombre, LatLng latLng){
        marcadorList.add(new MarcadorClass(nombre, latLng));
    }

    // Pal modelo TODO: buscar marcador y eliminar
    private void eliminarMarcador(String nombre, LatLng latLng){
        marcadorList.remove(new MarcadorClass(nombre, latLng));
    }

    //Esto no modelo
    private void actualizarMarcadores(){
        /* Forma cutre
        for(MarcadorClass marcador : marcadorList){
            mMap.addMarker(new MarkerOptions().position(marcador.getLatLng()).title(marcador.getName()));
        }*/
        /*HashMap<String, LatLng> marcadorHashMap = getMarcadorList();
        for(Map.Entry<String, LatLng> marcador : marcadorHashMap.entrySet()){
            mMap.addMarker(new MarkerOptions().position(marcador.getValue()).title(marcador.getKey()));
        }*/
        ArrayList<MarkerOptions> markerList = getMarcadorList();
        for(MarkerOptions marker : markerList){
            mMap.addMarker(marker);
        }
    }
    // Modelo
    private ArrayList<MarkerOptions> getMarcadorList() {
        /*HashMap<String, LatLng> marcadorHashMap = new HashMap<>();
        for(MarcadorClass marcador : marcadorList){
            marcadorHashMap.put(marcador.getName(), marcador.getLatLng());
        }
        return marcadorHashMap;*/
        ArrayList<MarkerOptions> markerList = new ArrayList<>();
        for (MarcadorClass marker : marcadorList) {
            markerList.add(new MarkerOptions().title(marker.getName()).position(marker.getLatLng()));
        }
        return markerList;
    }


    private String cargarPreferencias() {
        SharedPreferences preferencias = getSharedPreferences("idioma", Context.MODE_PRIVATE);
        return preferencias.getString("idioma","en");
    }
}