package com.example.weadives.ViewModelAndExtras;

import static android.content.Context.MODE_PRIVATE;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;

import com.example.weadives.Model.MarcadorClass;
import com.example.weadives.Model.MarcadorList;
import com.example.weadives.R;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.File;
import java.util.ArrayList;


public class ViewModelMapa extends AndroidViewModel implements DatabaseAdapter.mapaInterface {

    public static final String TAG = "ViewModel";

    private static ViewModelMapa viewModelMapa;
    private MarcadorList marcadorList;
    private final DatabaseAdapter dbA;
    private Resources r;
    private Context context;
    ArrayList<MarcadorClass> listaMarcador;
    private MutableLiveData<ArrayList<File>> fileData;

    public Resources getR() {
        return r;
    }

    public void setR(Resources r) {
        this.r = r;
    }

    public ViewModelMapa(@NonNull Application application) {
        super(application);
        context = getApplication().getApplicationContext();
        marcadorList = new MarcadorList();
        dbA = new DatabaseAdapter(this);
        cargarPersistencia();
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

    public void getWeatherImage(String type){
        dbA.getWeatherImage(context, type);
    }


    @Override
    public void setLatLng(ArrayList<Double> lat, ArrayList<Double> lon) {
        //_lat.setValue(lat);
        marcadorList.set_lat(lat);
        //_lon.setValue(lon);
        marcadorList.set_lon(lon);
    }

    public MutableLiveData<ArrayList<File>> getFileData() {
        if(fileData == null){
            fileData = new MutableLiveData<>();
        }
        return fileData;
    }

    @Override
    public void setImage(ArrayList<File> fileList) {
        fileData.setValue(fileList);
    }


    public void guardarPersistencia(){
        String save = comprimirArray( marcadorList.getMarcadores());
        SharedPreferences preferencias = context.getSharedPreferences("marcadores", MODE_PRIVATE);
        SharedPreferences.Editor editor=preferencias.edit();
        editor.putString("marcadores",save);
        editor.commit();

    }

    public void cargarPersistencia(){
        //Array de prueba
        ArrayList<MarcadorClass> marcadorList = new ArrayList<>();
        marcadorList.add(new MarcadorClass("test", new LatLng(2.4688644409,74.4644393921)));

        SharedPreferences preferencias = context.getSharedPreferences("marcadores",MODE_PRIVATE);

        marcadorList = descomprimirArray(preferencias.getString("marcadores", comprimirArray(marcadorList)));
        this.marcadorList.setMarcadores(marcadorList);
    }

    private String comprimirArray(ArrayList<MarcadorClass> marcadorList){
        String save = "";
        for(int i = 0; i < marcadorList.size(); i++){
            save += marcadorList.get(i).stringToSave() + "¿";
        }
        return save;
    }

    private ArrayList<MarcadorClass> descomprimirArray(String save){
        ArrayList<MarcadorClass> marcadorList = new ArrayList<>();
        String[] marcadorArray = save.split("¿");
        String[] fixArray;
        for(String marcadorString : marcadorArray){
            fixArray = marcadorString.split(",");
            marcadorList.add(new MarcadorClass(fixArray[0], new LatLng(Double.parseDouble(fixArray[1]), Double.parseDouble(fixArray[2])), fixArray[3], fixArray[4]));
        }
        return marcadorList;
    }

}
