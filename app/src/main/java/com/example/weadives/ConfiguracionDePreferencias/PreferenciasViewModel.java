package com.example.weadives.ConfiguracionDePreferencias;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;

import com.example.weadives.ViewModelAndExtras.DatabaseAdapter;

import java.util.ArrayList;
import java.util.Map;

public class PreferenciasViewModel extends AndroidViewModel implements DatabaseAdapter.preferenciasInterface{

    private static PreferenciasViewModel preferenciasViewModel;
    private final DatabaseAdapter dbA;
    private MutableLiveData<String> idNotification;

    public PreferenciasViewModel(@NonNull Application application) {
        super(application);
        dbA = new DatabaseAdapter(this);
        idNotification = new MutableLiveData<>();
    }

    public static PreferenciasViewModel getInstance(AppCompatActivity application){
        if (preferenciasViewModel == null){
            preferenciasViewModel = new ViewModelProvider(application).get(PreferenciasViewModel.class);
        }
        return preferenciasViewModel;
    }

    public void createCoordsNotification(String coords, Map<String, Object> data) {
        dbA.createCoordsNotification(coords, data);
    }

    @Override
    public void setNotificationId(String id) {
        idNotification.setValue(id);
    }
}
