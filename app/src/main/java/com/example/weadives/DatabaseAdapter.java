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
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.opencsv.CSVReader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
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
    public static mapaInterface listenerMapa;
    public static intentInterface listenerIntent;
    public static preferenciasInterface listenerPreferencias;
    public static DatabaseAdapter databaseAdapter;

    private File localFile;

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

    public DatabaseAdapter(mapaInterface mapaInterface){
        this.listenerMapa = mapaInterface;
        databaseAdapter = this;
        FirebaseFirestore.setLoggingEnabled(true);
    }

    public DatabaseAdapter(preferenciasInterface preferenciasInterface){
        this.listenerPreferencias = preferenciasInterface;
        databaseAdapter = this;
        FirebaseFirestore.setLoggingEnabled(true);
    }


    public interface vmInterface{
        void setCollection(ArrayList<UserClass> listaUsuarios);
        void setStatusLogIn(boolean status);
        void setUserID(String id);
        void setUser(UserClass u);
        void setToast(String s);
    }

    public interface mapaInterface{
        void setLatLng(ArrayList<Double> lat, ArrayList<Double> lon);
    }

    public interface preferenciasInterface {
        void setNotificationId(String id);
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
        listener.setStatusLogIn(false);
    }

    public void deleteAccount(){
        db.collection("Users").document(mAuth.getCurrentUser().getUid()).delete();
        mAuth.getCurrentUser().delete();
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

    public void createCoordsNotification(String coords,Map<String, Object> data){
        db.collection("Notificaciones")
                .add(data)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
                        listenerPreferencias.setNotificationId(documentReference.getId());
                        System.out.println("Se crea");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });

        DocumentReference docRef = db.collection("Datos").document(data.get("coords").toString());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                    } else {
                        Log.d(TAG, "No such document");
                        System.out.println("No existe");
                        //TODO Aqui crear el doc si no existe
                        StorageReference coordRef = storage.getReference().child("/Weather_data/Coord_data/" + coords + ".csv");
                        localFile = null;
                        try {
                            localFile = File.createTempFile("coords", ".csv");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        coordRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                // Local temp file has been created
                                System.out.println("Tenemos file");
                                createDocWithData(coords, localFile);
                                System.out.println(localFile.toString());
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                // Handle any errors
                            }
                        });
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    private void createDocWithData(String coord, File file){
        System.out.println("tenemos file");
        Map<String, Object> coordData = new HashMap<>();
        /*Map<String, String> directions = new HashMap<>();
        directions.put("S", "SUD");
        directions.put("SE", "SUDESTE");
        directions.put("N", "NORTE");
        directions.put("NE", "NORDESTE");
        directions.put("SO", "SUDOESTE");
        directions.put("E", "ESTE");
        directions.put("O", "OESTE");
        directions.put("NO", "NORDOESTE");*/
        List<String[]> data;
        InputStream in = null;
        try {
            in = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        InputStreamReader streamReader = new InputStreamReader(in, StandardCharsets.UTF_8);
        try (CSVReader reader = new CSVReader(streamReader)) {
            System.out.println("Leemos");
            data = reader.readAll();
            System.out.println("1");
            ArrayList<String> _0 = new ArrayList<>(Arrays.asList(data.get(4)));
            _0.remove(0);
            coordData.put("preasure", _0);
            System.out.println("2");
            ArrayList<String> _1 = new ArrayList<>(Arrays.asList(data.get(5)));
            _1.remove(0);
            coordData.put("temperatura", _1);
            System.out.println("3");
            ArrayList<String> _2 = new ArrayList<>(Arrays.asList(data.get(2)));
            _2.remove(0);
            coordData.put("waveDirection", _2);
            System.out.println("4");
            ArrayList<String> _3 = new ArrayList<>(Arrays.asList(data.get(1)));
            _3.remove(0);
            coordData.put("waveHeight", _3);
            System.out.println("5");
            ArrayList<String> _4 = new ArrayList<>(Arrays.asList(data.get(6)));
            _4.remove(0);
            coordData.put("windDirection", _4);
            System.out.println("6");
            ArrayList<String> _5 = new ArrayList<>(Arrays.asList(data.get(3)));
            _5.remove(0);
            coordData.put("wavePeriod", _5);

            ArrayList<String> _6 = new ArrayList<>(Arrays.asList(data.get(7)));
            _6.remove(0);
            coordData.put("wind", _6);


            /*
            coordData.put("preasure", Arrays.asList(data.get(4)).remove(0));
            coordData.put("temperatura", Arrays.asList(data.get(5)).remove(0));
            coordData.put("waveDirection", Arrays.asList(data.get(2)).remove(0));
            coordData.put("WaveHeight",Arrays.asList(data.get(1)).remove(0));
            coordData.put("windDirection", Arrays.asList(data.get(6)).remove(0));
            coordData.put("wavePeriod", Arrays.asList(data.get(3)).remove(0));
            coordData.put("wind", Arrays.asList(data.get(7)).remove(0));*/
            System.out.println("Hemos cogido datos");
            db.collection("Datos")
                    .document(coord)
                    .set(coordData)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "DocumentSnapshot successfully written!");
                            System.out.println("funciona");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Error writing document", e);
                            System.out.println("No funca");
                        }
                    });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
