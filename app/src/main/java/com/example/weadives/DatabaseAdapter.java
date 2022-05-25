package com.example.weadives;

import android.app.Activity;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.weadives.AreaUsuario.UserClass;
import com.example.weadives.PantallaPerfilAmigo.PublicacionClass;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DatabaseAdapter extends Activity {

    public static final String TAG = "DatabaseAdapter";

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final FirebaseStorage storage = FirebaseStorage.getInstance();
    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseAuth.IdTokenListener mAuthIDTokenListener;
    private FirebaseUser user;

    public static vmInterface listener;
    public static intentInterface listenerIntent;
    public static DatabaseAdapter databaseAdapter;

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

    public interface intentInterface {
        void intent();
    }

    public void register (String nombre, String correo, String contraseña){
        mAuth.createUserWithEmailAndPassword(correo, contraseña).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    listener.setUserID(task.getResult().getUser().getUid());
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
        user.put("Imagen", "https://www.pngmart.com/files/21/Account-User-PNG-Photo.png"); //Cambiar
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

    public void singout(){
        mAuth.signOut();
        user = null;
        listener.setUser(null);
        listener.setStatusLogIn(false);
    }

    public void deleteAccount(){
        db.collection("Users").document(mAuth.getCurrentUser().getUid()).delete();
        mAuth.getCurrentUser().delete();
        Log.d(TAG, "Cuenta borrada");
    }
}
