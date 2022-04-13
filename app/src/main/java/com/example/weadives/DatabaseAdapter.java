package com.example.weadives;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import java.util.HashMap;
import java.util.Map;

public class DatabaseAdapter extends Activity {

    public static final String TAG = "DatabaseAdapter";

    //Firebase
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private final FirebaseStorage storage = FirebaseStorage.getInstance();
    private FirebaseUser user;

    //Atributos
    private DatabaseAdapter dbA;
    private boolean funciona = false, logIn = false, statusLogIn = false;
    private String n, nombre;

    static DatabaseAdapter databaseAdapterInstance;

    public static DatabaseAdapter getInstance(){
        if (databaseAdapterInstance == null){
            databaseAdapterInstance = new DatabaseAdapter();
        }
        return databaseAdapterInstance;
    }

    public boolean addUser(String nombre, String correo, String contraseña, String iduser) {
        mAuth.createUserWithEmailAndPassword(correo, contraseña).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Map<String, String> user = new HashMap<>();
                    user.put("Correo", correo);
                    user.put("Nombre", nombre);
                    user.put("User ID", iduser);

                    db.collection("Users").document(correo).set(user);
                    funciona = true;
                }
            }
        });
        return funciona;
    }

    public boolean logIn(String correo, String contraseña){
        mAuth.signInWithEmailAndPassword(correo, contraseña).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    logIn = true;
                } else {
                    logIn = false;
                }
            }
        });
        return logIn;
    }

    public boolean currentUser(){
        return (mAuth.getCurrentUser() == null);
    }

    public void singout(){
        mAuth.signOut();
    }

    public void setIDActual(String numeroActual){
        db.collection("IdUserActual").document("numeroActual").set(numeroActual);
    }

    public int getIDActual(){
        db.collection("IDs").document("idActual").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        n = document.getData().toString();
                    }
                }
            }
        });
        return Integer.parseInt(n + 1);
    }

    public void setIDEliminados(String idEliminado){
        db.collection("IDs").document().set(idEliminado);
    }

    public void solicitudAmistad(){

    }

    public void eliminarCuenta(){
        mAuth.getCurrentUser().delete();
    }

    public void setName(TextView textView){
        String correo = mAuth.getCurrentUser().getEmail();
        db.collection("Users").document(correo).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        final String nombre = document.getString("Nombre");
                        textView.setText(nombre);
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    public void setId(TextView textView){
        String correo = mAuth.getCurrentUser().getEmail();
        db.collection("Users").document(correo).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        final String id = document.getString("User ID");
                        textView.setText("#" + id);
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }



    public void setLogInStatus (boolean b){
        this.statusLogIn = b;
    }

    public boolean getLogInStatus(){
        return statusLogIn;
    }

}
