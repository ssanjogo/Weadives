package com.example.weadives.ViewModelAndExtras;

import static java.lang.System.exit;

import android.app.Application;
import android.net.Uri;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;

import com.example.weadives.Model.PublicacionClass;
import com.example.weadives.Model.UserClass;
import com.example.weadives.ViewModelAndExtras.ViewModelParametros;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ViewModel extends AndroidViewModel implements  DatabaseAdapter.vmInterface {

    public static final String TAG = "ViewModel";

    private final MutableLiveData<List<UserClass>> listaUsuarios;
    private final MutableLiveData<List<UserClass>> listaAmigos;
    private final MutableLiveData<List<UserClass>> listaRecyclerView;
    private final MutableLiveData<String> mToast;
    private final MutableLiveData<UserClass> usuario;

    private String url;
    private boolean statusLogIn = false, keepSession = false;
    private final DatabaseAdapter dbA;

    private static ViewModel vm;
    private ArrayList<PublicacionClass> listaTemp;
    private MutableLiveData<ArrayList<PublicacionClass>> mutableListaTemp;
    private boolean acces = false;
    private boolean I = false;

    public static ViewModel getInstance(AppCompatActivity application){
        if (vm == null){
            System.out.println("PASAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
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
        this.usuario = new MutableLiveData<>();
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

    public String getUrl(){
        return this.url;
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
        System.out.println("GET CURRENT USER " + usuario);
        return this.usuario.getValue();
    }

    public void register(String nombre, String correo, String contraseña){
        setLogInStatus(true);
        dbA.register(nombre, correo, contraseña);
        if (usuario.getValue() != null) {
            listaUsuarios.getValue().add(usuario.getValue());
            listaUsuarios.setValue(listaUsuarios.getValue());
        }
    }

    public String getNom(){
        return usuario.getValue().getUsername();
    }
    public String getUserId(){
        return usuario.getValue().getId();
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
        System.out.println("PASAAAAA " + usuario.getValue());
        this.usuario.setValue(null);
        System.out.println("PASAAAAA2 " + usuario.getValue());
        dbA.singout();
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

    public void subirImagen(Uri uri) {
        dbA.subirImagen(uri, getUserId());
    }

    public void cambiarImagen(String imageUri){
        System.out.println("PASA POR CAMBIAR IMAGEN");
        HashMap<String, Object> usuario = convertUserToHashMap(getCurrentUser());
        dbA.updateDatos(usuario);
    }


    public void cambiarImagen2(String imageUri){
        UserClass user = getCurrentUser();
        user.setUrlImg(imageUri.toString());
        HashMap<String, Object> usuario = convertUserToHashMap(user);
        dbA.updateDatos(usuario);
        this.usuario.setValue(user);
    }

    public void enviarSolicitud(String idSolicitado){
        UserClass u = getUserByUID(idSolicitado);

        if (usuario.getValue().getStringSolicitudesEnviadas().equals("")){
            this.usuario.getValue().setStringSolicitudesEnviadas(usuario.getValue().getStringSolicitudesEnviadas() + idSolicitado);
        } else {
            this.usuario.getValue().setStringSolicitudesEnviadas(usuario.getValue().getStringSolicitudesEnviadas() + "," + idSolicitado);
        }
        HashMap<String, Object> hmCurrentUser = convertUserToHashMap(usuario.getValue());
        dbA.updateDatos(hmCurrentUser);

        if (u.getStringSolicitudesRecibidas().equals("")){
            u.setStringSolicitudesRecibidas(u.getStringSolicitudesRecibidas() + this.usuario.getValue().getId());
        } else {
            u.setStringSolicitudesRecibidas(u.getStringSolicitudesRecibidas() + "," + this.usuario.getValue().getId());
        }
        HashMap<String, Object> hmFuturoAmigo = convertUserToHashMap(u);


        dbA.updateDatos(hmFuturoAmigo);
        // Enviamos solicitud de amigo
        createSolicitud(usuario.getId(),u.getId());


        fillUserList();
    }

    private void createSolicitud(String idS, String idR){
        HashMap<String, Object> solicitudData = new HashMap<>();
        solicitudData.put("idS", idS);
        solicitudData.put("idR", idR);
        dbA.createFriendNoti(solicitudData);

    }

    public void cancelarEnvioSolicitud(String idSolicitado){
        UserClass u = getUserByUID(idSolicitado);
        String solE = "", solR = "";
        boolean first = true;

        for (String id : this.usuario.getValue().getListaSolicitudesEnviadas()){
            if (!id.equals(idSolicitado) && first){
                solE += id;
                first = false;
            } else if (!id.equals(idSolicitado) && !first){
                solE += ("," + id);
            }
        }
        this.usuario.getValue().setStringSolicitudesEnviadas(solE);
        HashMap<String, Object> hmCurrentUser = convertUserToHashMap(usuario.getValue());
        dbA.updateDatos(hmCurrentUser);

        first = true;
        for (String id2 : u.getListaSolicitudesRecibidas()){
            if (!id2.equals(idSolicitado) && first){
                solR += id2;
                first = false;
            } else if (!id2.equals(idSolicitado) && !first){
                solR += ("," + id2);
            }
        }
        u.setStringSolicitudesRecibidas(solR);
        HashMap<String, Object> hmFuturoAmigo = convertUserToHashMap(u);
        dbA.updateDatos(hmFuturoAmigo);

        fillUserList();
    }

    protected UserClass eliminarDeSolE(UserClass u, String id2){
        String solE = "";
        boolean first = true;
        for (String id : u.getListaSolicitudesEnviadas()){
            if (!id.equals(id2) && first){
                solE += id;
                first = false;
            } else if (!id.equals(id2) && !first){
                solE += ("," + id);
            }
        }
        u.setStringSolicitudesEnviadas(solE);
        return u;
    }

    protected UserClass eliminarDeSolR(UserClass u, String id2){
        String solR = "";
        boolean first = true;
        for (String id : u.getListaSolicitudesRecibidas()){
            if (!id.equals(id2) && first){
                solR += id;
                first = false;
            } else if (!id.equals(id2) && !first){
                solR += ("," + id);
            }
        }
        u.setStringSolicitudesRecibidas(solR);
        return u;
    }

    public void aceptarSolicitud(String idSolicita){
        UserClass u = getUserByUID(idSolicita);

        if (usuario.getValue().getStringAmigos().equals("")){
            this.usuario.getValue().setStringAmigos(usuario.getValue().getStringAmigos() + idSolicita);
        } else {
            this.usuario.getValue().setStringAmigos(usuario.getValue().getStringAmigos() + "," + idSolicita);
        }
        usuario.setValue(eliminarDeSolR(usuario.getValue(), idSolicita));
        HashMap<String, Object> hmCurrentUser = convertUserToHashMap(usuario.getValue());
        dbA.updateDatos(hmCurrentUser);

        if (u.getStringAmigos().equals("")){
            u.setStringAmigos(u.getStringAmigos() + this.usuario.getValue().getId());
        } else {
            u.setStringAmigos(u.getStringAmigos() + "," + this.usuario.getValue().getId());
        }
        u = eliminarDeSolE(u, this.usuario.getValue().getId());
        HashMap<String, Object> hmFuturoAmigo = convertUserToHashMap(u);
        dbA.updateDatos(hmFuturoAmigo);

        fillUserList();
    }

    public void rechazarSolicitud(String idSolicita){
        UserClass u = getUserByUID(idSolicita);

        usuario.setValue(eliminarDeSolR(usuario.getValue(), idSolicita));
        HashMap<String, Object> hmCurrentUser = convertUserToHashMap(usuario.getValue());
        dbA.updateDatos(hmCurrentUser);

        u = eliminarDeSolE(u, this.usuario.getValue().getId());
        HashMap<String, Object> hmFuturoAmigo = convertUserToHashMap(u);
        dbA.updateDatos(hmFuturoAmigo);

        fillUserList();
    }

    public void unfollow(String idAmigo){
        UserClass user = getUserByUID(idAmigo);
        String amigos = "";
        boolean first = true;
        for(String uid: usuario.getValue().getListaAmigos()){
            if (!idAmigo.equals(uid) && first){
                amigos += uid;
                first = false;
            } else if (!(usuario.getValue().getId()).equals(uid) && !first){
                amigos += ("," + uid);
            }
        }
        usuario.getValue().setStringAmigos(amigos);
        HashMap<String, Object> hmUsuario = convertUserToHashMap(usuario.getValue());
        dbA.updateDatos(hmUsuario);

        amigos = "";
        first = true;
        for(String uid: user.getListaAmigos()){
            if (!(usuario.getValue().getId()).equals(uid) && first){
                amigos += uid;
                first = false;
            } else if (!(usuario.getValue().getId()).equals(uid) && !first){
                amigos += ("," + uid);
            }
        }
        user.setStringAmigos(amigos);
        HashMap<String, Object> hmUsuarioA = convertUserToHashMap(user);
        dbA.updateDatos(hmUsuarioA);

        fillUserList();
    }

    public void buscarPorNombre(String nombre) {
        List<UserClass> listaFiltradaPorNombre = new ArrayList<>();
        for (UserClass user : getListaUsers().getValue()){
            if (user.getUsername().contains(nombre) && !user.getId().equals(this.usuario.getValue().getId())){
                listaFiltradaPorNombre.add(user);
            }
        }
        this.listaRecyclerView.setValue(listaFiltradaPorNombre);
    }

    public void fillUserList() {
        UserClass currentUser = this.usuario.getValue();
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
        if (usuario.getValue().getStringSolicitudesRecibidas().equals("")){
            return -1;
        }
        return usuario.getValue().getListaSolicitudesRecibidas().size();
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
    public void setImage(String url) {
        this.url = url;
        this.usuario.getValue().setUrlImg(url);
        //System.out.println("USUARIO: " + this.usuario.toString());
        cambiarImagen(url);
    }

    @Override
    public void setUser(UserClass u) {
        this.usuario.setValue(u);
        System.out.println("VIEW MODEL " + usuario.getValue());
        ViewModelParametros.getSingletonInstance().setUser(u);
        System.out.println("VIEW MODEL2 " + usuario.getValue());

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
        //System.out.println("SOY EL VIEWMODEL RECIBIENDO ");
        //System.out.println(lista);
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
        //System.out.println("SOY EL VIEWMODEL PIDIENDO ");
        dbA.getPublicationsFromUsuario(id);
        acces=false;
    }

    public ArrayList<PublicacionClass> getPublicationsFrom(){

        //System.out.println("Metodo 3 ");
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

    public void setI(boolean b) {
        this.I = b;
    }

    public boolean isI(){
        return I;
    }
}