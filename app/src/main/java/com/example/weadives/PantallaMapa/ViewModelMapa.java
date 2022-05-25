package com.example.weadives.PantallaMapa;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.weadives.DatabaseAdapter;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;


public class ViewModelMapa extends AndroidViewModel implements DatabaseAdapter.mapaInterface {

    public static final String TAG = "ViewModel";

    private static ViewModelMapa viewModelMapa;
    private MarcadorList marcadorList;
    private final DatabaseAdapter dbA;

    public ViewModelMapa(@NonNull Application application) {
        super(application);
        dbA = new DatabaseAdapter(this);
        marcadorList = new MarcadorList();
        MarcadorClass title = new MarcadorClass("Selecciona un marcador");
        marcadorList.getMarcadores().add(title);
        dbA.getLatLng();
    }

    public static ViewModelMapa getInstance(FragmentActivity application){
        if (viewModelMapa == null){
            System.out.println("viene aqui");
            viewModelMapa = new ViewModelProvider(application).get(ViewModelMapa.class);
        }
        return viewModelMapa;
    }

    public void guardarMarcador(String nombre, LatLng coords){
        marcadorList.guardarMarcador(nombre, coords);
    }

    public void eliminarMarcador(String nombre, LatLng coords){
        marcadorList.eliminarMarcador(nombre, coords);
    }

    public ArrayList<MarkerOptions> getMarkerOptionsList(){
        return marcadorList.getMarkerOptionsList();
    }

    public ArrayList<MarcadorClass> getMarcadores() {
        return marcadorList.getMarcadores();
    }


    @Override
    public void setLatLng(ArrayList<Double> lat, ArrayList<Double> lon) {
        //_lat.setValue(lat);
        marcadorList.set_lat(lat);
        //_lon.setValue(lon);
        marcadorList.set_lon(lon);
    }
}
