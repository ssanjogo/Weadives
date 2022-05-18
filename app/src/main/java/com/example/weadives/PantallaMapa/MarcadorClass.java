package com.example.weadives.PantallaMapa;


import androidx.lifecycle.MutableLiveData;

import com.example.weadives.AreaUsuario.UserClass;
import com.example.weadives.DatabaseAdapter;
import com.example.weadives.ViewModel;
import com.google.android.gms.maps.model.LatLng;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MarcadorClass{

    private LatLng latLng;
    private String name;
    private ArrayList<Double> _lat;
    private ArrayList<Double> _lon;
    private String r_lat, r_lon;
    private DatabaseAdapter db;
    private static final DecimalFormat df = new DecimalFormat("00.0000000000");

    public MarcadorClass(String name, LatLng latLng, ArrayList<Double> _lat, ArrayList<Double> _lon) {
        this.name = name;
        this.latLng = latLng;
        this._lat = _lat;
        this._lon = _lon;
        castCoord(latLng.longitude, latLng.latitude);
        System.out.println(latLng + r_lat + r_lon);
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private void castCoord(Double lon, Double lat){

        Double smallest = Double.POSITIVE_INFINITY;
        Double dist;
        for (int x = 0; x < _lon.size(); x++){
            for (int y = 0; y < _lat.size(); y++){

                dist = Math.abs(lon - _lon.get(x)) + Math.abs(lat - _lat.get(y));
                if (dist < smallest){
                    smallest = dist;

                    r_lat = df.format(_lon.get(x));
                    r_lon = df.format(_lat.get(y));
                }
            }
        }
    }

    public void updateCoordsData(ArrayList<Double> _lat, ArrayList<Double> _lon){
        this._lat = _lat;
        this._lon = _lon;
    }

}
