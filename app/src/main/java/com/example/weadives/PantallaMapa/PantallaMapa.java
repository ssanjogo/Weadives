package com.example.weadives.PantallaMapa;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.fragment.app.FragmentActivity;

import com.example.weadives.PantallaGestorInundaciones.PantallaGestorInundaciones;
import com.example.weadives.R;
import com.example.weadives.databinding.PantallaMapaBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class PantallaMapa extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private PantallaMapaBinding binding;
    private ImageView btn_home;
    private Button btn_gestorNotificaciones;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = PantallaMapaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        ImageView btn_home=findViewById(R.id.btn_home5);
        btn_home.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                finish();
            }
        });
        Button btn_gestorNotificaciones =findViewById(R.id.btn_gestorNotificaciones);
        btn_gestorNotificaciones.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

                Intent testIntent = new Intent(getApplicationContext(), PantallaGestorInundaciones.class);
                startActivity(testIntent);
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

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
}