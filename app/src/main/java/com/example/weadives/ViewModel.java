package com.example.weadives;

import android.app.Application;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;

import com.example.weadives.AreaUsuario.UserClass;
import com.google.firebase.firestore.auth.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ViewModel extends AndroidViewModel implements  DatabaseAdapter.vmInterface {

    private final MutableLiveData<ArrayList<UserClass>> listaUsuarios;
    private final MutableLiveData<UserClass> usuario;
    private boolean statusLogIn = false;
    private DatabaseAdapter dbA;

    private String UID;

    public static final String TAG = "ViewModel";

    static ViewModel vm;

    public static ViewModel getInstance(AppCompatActivity application){
        if (vm == null){
            vm = new ViewModelProvider(application).get(ViewModel.class);;
        }
        return vm;
    }

    public ViewModel(Application application) {
        super(application);

        listaUsuarios = new MutableLiveData<>();
        usuario = new MutableLiveData<>();
        dbA = new DatabaseAdapter(this);
        dbA.getCollection();
    }

    public ArrayList<UserClass> getListaUsers(){
        return listaUsuarios.getValue();
    }

    public UserClass getUserByUID(String uid){
        for (int i = 0; i < listaUsuarios.getValue().size(); i++) {
            if (listaUsuarios.getValue().get(i).getId().equals(uid)){
                return listaUsuarios.getValue().get(i);
            }
        }
        return null;
    }

    public UserClass getCurrentUser(){
        return usuario.getValue();
    }

    public String getCurrentUserUID(){
        return UID;
    }

    public void register(String nombre, String correo, String contraseña){
        dbA.register(nombre, correo, contraseña);
        String uid = getCurrentUserUID();
        UserClass u = new UserClass(uid, nombre, correo, "", "", "", "");
        if (u != null) {
            listaUsuarios.getValue().add(u);
            // Inform observer.
            listaUsuarios.setValue(listaUsuarios.getValue());
            usuario.setValue(u);
        }
    }

    public void logIn(String correo, String contraseña, boolean login){
        dbA.logIn(correo, contraseña);
        usuario.setValue(getUserByUID(getCurrentUserUID()));
        setLogInStatus(login);
    }

    public void setLogInStatus (boolean b){
        this.statusLogIn = b;
    }

    public boolean getLogInStatus (){
        return this.statusLogIn;
    }

    public void singOut(){
        dbA.singout();
        this.usuario.setValue(null);
    }

    public boolean correoRepetido(String correo) {
        for (int i = 0; i < listaUsuarios.getValue().size(); i++) {
            if (listaUsuarios.getValue().get(i).getCorreo().equals(correo)){
                return true;
            }
        }
        return false;
    }

    public void cambiarCorreo(String nuevoDato){
        UserClass usuario = getCurrentUser();
        usuario.setCorreo(nuevoDato);
        dbA.cambiarCorreo(nuevoDato, usuario);
    }

    public void cambiarNombre(String nuevoDato){
        UserClass user = getCurrentUser();
        user.setUsername(nuevoDato);
        HashMap<String, Object> usuario = convertUserToHashMap(user);
        dbA.cambiarUsername(nuevoDato, usuario);
    }

    private HashMap<String, Object> convertUserToHashMap(UserClass user){
        HashMap<String, Object> usuario = new HashMap<>();
        usuario.put("Nombre", user.getUsername());
        usuario.put("Correo", user.getCorreo());
        usuario.put("UID", user.getId());
        usuario.put("Imagen", user.getUrlImg());
        usuario.put("Amigos", user.getStringAmigos());
        usuario.put("Solicitudes recibidas", user.getStringSolicitudesRecibidas());
        usuario.put("Solicitudes enviadas", user.getStringSolicitudesEnviadas());

        return usuario;
    }

    public void deleteAccount(){
        dbA.deleteAccount();
    }

    @Override
    public void setCollection(ArrayList<UserClass> listaUsuarios) {
        this.listaUsuarios.setValue(listaUsuarios);
    }

    @Override
    public void setUserId(String id) {
        this.UID = id;
    }

    @Override
    public void setStatusLogIn(boolean status) {
        this.statusLogIn = status;
    }
}
