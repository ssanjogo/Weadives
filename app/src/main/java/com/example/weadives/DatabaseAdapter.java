package com.example.weadives;

import android.app.Activity;
import android.content.Context;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
    private boolean funciona = false, logIn = false;



    public boolean addUser(String nombre, String correo, String contraseña, String iduser) {
        mAuth.createUserWithEmailAndPassword(correo, contraseña).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                System.out.println("task" + task.isSuccessful());
                if (task.isSuccessful()) {
                    Map<String, String> user = new HashMap<>();
                    user.put("Nombre:", nombre);
                    user.put("Correo:", correo);
                    user.put("User ID:", iduser);

                    db.collection("Users").document(iduser).set(user);
                    funciona = true;

                }
            }
        });
        return funciona;
    }

    public void solicitudAmistad(){

    }

    public void eliminarCuenta(){
        mAuth.getCurrentUser().delete();
    }

    public boolean logIn(String correo, String contraseña){
        System.out.println("pasa");
        mAuth.signInWithEmailAndPassword(correo, contraseña).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    logIn = true;
                    System.out.println("pasa, es true.");
                } else {
                    logIn = false;
                    System.out.println("pasa, es false.");
                }
            }
        });
        return logIn;
    }


}
