package com.example.weadives;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.weadives.AreaUsuario.AreaUsuario;
import com.example.weadives.AreaUsuario.UserClass;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.User;
import com.google.firebase.storage.FirebaseStorage;

import java.util.HashMap;
import java.util.List;
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

    public boolean addUser(String nombre, String correo, String contraseña) {
        mAuth.createUserWithEmailAndPassword(correo, contraseña).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    uid = mAuth.getCurrentUser().getUid();
                    UserClass user = new UserClass(uid, nombre, correo, "", "", "", "");
                    db.collection("Users").document(uid).set(user);
                    funciona = true;
                }
            }
        });
        return funciona;
    }

    public UserClass getUser(String uid){
        final UserClass[] user = new UserClass[1];
        System.out.println("PASSSSSSSSSSSSSSSSSSSSSSSSSSSAAAAAAAAAAAAAAAAAAAA");
        db.collection("Users").document(uid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    user[0] = new UserClass(document.getString("UID"), document.getString("Nombre"), document.getString("Correo"), document.getString("Imagen"), document.getString("Amigos"), document.getString("Solicitudes recibidas"), document.getString("Solicitudes enviadas"));

                    System.out.println("UUUUUUUUUUUUUUUUUUUUUUUUUUUUUSUARIO" + user[0]);
                    try {
                        task.wait(150);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    System.out.println("HOLA??????????????????? WTF");
                }
            }
        });
        System.out.println("POR AQUI TAMBIEN A VER QUE TAL");
        return user[0];
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
                    }
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

    public void enviarSolicitudAmistad(String idAmigo){
        String uid = mAuth.getCurrentUser().getUid();
        UserClass user = getUser(uid);
        System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA" + user);
        String SolE = user.getStringSolicitudesEnviadas();
        SolE += idAmigo;
        user.setStringSolicitudesEnviadas(SolE);
        db.collection("Users").document(uid).set(user);


        UserClass user2 = getUser(idAmigo);

        String SolR = user.getStringSolicitudesRecibidas();
        SolR += uid;
        user.setStringSolicitudesRecibidas(SolR);
        db.collection("Users").document(uid).set(user2);

        System.out.println("A ver Si cambia");

    }

}
