package com.example.weadives.ViewModelAndExtras;

import android.app.Application;
import android.content.res.Resources;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.weadives.PantallaMapa.MarcadorClass;
import com.example.weadives.PantallaMapa.MarcadorList;
import com.example.weadives.R;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;


public class ViewModelMapa extends AndroidViewModel implements DatabaseAdapter.mapaInterface {

    public static final String TAG = "ViewModel";

    private static ViewModelMapa viewModelMapa;
    private MarcadorList marcadorList;
    private final DatabaseAdapter dbA;
    private Resources r;

    public Resources getR() {
        return r;
    }

    public void setR(Resources r) {
        this.r = r;
    }

    public ViewModelMapa(@NonNull Application application) {
        super(application);
        dbA = new DatabaseAdapter(this);
        marcadorList = new MarcadorList();

    }



    public static ViewModelMapa getInstance(FragmentActivity application){
        if (viewModelMapa == null){
            viewModelMapa = new ViewModelProvider(application).get(ViewModelMapa.class);
        }
        return viewModelMapa;
    }

    public static ViewModelMapa getInstance(FragmentActivity application, Resources r){
        if (viewModelMapa == null){
            viewModelMapa = new ViewModelProvider(application).get(ViewModelMapa.class);
            viewModelMapa.setR(r);
            MarcadorClass title2 = new MarcadorClass(r.getString(R.string.marcador_vacio));

            viewModelMapa.marcadorList.getMarcadores().add(title2);
            viewModelMapa.dbA.getLatLng();
        }
        return viewModelMapa;
    }

    public void updateTextSelect(Resources r){
        setR(r);
        viewModelMapa.marcadorList.deletePos(0);
        MarcadorClass title2 = new MarcadorClass(r.getString(R.string.marcador_vacio));
        viewModelMapa.marcadorList.insertPos(0,title2);
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
