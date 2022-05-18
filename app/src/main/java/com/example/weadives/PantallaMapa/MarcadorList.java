package com.example.weadives.PantallaMapa;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MarcadorList {

    private ArrayList<MarcadorClass> marcadorList;




    private ArrayList<Double> _lat;
    private ArrayList<Double> _lon;

    public MarcadorList() {
        this.marcadorList = new ArrayList<>();
        this._lat = _lat;
        this._lon = _lon;
    }

    public void guardarMarcador(String nombre, LatLng latLng){
        marcadorList.add(new MarcadorClass(nombre, latLng, _lat, _lon));
    }

    public void eliminarMarcador(String nombre, LatLng latLng){
        marcadorList.remove(buscarMarcador(nombre, latLng));
    }

    public ArrayList<MarkerOptions> getMarkerOptionsList() {
        ArrayList<MarkerOptions> markerList = new ArrayList<>();
        for (MarcadorClass marker : marcadorList) {
            markerList.add(new MarkerOptions().title(marker.getName()).position(marker.getLatLng()));
        }
        return markerList;
    }

    private MarcadorClass buscarMarcador(String name, LatLng pos){
        for(MarcadorClass marcador : marcadorList){
            if (marcador.getName() == name && marcador.getLatLng() == pos){
                return marcador;
            }
        }
        return null;
    }

    public ArrayList<Double> get_lat() {
        return _lat;
    }

    public ArrayList<Double> get_lon() {
        return _lon;
    }

    public void set_lat(ArrayList<Double> _lat) {
        this._lat = _lat;
    }

    public void set_lon(ArrayList<Double> _lon) {
        this._lon = _lon;
    }

}
