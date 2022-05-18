package com.example.weadives.PantallaMapa;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;

import com.example.weadives.AreaUsuario.UserClass;
import com.example.weadives.DatabaseAdapter;
import com.example.weadives.ViewModel;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import androidx.lifecycle.ViewModelProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import java.util.ArrayList;
import java.util.List;


public class MapaViewModel extends AndroidViewModel implements DatabaseAdapter.mapaInterface {

    public static final String TAG = "ViewModel";

    private static MapaViewModel mapaViewModel;
    private MarcadorList marcadorList;
    private final MutableLiveData<ArrayList<Double>> _lat;
    private final MutableLiveData<ArrayList<Double>> _lon;
    private final DatabaseAdapter dbA;

    public MapaViewModel(@NonNull Application application) {
        super(application);
        System.out.println("Lo hace");
        dbA = new DatabaseAdapter(this);
        System.out.println("1");
        _lat = new MutableLiveData<>();
        System.out.println("2");
        _lon = new MutableLiveData<>();
        System.out.println("3");
        dbA.getLatLng();
        System.out.println("4");
        marcadorList = new MarcadorList();
        System.out.println("5");
    }

    public static MapaViewModel getInstance(FragmentActivity application){
        if (mapaViewModel == null){
            System.out.println("viene aqui");
            mapaViewModel = new ViewModelProvider(application).get(MapaViewModel.class);
        }
        return mapaViewModel;
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

    @Override
    public void setLatLng(ArrayList<Double> lat, ArrayList<Double> lon) {
        //_lat.setValue(lat);
        marcadorList.set_lat(lat);
        //_lon.setValue(lon);
        marcadorList.set_lon(lon);
    }
    
    /*public void getCoordsData(){
        System.out.println("llama db");
        dbA.getLatLng();
    }*/
}
