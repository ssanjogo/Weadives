package com.example.weadives;

import static java.lang.System.exit;

import android.app.Application;
import android.net.Uri;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;

import com.example.weadives.AreaUsuario.UserClass;
import com.example.weadives.PantallaPerfilAmigo.PublicacionClass;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ViewModel extends AndroidViewModel implements  DatabaseAdapter.vmInterface {

    public static final String TAG = "ViewModel";

    private final MutableLiveData<List<UserClass>> listaUsuarios;
    private final MutableLiveData<List<UserClass>> listaAmigos;
    private final MutableLiveData<List<UserClass>> listaRecyclerView;
    private final MutableLiveData<String> mToast;
    private UserClass usuario;


    private String UID;
    private boolean statusLogIn = false, keepSession = false;
    private final DatabaseAdapter dbA;

    private static ViewModel vm;
    private ArrayList<PublicacionClass> listaTemp;
    private MutableLiveData<ArrayList<PublicacionClass>> mutableListaTemp;
    private boolean acces=false;

    public static ViewModel getInstance(AppCompatActivity application){
        if (vm == null){
            vm = new ViewModelProvider(application).get(ViewModel.class);
        }
        return vm;
    }

    public static ViewModel getInstance(){
        if (vm == null){
            exit(-1);
        }
        return vm;
    }

    public ViewModel(Application application) {
        super(application);
        listaUsuarios = new MutableLiveData<>();
        listaRecyclerView = new MutableLiveData<>();
        listaAmigos = new MutableLiveData<>();
        mToast = new MutableLiveData<>();
        statusLogIn = false;
        dbA = new DatabaseAdapter(this);
        dbA.getAllUsers();
        if(dbA.accountNotNull()){
            dbA.getUser2();
        }
        listaTemp=new ArrayList<>();
        mutableListaTemp =new MutableLiveData<>();
        mutableListaTemp.setValue(listaTemp);
    }

    public  MutableLiveData<ArrayList<PublicacionClass>> getMutable() {
        System.out.println("GETMUTABLE");
        System.out.println(mutableListaTemp);
        System.out.println(listaTemp);
        if (mutableListaTemp == null) {
            mutableListaTemp = new MutableLiveData<>();
        }

        return mutableListaTemp;
    }

    public LiveData<List<UserClass>> getListaUsers(){
        return listaUsuarios;
    }

    public LiveData<List<UserClass>> getListaRecyclerView(){
        return this.listaRecyclerView;
    }

    public LiveData<String> getToast(){
        return mToast;
    }

    public boolean accountNotNull(){
        return dbA.accountNotNull();
    }

    public boolean iskeepSession() {
        return this.keepSession;
    }

    public void keepSession(boolean ks){
        this.keepSession = ks;
    }

    public void getUser(){
        dbA.getUser2();
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
        return this.usuario;
    }

    public void register(String nombre, String correo, String contraseña){
        setLogInStatus(true);
        dbA.register(nombre, correo, contraseña);
        if (usuario != null) {
            listaUsuarios.getValue().add(usuario);
            listaUsuarios.setValue(listaUsuarios.getValue());
            reload();
        }
    }

    public String getNom(){
        return usuario.getUsername();
    }
    public String getUserId(){
        return usuario.getId();
    }

    public void logIn(String correo, String contraseña){
        setLogInStatus(true);
        dbA.logIn(correo, contraseña);
    }

    public void setLogInStatus (boolean b){
        this.statusLogIn = b;
    }

    public boolean getLogInStatus (){
        return this.statusLogIn;
    }

    public void singOut(){
        setLogInStatus(false);
        listaRecyclerView.setValue(null);
        this.usuario = null;
        dbA.singout();
    }

    public void deleteAccount(){
        System.out.println("PASAAAAAAAAAAAAAAAAAAA");
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

    public void cambiarImagen(Uri uri) {
        UserClass user = getCurrentUser();
        System.out.println("ASI DEBERIA SER LA URI: " + uri);
        user.setUrlImg(uri.toString());
        HashMap<String, Object> usuario = convertUserToHashMap(user);
        dbA.updateDatos(usuario);
        this.usuario = user;
    }

    public void cambiarImagen2(String imageUri){
        UserClass user = getCurrentUser();
        user.setUrlImg(imageUri.toString());
        HashMap<String, Object> usuario = convertUserToHashMap(user);
        dbA.updateDatos(usuario);
        this.usuario = user;
    }


    public void unfollow(String idAmigo) {
        int i = 0;
        String amigos = "";
        UserClass user = usuario;
        for(String uid: user.getListaAmigos()){
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

        reload();
        fillUserList();
    }

    public void enviarsolicitud(String idAmigo) {
        UserClass currentUser = usuario;
        UserClass futuroAmigo = getUserByUID(idAmigo);

        String solE = "";
        if (currentUser.getListaSolicitudesEnviadas().size() > 0){
            solE += currentUser.getStringSolicitudesEnviadas() + "," + idAmigo;
        }
        solE += currentUser.getStringSolicitudesEnviadas() + idAmigo;
        currentUser.setStringSolicitudesEnviadas(solE);
        HashMap<String, Object> hmCurrentUser = convertUserToHashMap(currentUser);
        dbA.updateDatos(hmCurrentUser);

        String solR = "";
        if (futuroAmigo.getListaSolicitudesRecibidas().size() > 0){
            solR += futuroAmigo.getStringSolicitudesRecibidas() + "," + currentUser.getId();
        }
        solR = futuroAmigo.getStringSolicitudesRecibidas() + currentUser.getId();
        futuroAmigo.setStringSolicitudesRecibidas(solR);
        HashMap<String, Object> hmFuturoAmigo = convertUserToHashMap(futuroAmigo);
        dbA.updateDatos(hmFuturoAmigo);

        reload();
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
        UserClass currentUser = usuario;
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
        reload();
        fillUserList();
    }

    public void aceptarSolicitud(UserClass usuarioSolicita){
        UserClass currentUser = usuario;

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
        this.usuario = currentUser;
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
        reload();
        fillUserList();
    }

    public void rechazarSolicitud(UserClass usuarioSolicita){
        UserClass currentUser = usuario;
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
        this.usuario = currentUser;
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
        reload();
        fillUserList();
    }

    public void getPublicaciones(){
        dbA.getPublicationsUsuario(this.UID);
    }

    public void buscarPorNombre(String nombre) {
        List<UserClass> listaFiltradaPorNombre = new ArrayList<>();
        for (UserClass user : getListaUsers().getValue()){
            if (user.getUsername().contains(nombre) && !user.getId().equals(this.usuario.getId())){
                listaFiltradaPorNombre.add(user);
            }
        }
        this.listaRecyclerView.setValue(listaFiltradaPorNombre);
    }

    public void fillUserList() {
        UserClass currentUser = this.usuario;
        System.out.println("AQUIIIIIIIIIIIIIIIIIIIII" + usuario);
        UserClass user;
        List<UserClass> listaUsers = new ArrayList<>();
        if (currentUser != null && !currentUser.getStringSolicitudesRecibidas().equals("")) {
            for (String uid : getCurrentUser().getListaSolicitudesRecibidas()) {
                user = getUserByUID(uid);
                if (user != null) {
                    listaUsers.add(user);
                }
            }
        }

        if (currentUser != null && !currentUser.getStringAmigos().equals("")) {
            for (String uid : getCurrentUser().getListaAmigos()) {
                user = getUserByUID(uid);
                if (user != null) {
                    listaUsers.add(user);
                }
            }
        }
        this.listaRecyclerView.setValue(listaUsers);
    }

    public int sizelista(){
        return usuario.getListaSolicitudesRecibidas().size();
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

    private void reload(){
        dbA.getAllUsers();
        dbA.getUser();
    }
    public void subirPublicacion(HashMap<String, String> coments, HashMap<String, String> likes, String parametros, String idPublicacion, String idUsuario){
        dbA.savePublicacion(coments,likes,parametros,idPublicacion,idUsuario);
    }
    public void updatePublicacion(HashMap<String, String> coments, HashMap<String, String> likes, String parametros, String idPublicacion, String idUsuario){
        dbA.updatePublicacion(coments,likes,parametros,idPublicacion,idUsuario);
    }
    public void deletePublicacion(String idPublicacion){
        dbA.deletePublicacion(idPublicacion);
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
    }

    @Override
    public void setUser(UserClass u) {
        this.usuario = u;
        ViewModelParametros.getSingletonInstance().setUser(u);

    }

    @Override
    public void setToast(String s) {
        mToast.setValue(s);
    }

    @Override
    public void notifyId(String id) {
        ViewModelParametros.getSingletonInstance().notifyId(id);
    }

    @Override
    public void setListaPublicacion(ArrayList<PublicacionClass> publicacionClasses) {
       ViewModelParametros.getSingletonInstance().setListaPublicacion(publicacionClasses);

    }

    @Override
    public void setListaPublicacionTemp(ArrayList<PublicacionClass> lista) {
        System.out.println("SOY EL VIEWMODEL RECIBIENDO ");
        System.out.println(lista);
        mutableListaTemp.setValue(lista);
        listaTemp=lista;
        ViewModel.getInstance().getMutable().setValue(lista);

        if(lista!=null){
            listaTemp=lista;
            mutableListaTemp.setValue(lista);

        }else{
            mutableListaTemp.setValue(new ArrayList<>());
        }
        acces=true;

    }
    public MutableLiveData<ArrayList<PublicacionClass>> getCurrentList() {
        if (mutableListaTemp == null) {
            mutableListaTemp = new MutableLiveData<ArrayList<PublicacionClass>>();
        }
        return mutableListaTemp;
    }



    public void setGetPublicationsFrom(String id){
        System.out.println("SOY EL VIEWMODEL PIDIENDO ");
        dbA.getPublicationsFromUsuario(id);
        acces=false;
    }

    public ArrayList<PublicacionClass> getPublicationsFrom(){

        System.out.println("Metodo 3 ");
        return new ArrayList<>();
        /*System.out.println(listaTemp);
        if(listaTemp.size()==0){
            return new ArrayList<>();
        }
        return listaTemp;*/
    }

    public boolean getAcces() {
        return acces;
    }

}