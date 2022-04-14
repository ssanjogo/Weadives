package com.example.weadives.AreaUsuario;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weadives.PantallaInicio.PantallaInicio;
import com.example.weadives.PantallaMiPerfil.PantallaMiPerfil;
import com.example.weadives.R;

import java.util.ArrayList;
import java.util.List;

public class AreaUsuario extends AppCompatActivity {

    private ImageView img_perfil, btn_home4, btn_añadir;
    private TextView txt_nombrePerfil, txt_codigo;
    private EditText etT_buscarPorCodigo;
    private RecyclerView rv_llistaUsuarios;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<UserClass> userList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.area_usuario);
        txt_nombrePerfil = findViewById(R.id.txt_nombrePerfil);
        etT_buscarPorCodigo = findViewById(R.id.etT_buscarPorCodigo);
        btn_home4 = findViewById(R.id.btn_home4);
        btn_añadir = findViewById(R.id.btn_añadir);
        img_perfil = findViewById(R.id.img_perfil);
        rv_llistaUsuarios = findViewById(R.id.rv_llistaUsuarios);
        ConstraintLayout constraintLayout = findViewById(R.id.constraintLayout);

        Intent intent = getIntent();

        dbA = DatabaseAdapter.getInstance();
        userList = fillUserList();

        RecyclerView recyclerView = findViewById(R.id.rv_llistaUsuarios);
        //mejorar performance
        recyclerView.hasFixedSize();
        //lineal layout
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        //especificar adapter
        mAdapter= new UserListAdapter(userList,AreaUsuario.this);
        recyclerView.setAdapter(mAdapter);

        dbA.setName(txt_nombrePerfil);

        btn_home4.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if(!dbA.getLogInStatus()){
                    dbA.singout();
                }
                Intent pantallaInicio = new Intent(getApplicationContext(), PantallaInicio.class);
                startActivity(pantallaInicio);
            }
        });
        constraintLayout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent pantallaMiperfil = new Intent(getApplicationContext(), PantallaMiPerfil.class);
                startActivity(pantallaMiperfil);
            }
        });

    }

    private List<UserClass> fillUserList() {//String id, String username, String correo, String urlImg)
        List<UserClass> userList = new ArrayList<>();
        UserClass Jose = new UserClass("0001","Jose","Jose@gmail.com","https://upload.wikimedia.org/wikipedia/commons/a/ab/Abraham_Lincoln_O-77_matte_collodion_print.jpg");
        Jose.sentSolicitud();
        userList.add(Jose);
        userList.add(new UserClass("0002","Xx_Pro_xX","Pro@gmail.com","https://static.wikia.nocookie.net/youtubepedia/images/3/33/1_wVf0oHfP9iaU61YodjtAqQ.jpeg/revision/latest?cb=20200823233708&path-prefix=es"));
        userList.add(new UserClass("0003","Kaladin","BT@gmail.com","https://i.pinimg.com/736x/1e/84/b5/1e84b5b8fe380ca6ee49e2e50db166a2.jpg"));
        userList.add(new UserClass("0004","Mikol","Mk@gmail.com","https://static.wikia.nocookie.net/ficcion-sin-limites/images/f/f8/SmashSteve.png/revision/latest?cb=20210104203302&path-prefix=es"));
        userList.add(new UserClass("0005","NoPNG","NP@gmail.com"));
        return userList;
    }

}
