package com.example.weadives.Model;



import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class MarcadorClass{

    private LatLng latLng;
    private String name;
    private String r_lat, r_lon;


    public MarcadorClass(String name, LatLng latLng, String _lat, String _lon) {
        this.name = name;
        this.latLng = latLng;
        this.r_lat = _lat;
        this.r_lon = _lon;
    }

    public MarcadorClass(String name){
        this.name = name;
        this.latLng = new LatLng(0,0);
        this.r_lat = "0";
        this.r_lon = "0";
    }
    public MarcadorClass(String name,LatLng latLng){
        this.name = name;
        this.latLng = latLng;
        this.r_lat = "0";
        this.r_lon = "0";
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

    public String getLocation(){
        return r_lat + "_" + r_lon;
    }

    @Override
    public String toString(){
        return this.name;
    }

    public String stringToSave(){
        return name + "," + latLng.latitude + "," + latLng.longitude + "," + r_lat + "," + r_lon + ",";
    }

}
