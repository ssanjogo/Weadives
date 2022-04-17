package com.example.weadives;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.weadives.AreaUsuario.AreaUsuario;
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
import java.util.UUID;

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
    private String n, nombre, uid;

    static DatabaseAdapter databaseAdapterInstance;

    public static DatabaseAdapter getInstance(){
        if (databaseAdapterInstance == null){
            databaseAdapterInstance = new DatabaseAdapter();
        }
        return databaseAdapterInstance;
    }

    public void addUser(String nombre, String correo, String contraseña) {
        mAuth.createUserWithEmailAndPassword(correo, contraseña).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    uid = mAuth.getCurrentUser().getUid();
                    Map<String, String> user = new HashMap<>();
                    user.put("UID", uid);
                    user.put("Nombre", nombre);
                    user.put("Correo", correo);
                    user.put("Imagen", "");
                    user.put("Amigos", "");
                    user.put("Solicitudes recibidas", "");
                    user.put("Solicitudes enviadas", "");

                    db.collection("Users").document(uid).set(user);
                    Intent areaUsuario = new Intent(getApplicationContext(), AreaUsuario.class);
                    areaUsuario.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(areaUsuario);
                    finish();
                }
            }
        });
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

    public void setName(TextView textView){
        String uid = mAuth.getCurrentUser().getUid();
        db.collection("Users").document(uid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
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

    public void setCorreo(TextView textView){
        String correo = mAuth.getCurrentUser().getEmail();
        textView.setText(correo);
    }

    public void setLogInStatus (boolean b){
        this.statusLogIn = b;
    }

    public boolean getLogInStatus(){
        return statusLogIn;
    }

    public void singout(){
        mAuth.signOut();
    }

    public void eliminarCuenta(){
        mAuth.getCurrentUser().delete();
    }

    public boolean currentUser(){
        return (mAuth.getCurrentUser() == null);
    }

}
