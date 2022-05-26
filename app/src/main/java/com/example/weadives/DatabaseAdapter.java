package com.example.weadives;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.weadives.AreaUsuario.UserClass;
import com.google.android.gms.tasks.Continuation;
import com.example.weadives.PantallaPerfilAmigo.PublicacionClass;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StreamDownloadTask;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import io.grpc.internal.JsonUtil;

public class DatabaseAdapter extends Activity {

    public static final String TAG = "DatabaseAdapter";

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final FirebaseStorage storage = FirebaseStorage.getInstance();
    private final StorageReference storageRef = storage.getReference();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseAuth.IdTokenListener mAuthIDTokenListener;
    private FirebaseUser user = mAuth.getCurrentUser();

    public static vmInterface listener;
    public static mapaInterface listenerMapa;
    public static horarioInterface listenerHorario;
    public static intentInterface listenerIntent;
    public static DatabaseAdapter databaseAdapter;

    public String path;



    public DatabaseAdapter(vmInterface listener){
        this.listener = listener;
        databaseAdapter = this;
        FirebaseFirestore.setLoggingEnabled(true);
    }

    public DatabaseAdapter(intentInterface listenerLogIn){
        this.listenerIntent = listenerLogIn;
        databaseAdapter = this;
        FirebaseFirestore.setLoggingEnabled(true);
    }

    public boolean accountNotNull() {
        return (this.mAuth.getCurrentUser() != null);
    }

    public DatabaseAdapter(mapaInterface mapaInterface){
        this.listenerMapa = mapaInterface;
        databaseAdapter = this;
        FirebaseFirestore.setLoggingEnabled(true);
    }

    public DatabaseAdapter(horarioInterface horarioInterface){
        this.listenerHorario = horarioInterface;
        databaseAdapter = this;
        FirebaseFirestore.setLoggingEnabled(true);
    }

    public DatabaseAdapter(String path){
        this.path = path;
    }

    public interface vmInterface{
        void setCollection(ArrayList<UserClass> listaUsuarios);
        void setStatusLogIn(boolean status);
        void setUserID(String id);
        void setUser(UserClass u);
        void setToast(String s);
        void notifyId(String id);
        void setListaPublicacion(ArrayList<PublicacionClass> publicacionClasses);
        void setListaPublicacionTemp(ArrayList<PublicacionClass> lista);
    }

    public interface vmpInterface{
        void setStatusLogIn(boolean status);
        void setUserID(String id);
        void setUser(UserClass u);
        void setListaPublicacion(ArrayList<PublicacionClass> publicacionClasses);
    }

    //Mapa
    public interface mapaInterface{
        void setLatLng(ArrayList<Double> lat, ArrayList<Double> lon);
    }

    //Horario
    public interface horarioInterface{
        void getCsvRef(String CsvRef);
    }


    public interface intentInterface {
        void intent();
    }


