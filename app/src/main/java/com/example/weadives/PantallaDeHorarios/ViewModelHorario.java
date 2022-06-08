package com.example.weadives.PantallaDeHorarios;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.weadives.ViewModelAndExtras.DatabaseAdapter;
import com.example.weadives.Model.MarcadorClass;

public class ViewModelHorario extends AndroidViewModel implements DatabaseAdapter.horarioInterface{


    private static ViewModelHorario viewModelHorario;
    private MarcadorClass horarioLocation;

    private String csvRef;
    private DatabaseAdapter dba;

    public ViewModelHorario (@NonNull Application application){
        super(application);
        dba = new DatabaseAdapter(this);
        //csvRef = "app/src/main/assets/TEST.csv";
    }

    public static ViewModelHorario getInstance(AppCompatActivity application){
        if (viewModelHorario == null){
            viewModelHorario = new ViewModelProvider(application).get(ViewModelHorario.class);
        }
        return viewModelHorario;
    }

    public void getDataLocation() {
        dba.getStorageData(horarioLocation.getLocation());
    }

    public String getCsvRef(){
        return this.csvRef;
    }

    public void setCsvRef(String csvRef){
        this.csvRef = csvRef;
    }

    @Override
    public void getCsvRef(String CsvRef) {
        //System.out.println("Pasa por aqui?: " + CsvRef);
        setCsvRef(CsvRef);
    }

    public void setMarcador (MarcadorClass marcador){
        this.horarioLocation = marcador;
        //System.out.println("Marcador Horari: " + marcador);
    }

    public MarcadorClass getMarcador () { return this.horarioLocation; }
}
