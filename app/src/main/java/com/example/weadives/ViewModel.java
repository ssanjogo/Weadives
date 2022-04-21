package com.example.weadives;

import android.app.Activity;
import android.app.Application;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;

import com.example.weadives.AreaUsuario.UserClass;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ViewModel extends AndroidViewModel implements  DatabaseAdapter.vmInterface {

    private final MutableLiveData<List<UserClass>> listaUsuarios;
    private final MutableLiveData<List<UserClass>> listaRecyclerView;
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
        listaRecyclerView = new MutableLiveData<>();
        usuario = new MutableLiveData<>();
        dbA = new DatabaseAdapter(this);
        dbA.getCollection();
    }

    public LiveData<List<UserClass>> getListaUsers(){
        return listaUsuarios;
    }

    public LiveData<List<UserClass>> getListaRecyclerView(){
        return this.listaRecyclerView;
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
        return this.usuario.getValue();
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
            this.usuario.setValue(u);
        }
    }

    public void logIn(String correo, String contraseña, boolean login){
        dbA.logIn(correo, contraseña);
        this.usuario.setValue(getUserByUID(getCurrentUserUID()));
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

    public boolean uidInListaSolicitudesEnviadas(String uidAmigo) {
        for (String uid : getCurrentUser().getListaSolicitudesEnviadas()){
            if (uidAmigo.equals(uid)){
                return true;
            }
        }
        return false;
    }

    public boolean uidInListaAmigos(String uidAmigo) {
        for (String uid : getCurrentUser().getListaAmigos()){
            if (uidAmigo.equals(uid)){
                return true;
            }
        }
        return false;
    }

    public void unfollow(String idAmigo) {
        int i = 0;
        String amigos = "";
        UserClass user = getCurrentUser();
        for(String uid: getCurrentUser().getListaAmigos()){
            if (!idAmigo.equals(uid)){
                if (i == 0){
                    amigos += uid;
                } else {
                    amigos += ("," + uid);
                }
                i++;
            }
        }
        user.setStringAmigos(amigos);
        HashMap<String, Object> usuario = convertUserToHashMap(user);
        dbA.unfollow(usuario);
        fillUserList();
    }

    public void buscarPorNombre(String nombre) {
        List<UserClass> listaFiltradaPorNombre = new ArrayList<>();
        for (UserClass usuario : getListaUsers().getValue()){
            if (usuario.getUsername().contains(nombre)){
                listaFiltradaPorNombre.add(usuario);
            }
        }
        this.listaRecyclerView.setValue(listaFiltradaPorNombre);
    }

    public void fillUserList() {
        UserClass user;
        List<UserClass> listaUsers = new ArrayList<>();
        System.out.println(getCurrentUser());
        if (!getCurrentUser().getStringSolicitudesRecibidas().equals("")) {
            for (String uid : getCurrentUser().getListaSolicitudesRecibidas()) {
                user = getUserByUID(uid);
                if (user != null) {
                    listaUsers.add(user);
                }
            }
        }

        if (!getCurrentUser().getStringAmigos().equals("")) {
            for (String uid : getCurrentUser().getListaAmigos()) {
                user = getUserByUID(uid);
                if (user != null) {
                    listaUsers.add(user);
                }
            }
        }
        System.out.println("METODO fillUserList" + listaUsers);
        this.listaRecyclerView.setValue(listaUsers);
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
