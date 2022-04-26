package com.example.weadives.PantallaMapa;

import com.google.android.gms.maps.model.LatLng;

public class MarcadorClass {

    private LatLng latLng;
    private String name;

    public MarcadorClass(String name, LatLng latLng) {
        this.name = name;
        this.latLng = latLng;
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
}
