package com.example.weadives;

import android.app.Activity;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.weadives.AreaUsuario.UserClass;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import org.w3c.dom.Document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DatabaseAdapter extends Activity {

    public static final String TAG = "DatabaseAdapter";

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final FirebaseStorage storage = FirebaseStorage.getInstance();
    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser user;

    public static vmInterface listener;
    public static DatabaseAdapter databaseAdapter;

    public DatabaseAdapter(vmInterface listener){
        this.listener = listener;
        databaseAdapter = this;
        FirebaseFirestore.setLoggingEnabled(true);
    }

    public interface vmInterface{
        void setCollection(ArrayList<UserClass> listaUsuarios);
        void setUserId(String id);
        void setStatusLogIn(boolean status);
    }

    public void getCollection(){
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

                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });
    }

    public void register (String nombre, String correo, String contraseña){
        mAuth.createUserWithEmailAndPassword(correo, contraseña).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    listener.setUserId(mAuth.getCurrentUser().getUid());
                    saveUser(nombre, correo, mAuth.getCurrentUser().getUid());
                }
            }
        });
    }

    public void logIn(String correo, String contraseña){
        mAuth.signInWithEmailAndPassword(correo, contraseña).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    listener.setUserId(mAuth.getCurrentUser().getUid());
                } else {
                    Log.d(TAG, "Error en el log in");
                }
            }
        });
    }

    public void saveUser (String nombre, String correo, String uid) {
        Map<String, String> user = new HashMap<>();
        user.put("Nombre", nombre);
        user.put("Correo", correo);
        user.put("UID", uid);
        user.put("Imagen", "");
        user.put("Amigos", "");
        user.put("Solicitudes recibidas", "");
        user.put("Solicitudes enviadas", "");

        Log.d(TAG, "saveUser");
        db.collection("Users").add(user).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(TAG, "Error adding document", e);
            }
        });
    }

    public HashMap<String, String> getUsers () {
        db.collection("Users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d(TAG, document.getId() + " => " + document.getData());
                    }
                } else {
                    Log.w(TAG, "Error getting documents.", task.getException());
                }
            }
        });
        return new HashMap<>();
    }

    public void cambiarCorreo(String nuevoDato, UserClass user){
        db.collection("Users").document(mAuth.getCurrentUser().getUid()).update((Map<String, Object>) user);
    }

    public void cambiarUsername(String nuevoDato, HashMap<String, Object> user){
        db.collection("Users").document(mAuth.getCurrentUser().getUid()).update(user);
    }

    public void singout(){
        mAuth.signOut();
        listener.setStatusLogIn(false);
        listener.setUserId(null);
    }

    public void deleteAccount(){
        db.collection("Users").document(mAuth.getCurrentUser().getUid()).delete();
        mAuth.getCurrentUser().delete();
        Log.d(TAG, "Cuenta borrada");
    }

}
