package com.example.weadives.PantallaInicio;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.weadives.PantallaMapa.PantallaMapa;
import com.example.weadives.PantallaPrincipal.PantallaPrincipal;
import com.example.weadives.R;

public class PantallaInicio extends AppCompatActivity {

    private ImageView Imagen_superior, btn_en, btn_es;
    private Bitmap results, maskbitmap;
    private Button btn_invisible;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.pantalla_inicio);
        LinearLayout layout =findViewById(R.id.LinearMainLayout);
        Imagen_superior = findViewById(R.id.img_inicio);
        btn_invisible = findViewById(R.id.btn_invisible);
        Bitmap finalMasking = MaskingProcess();
        btn_en = findViewById(R.id.btn_en);
        btn_es = findViewById(R.id.btn_es);

        btn_en.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                guardarPreferencias("en");
                Toast toast = Toast.makeText(getApplicationContext(), "Language changed into English", Toast.LENGTH_SHORT);
                toast.show();
            }
        });

        btn_es.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                guardarPreferencias("es");
                Toast toast = Toast.makeText(getApplicationContext(), "Idioma cambiado a Castellano", Toast.LENGTH_SHORT);
                toast.show();
            }
        });

        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent testIntent = new Intent(getApplicationContext(), PantallaPrincipal.class);
                    //testIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(testIntent);
                    //finish();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast toast2 = Toast.makeText(getApplicationContext(), "Error: "+e.getMessage(), Toast.LENGTH_SHORT);
                    toast2.show();
                }
            }
        });

        btn_invisible.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent testIntent = new Intent(getApplicationContext(), PantallaMapa.class);
                    startActivity(testIntent);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast toast2 = Toast.makeText(getApplicationContext(), "Error: "+e.getMessage(), Toast.LENGTH_SHORT);
                    toast2.show();
                }
            }
        });
    }

    private void guardarPreferencias(String string) {
        SharedPreferences preferencias = getSharedPreferences("idioma",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=preferencias.edit();
        editor.putString("idioma",string);
        editor.commit();
    }

    private String cargarPreferencias() {
        SharedPreferences preferencias = getSharedPreferences("idioma",Context.MODE_PRIVATE);
        return preferencias.getString("idioma","en");
    }

    private Bitmap MaskingProcess() {
        try{
            Bitmap test, mask1;
            test= BitmapFactory.decodeResource(getResources(),R.drawable.mapa4);
            mask1 = BitmapFactory.decodeResource(getResources(),R.drawable.mask1);
            if (test != null){

                int iv_width = test.getWidth();
                int iv_height = test.getHeight();
                results = Bitmap.createBitmap(iv_width,iv_height,Bitmap.Config.ARGB_8888);
                maskbitmap = Bitmap.createScaledBitmap(mask1,iv_width,iv_height,true);

                Canvas canvas = new Canvas(results);
                Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
                paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));

                canvas.drawBitmap(test,0,0,null);
                canvas.drawBitmap(maskbitmap,0,0,paint);

                paint.setXfermode(null);
                paint.setStyle(Paint.Style.STROKE);
            }
            } catch (OutOfMemoryError outOfMemoryError) {
            outOfMemoryError.printStackTrace();
        }
        Imagen_superior.setImageBitmap(results);
        return results;
    }




}