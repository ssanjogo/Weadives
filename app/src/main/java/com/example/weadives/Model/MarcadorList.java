package com.example.weadives.Model;

import com.example.weadives.ViewModelAndExtras.ViewModelMapa;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class MarcadorList {

    private ArrayList<MarcadorClass> marcadorList;


    private ArrayList<Double> _lat;     // Array de Latitudes reales
    private ArrayList<Double> _lon;     // Array de Longitudes reales

    private String r_lat;
    private String r_lon;

    private static final DecimalFormat df = new DecimalFormat("##.##########");

    public MarcadorList() {
        this.marcadorList = new ArrayList<>();
    }

    public MarcadorList(ArrayList<MarcadorClass> marcadorListlist) {
        this.marcadorList = marcadorList;
    }

    public void guardarMarcador(String nombre, LatLng latLng){
        castCoord(latLng.longitude, latLng.latitude);
        marcadorList.add(new MarcadorClass(nombre, latLng, r_lat, r_lon));
    }

    public void eliminarMarcador(String nombre, LatLng latLng){
        marcadorList.remove(buscarMarcador(nombre, latLng));
    }
    public void deletePos(int pos){
        marcadorList.remove(pos);
    }

    public void insertPos(int pos,String nombre, LatLng latLng){
        castCoord(latLng.longitude, latLng.latitude);
        marcadorList.add(pos,new MarcadorClass(nombre, latLng, r_lat, r_lon));
    }
    public void insertPos(int pos,MarcadorClass m){
        marcadorList.add(pos,m);
    }



    public ArrayList<MarkerOptions> getMarkerOptionsList() {
        ArrayList<MarkerOptions> markerList = new ArrayList<>();
        for (MarcadorClass marker : marcadorList) {
            markerList.add(new MarkerOptions().title(marker.getName()).position(marker.getLatLng()));
        }
        return markerList;
    }

    public ArrayList<MarcadorClass> getMarcadores(){
        return marcadorList;
    }

    public void setMarcadores(ArrayList<MarcadorClass> marcadorList){
        this.marcadorList = marcadorList;
    }

    private MarcadorClass buscarMarcador(String name, LatLng pos){
        for(MarcadorClass marcador : marcadorList){
            if (marcador.getName().equals(name) && marcador.getLatLng().equals(pos)){
                return marcador;
            }
        }
        return null;
    }

    private void castCoord(Double lon, Double lat){

        Double smallest = Double.POSITIVE_INFINITY;
        Double dist;
        for (int x = 0; x < _lon.size(); x++){
            for (int y = 0; y < _lat.size(); y++){

                dist = Math.abs(lon - _lon.get(x)) + Math.abs(lat - _lat.get(y));
                if (dist < smallest){
                    smallest = dist;

                    r_lat = df.format(_lat.get(x));
                    r_lon = df.format(_lon.get(y));
                }
            }
        }
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