    public void register (String nombre, String correo, String contraseña){
        mAuth.createUserWithEmailAndPassword(correo, contraseña).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    //listener.setUserID(task.getResult().getUser().getUid());
                    saveUser(nombre, correo, task.getResult().getUser().getUid());
                } else {
                    Log.w(TAG, "Error register");
                }
            }
        });
    }

    public void logIn(String correo, String contraseña){
        mAuth.signInWithEmailAndPassword(correo, contraseña).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    listener.setUserID(task.getResult().getUser().getUid());
                    getUser();
                    System.out.println("LAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
                    getPublicationsUsuario(task.getResult().getUser().getUid());
                } else {
                    Log.e(TAG, "Error en el log in");
                }
            }
        });
    }

    public void getUser(){
        db.collection("Users").document(mAuth.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    UserClass u = new UserClass(document.getString("UID"), document.getString("Nombre"), document.getString("Correo"), document.getString("Imagen"), document.getString("Amigos"), document.getString("Solicitudes recibidas"), document.getString("Solicitudes enviadas"));
                    listener.setUser(u);
                    listenerIntent.intent();
                }
            }
        });
    }

    public void getUser2(){
        db.collection("Users").document(mAuth.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    UserClass u = new UserClass(document.getString("UID"), document.getString("Nombre"), document.getString("Correo"), document.getString("Imagen"), document.getString("Amigos"), document.getString("Solicitudes recibidas"), document.getString("Solicitudes enviadas"));
                    listener.setUser(u);
                }
            }
        });
    }

    public void getPublicationsUsuario(String idUsuario) {
        db.collection("Publicaciones").whereEqualTo("idUsuario", idUsuario).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    ArrayList<PublicacionClass> lista= new ArrayList<>();
                    for(DocumentSnapshot document : task.getResult()){
                        Map<String, Object> publi = document.getData();
                        ParametrosClass p = ParametrosClass.descomprimir(document.getString("Parametros")).get(0);
                        PublicacionClass pc = new PublicacionClass((HashMap<String, String>) publi.get("Map comentarios"), (HashMap<String, String>) publi.get("Map likes"), p, (String)publi.get("idPublicacion"), (String)publi.get("idUsuario"));
                        lista.add(pc);
                    }
                    listener.setListaPublicacion(lista);
                }
            }
        });
    }
    public void getPublicationsFromUsuario(String idUsuario) {
        db.collection("Publicaciones").whereEqualTo("idUsuario", idUsuario).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    ArrayList<PublicacionClass> lista= new ArrayList<>();
                    for(DocumentSnapshot document : task.getResult()){
                        Map<String, Object> publi = document.getData();
                        ParametrosClass p = ParametrosClass.descomprimir(document.getString("Parametros")).get(0);
                        PublicacionClass pc = new PublicacionClass((HashMap<String, String>) publi.get("Map comentarios"), (HashMap<String, String>) publi.get("Map likes"), p, (String)publi.get("idPublicacion"), (String)publi.get("idUsuario"));
                        lista.add(pc);
                    }
                    System.out.println("SOY EL DATABASE");
                    System.out.println(lista);
                    listener.setListaPublicacionTemp(lista);
                }
            }
        });
    }
    public void updatePublicacion(HashMap<String, String> coments, HashMap<String, String> likes, String parametros, String idPublicacion, String idUsuario){
        Map<String, Object> publicacion = new HashMap<>();
        publicacion.put("Map comentarios", coments);
        publicacion.put("Map likes", likes);
        publicacion.put("Parametros", parametros);
        publicacion.put("idPublicacion", idPublicacion);
        publicacion.put("idUsuario", idUsuario);
        Log.i(TAG, "updatePublicacion");
        db.collection("Publicaciones").document(idPublicacion).update(publicacion);
    }
    public void savePublicacion(HashMap<String, String> coments, HashMap<String, String> likes, String parametros, String idPublicacion, String idUsuario){
        DocumentReference dr=db.collection("Publicaciones").document();
        Map<String, Object> publicacion = new HashMap<>();
        publicacion.put("Map comentarios", coments);
        publicacion.put("Map likes", likes);
        publicacion.put("Parametros", parametros);
        publicacion.put("idPublicacion", dr.getId());
        publicacion.put("idUsuario", idUsuario);

        Log.i(TAG, "savePublicacion");


        db.collection("Publicaciones").document(dr.getId()).set(publicacion).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                   listener.notifyId(dr.getId());
                }
            }
        });
    }
    public void deletePublicacion(String idPublicacion){
        Log.i(TAG, "deletePublicacion");
        db.collection("Publicaciones").document(idPublicacion).delete();

    }

    public void saveUser (String nombre, String correo, String uid) {
        Map<String, String> user = new HashMap<>();
        user.put("Nombre", nombre);
        user.put("Correo", correo);
        user.put("UID", uid);
        user.put("Imagen", "https://firebasestorage.googleapis.com/v0/b/weadives.appspot.com/o/Imagenes_Perfil%2FprofillePicBase.png?alt=media&token=544d8b5c-11de-4acb-9bdd-54bb5f5297af"); //Cambiar
        user.put("Amigos", "");
        user.put("Solicitudes recibidas", "");
        user.put("Solicitudes enviadas", "");

        Log.i(TAG, "saveUser");
        db.collection("Users").document(uid).set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    getUser();
                }
            }
        });
    }

    public void updateDatos(HashMap<String, Object> user){
        db.collection("Users").document(user.get("UID").toString()).update(user);
    }

    public void unfollow(HashMap<String, Object> user) {
        db.collection("Users").document(mAuth.getCurrentUser().getUid()).update(user);
    }

    public void getAllUsers(){
        Log.d(TAG,"updateUsers");
        db.collection("Users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    ArrayList<UserClass> retrieved_users = new ArrayList<UserClass>() ;
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d(TAG, document.getId() + " => " + document.getData());
                        retrieved_users.add(new UserClass(document.getString("UID"), document.getString("Nombre"), document.getString("Correo"), document.getString("Imagen"), document.getString("Amigos"), document.getString("Solicitudes recibidas"), document.getString("Solicitudes enviadas")));
                    }
                    listener.setCollection(retrieved_users);
                    Log.i(TAG, "Usuarios añadidos al ViewModel");
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });
    }

    public void cambiarCorreo(String s){
        mAuth.getCurrentUser().updateEmail(s).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Log.d(TAG, "Correo cambiado correctamente.");
                } else {
                    Log.e(TAG, "Error al cambiar el correo");
                }
            }
        });
    }

    public void cambiarContraseña(String s){
        mAuth.getCurrentUser().updatePassword(s).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Log.d(TAG, "Contraseña cambiada correctamente.");
                } else {
                    Log.e(TAG, "Error al cambiar la contraseña");
                }
            }
        });
    }

    public void subirImagen(Uri file, String userId) {
        StorageReference userRef = storageRef.child("Imagenes_Perfil/" + userId);
        UploadTask uploadTask = userRef.putFile(file);

        uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw Objects.requireNonNull(task.getException());
                }
                return userRef.getDownloadUrl(); //RETORNO LA  URL DE DESCARGA DE LA FOTO
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if(task.isSuccessful()){
                    Uri uri = task.getResult();  //AQUI YA TENGO LA RUTA DE LA FOTO LISTA PARA INSERTRLA EN DATABASE
                    assert uri != null;
                }
            }
        });
    }

    public void singout(){
        mAuth.signOut();
        user = null;
        listener.setStatusLogIn(false);
    }

    public void deleteAccount() {
        db.collection("Users").document(mAuth.getCurrentUser().getUid()).delete();
        mAuth.getCurrentUser().delete();
        System.out.println("ELIMINAR USER: " + mAuth.getCurrentUser());
        mAuth.signOut();
        Log.d(TAG, "Cuenta borrada");
    }

    public void getLatLng(){
        db.collection("Data").document("Coord_base").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    ArrayList<Double> Lat = (ArrayList<Double>) document.get("Lat");
                    ArrayList<Double> Lon = (ArrayList<Double>) document.get("Lon");
                    listenerMapa.setLatLng(Lat, Lon);
                }
            }
        });
    }


    public void getStorageData (String fileName) {


        //System.out.println(mAuth.getCurrentUser().getEmail());

        StorageReference fileRef = storage.getReference()
                .child("Weather_data")
                .child("Coord_data")
                .child(fileName + ".csv");

        try {
            File localCSV = File.createTempFile("weatherData", ".csv");

            FileDownloadTask downloadTask = fileRef.getFile(localCSV);


            int i = 0;
            while(downloadTask.isInProgress()){
                System.out.println("WHILE: " + i++);
                System.out.println("Complete: " + downloadTask.isComplete());
                System.out.println("Successful: " + downloadTask.isSuccessful());
                System.out.println("Cancelled: " + downloadTask.isCanceled());
                System.out.println("Paused: " + downloadTask.isPaused());
            }

            System.out.println("WE OOOUT!!:");
            System.out.println("Complete: " + downloadTask.isComplete());
            System.out.println("Successful: " + downloadTask.isSuccessful());
            System.out.println("Cancelled: " + downloadTask.isCanceled());
            System.out.println("Paused: " + downloadTask.isPaused());
            System.out.println(downloadTask.getResult().getBytesTransferred());
            System.out.println(localCSV.getPath());
            listenerHorario.getCsvRef(localCSV.getAbsolutePath());
        } catch (IOException e) {
            System.out.println("TREMENDOO ERROOOR. " + e.getMessage());
        }
    }
}
