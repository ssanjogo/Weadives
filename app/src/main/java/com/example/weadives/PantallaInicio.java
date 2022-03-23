package com.example.weadives;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class PantallaInicio extends AppCompatActivity {

    private ImageView Imagen_superior,Imagen_inferior;
    private Bitmap results, maskbitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pantalla_inicio);
        LinearLayout layout = findViewById(R.id.LinearMainLayout);
        Imagen_superior = findViewById(R.id.imageview1);
        Bitmap finalMasking = MaskingProcess();

        Imagen_superior.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent testIntent = new Intent(getApplicationContext(), PantallaPrincipal.class);
                startActivity(testIntent);
            }
        });

        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent testIntent = new Intent(getApplicationContext(), PantallaPrincipal.class);
                startActivity(testIntent);
            }
        });


    }

    private Bitmap MaskingProcess() {
        try{
            Bitmap test, mask1;
            test= BitmapFactory.decodeResource(getResources(),R.drawable.pubg);
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