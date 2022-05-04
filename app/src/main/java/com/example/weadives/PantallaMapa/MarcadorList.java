package com.example.weadives.PantallaMapa;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MarcadorList {

    private ArrayList<MarcadorClass> marcadorList;

    public MarcadorList() {
        this.marcadorList = new ArrayList<>();
        marcadorList.add(new MarcadorClass("Marcador", new LatLng(3.2652983846256944,72.83189155161381)));
    }

    public MarcadorList(ArrayList<MarcadorClass> marcadorList) {
        this.marcadorList = marcadorList;
    }

    public void guardarMarcador(String nombre, LatLng latLng){
        marcadorList.add(new MarcadorClass(nombre, latLng));
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

    private void aver(){
        for(MarcadorClass marcador : marcadorList){
            System.out.println(marcador.getName() +" "+ marcador.getLatLng());
        }
    }



}
