package com.example.weadives;

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

    public static final String TAG = "ViewModel";

    private final MutableLiveData<List<UserClass>> listaUsuarios;
    private final MutableLiveData<List<UserClass>> listaRecyclerView;
    private final MutableLiveData<UserClass> usuario;

    private String UID;
    private boolean statusLogIn = false;
    private DatabaseAdapter dbA;

    private static ViewModel vm;

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
        statusLogIn = false;
        dbA = new DatabaseAdapter(this);
        dbA.getAllUsers();
    }

    public LiveData<List<UserClass>> getListaUsers(){
        return listaUsuarios;
    }

    public LiveData<List<UserClass>> getListaRecyclerView(){
        return this.listaRecyclerView;
    }

    public UserClass getUserByUID(String uid){
        System.out.println(listaUsuarios.getValue());
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

    public void register(String nombre, String correo, String contraseña){
        dbA.register(nombre, correo, contraseña);
        UserClass u = new UserClass(UID, nombre, correo, "https://www.pngmart.com/files/21/Account-User-PNG-Photo.png", "", "", "");
        if (u != null) {
            listaUsuarios.getValue().add(u);
            // Inform observer.
            listaUsuarios.setValue(listaUsuarios.getValue());

        }
    }

    public void logIn(String correo, String contraseña){
        dbA.logIn(correo, contraseña);

    }

    public void setLogInStatus (boolean b){
        this.statusLogIn = b;
    }

    public boolean getLogInStatus (){
        return this.statusLogIn;
    }

    public void singOut(){
        dbA.singout();
        listaRecyclerView.setValue(null);
        this.usuario.setValue(null);
    }

    public void deleteAccount(){
        dbA.deleteAccount();
    }

    public void cambiarCorreo(String correo){
        UserClass user = getCurrentUser();
        user.setCorreo(correo);
        HashMap<String, Object> usuario = convertUserToHashMap(user);
        dbA.cambiarCorreo(correo);
        dbA.updateDatos(usuario);
    }

    public void cambiarNombre(String nombre){
        UserClass user = getCurrentUser();
        user.setUsername(nombre);
        HashMap<String, Object> usuario = convertUserToHashMap(user);
        dbA.updateDatos(usuario);
    }

    public void cambiarContraseña(String contraseña) {
        dbA.cambiarContraseña(contraseña);
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
    //DvELOjMzXIfa1vnwU9CqVgvkv3p1,mEisP2TzCHMD7THo8GqUNITTET03

    public void enviarsolicitud(String idAmigo) {
        UserClass currentUser = usuario.getValue();
        UserClass futuroAmigo = getUserByUID(idAmigo);

        String solE = "";
        if (currentUser.getStringSolicitudesEnviadas().length() > 0){
            solE += currentUser.getStringSolicitudesEnviadas() + "," + idAmigo;
        }
        solE += currentUser.getStringSolicitudesEnviadas() + idAmigo;
        currentUser.setStringSolicitudesEnviadas(solE);
        HashMap<String, Object> hmCurrentUser = convertUserToHashMap(currentUser);
        dbA.updateDatos(hmCurrentUser);

        String solR = "";
        if (futuroAmigo.getStringSolicitudesRecibidas().length() > 0){
            solR += futuroAmigo.getStringSolicitudesRecibidas() + "," + currentUser.getId();
        }
        solR = futuroAmigo.getStringSolicitudesRecibidas() + currentUser.getId();
        futuroAmigo.setStringSolicitudesRecibidas(solR);
        HashMap<String, Object> hmFuturoAmigo = convertUserToHashMap(futuroAmigo);
        dbA.updateDatos(hmFuturoAmigo);

        fillUserList();
    }

    public void cancelarEvioSolicitud(String idAmigo) {
        int i = 0;
        String solR = "";
        UserClass futuroAmigo = getUserByUID(idAmigo);
        for(String uid: futuroAmigo.getListaSolicitudesRecibidas()){
            if (!idAmigo.equals(uid)){
                if (i == 0){
                    solR += uid;
                } else {
                    solR += ("," + uid);
                }
                i++;
            }
        }
        futuroAmigo.setStringSolicitudesRecibidas(solR);
        HashMap<String, Object> hmFuturoAmigo = convertUserToHashMap(futuroAmigo);
        dbA.updateDatos(hmFuturoAmigo);

        i = 0;
        String solE = "";
        UserClass currentUser = usuario.getValue();
        for(String uid: currentUser.getListaSolicitudesEnviadas()){
            if (!currentUser.getId().equals(uid)){
                if (i == 0){
                    solE += uid;
                } else {
                    solE += ("," + uid);
                }
                i++;
            }
        }
        currentUser.setStringSolicitudesEnviadas(solE);
        HashMap<String, Object> hmCurrentUser = convertUserToHashMap(currentUser);
        dbA.updateDatos(hmCurrentUser);
        fillUserList();
    }

    public void aceptarSolicitud(UserClass usuarioSolicita){
        UserClass currentUser = usuario.getValue();

        int i = 0;
        String solR = "";
        String solE = "";
        String amigos = "";
        for(String uid: currentUser.getListaSolicitudesRecibidas()){
            if (!usuarioSolicita.getId().equals(uid)){
                if (i == 0){
                    solR += uid;
                } else {
                    solR += ("," + uid);
                }
                i++;
            }
        }
        currentUser.setStringSolicitudesRecibidas(solR);
        this.usuario.setValue(currentUser);
        HashMap<String, Object> hmCurrentUser = convertUserToHashMap(currentUser);
        dbA.updateDatos(hmCurrentUser);

        i = 0;

        for(String uid: usuarioSolicita.getListaSolicitudesEnviadas()){
            if (!currentUser.getId().equals(uid)){
                if (i == 0){
                    solE += uid;
                } else {
                    solE += ("," + uid);
                }
                i++;
            }
        }
        usuarioSolicita.setStringSolicitudesEnviadas(solE);
        amigos += usuarioSolicita.getStringAmigos() + currentUser.getId();
        usuarioSolicita.setStringAmigos(amigos);
        HashMap<String, Object> hmUsuarioSolicita = convertUserToHashMap(usuarioSolicita);
        dbA.updateDatos(hmUsuarioSolicita);
        fillUserList();
    }

    public void rechazarSolicitud(UserClass usuarioSolicita){
        UserClass currentUser = usuario.getValue();
        String solR = "", solE = "";
        int i = 0;

        for(String uid: currentUser.getListaSolicitudesRecibidas()){
            if (!usuarioSolicita.getId().equals(uid)){
                if (i == 0){
                    solR += uid;
                } else {
                    solR += ("," + uid);
                }
                i++;
            }
        }
        currentUser.setStringSolicitudesRecibidas(solR);
        this.usuario.setValue(currentUser);
        HashMap<String, Object> hmCurrentUser = convertUserToHashMap(currentUser);
        dbA.updateDatos(hmCurrentUser);

        i = 0;

        for(String uid: usuarioSolicita.getListaSolicitudesEnviadas()){
            if (!currentUser.getId().equals(uid)){
                if (i == 0){
                    solE += uid;
                } else {
                    solE += ("," + uid);
                }
                i++;
            }
        }
        usuarioSolicita.setStringSolicitudesEnviadas(solE);
        HashMap<String, Object> hmUsuarioSolicita = convertUserToHashMap(usuarioSolicita);
        dbA.updateDatos(hmUsuarioSolicita);
        fillUserList();
    }

    public void buscarPorNombre(String nombre) {
        List<UserClass> listaFiltradaPorNombre = new ArrayList<>();
        for (UserClass usuario : getListaUsers().getValue()){
            if (usuario.getUsername().contains(nombre) && !usuario.equals(this.usuario)){
                listaFiltradaPorNombre.add(usuario);
            }
        }
        this.listaRecyclerView.setValue(listaFiltradaPorNombre);
    }

    public int fillUserList() {
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
        this.listaRecyclerView.setValue(listaUsers);
        return getCurrentUser().getListaSolicitudesRecibidas().size();
    }

    public boolean correoRepetido(String correo) {
        for (int i = 0; i < listaUsuarios.getValue().size(); i++) {
            if (listaUsuarios.getValue().get(i).getCorreo().equals(correo)){
                return true;
            }
        }
        return false;
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

    public int sizeListaSolPendientes(){
        if (getCurrentUser().getStringSolicitudesEnviadas().equals("")){
            return 0;
        } else {
            return this.usuario.getValue().getListaSolicitudesRecibidas().size();
        }
    }

    @Override
    public void setCollection(ArrayList<UserClass> listaUsuarios) {
        this.listaUsuarios.setValue(listaUsuarios);
    }

    @Override
    public void setStatusLogIn(boolean status) {
        this.statusLogIn = status;
    }

    @Override
    public void setUserID(String id) {
        this.UID = id;
        //this.usuario.setValue(getUserByUID(id));
    }

    @Override
    public void setUser(UserClass u) {
        this.usuario.setValue(u);
    }
}
