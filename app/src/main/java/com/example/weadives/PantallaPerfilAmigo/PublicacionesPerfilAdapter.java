package com.example.weadives.PantallaPerfilAmigo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weadives.R;

import java.util.List;
//Si no se pone el exetend correctamente, el onbind peta
public class PublicacionesPerfilAdapter extends RecyclerView.Adapter<PublicacionesPerfilAdapter.PublicacionesPerfilViewHolder> {
    List<PublicacionClass> publicacionClassList;
    Context context;


    public PublicacionesPerfilAdapter(List<PublicacionClass> publicacionClassList, Context context) {
        this.publicacionClassList = publicacionClassList;
        this.context = context;
    }


    @NonNull
    @Override
    public PublicacionesPerfilAdapter.PublicacionesPerfilViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.one_publicacion_list,parent,false);
        PublicacionesPerfilViewHolder holder = new PublicacionesPerfilAdapter.PublicacionesPerfilViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull PublicacionesPerfilViewHolder holder, int position) {
        holder.txt_viento.setText("Wind :");
        holder.txt_activityName.setText("Activity Name :");
        holder.txt_vientoOutput.setText(Float.toString(publicacionClassList.get(position).getParametros().getViento()));
        holder.txt_activityNameOutput.setText(publicacionClassList.get(position).getParametros().getNombreActividad());
        holder.txt_userId.setText("ID");
        holder.txt_userIdOutput.setText(Integer.toString(publicacionClassList.get(position).getParametros().getUserID()));
        System.out.println(Integer.toString(publicacionClassList.get(position).getParametros().getUserID()));
        holder.txt_numlikes.setText(Integer.toString(publicacionClassList.get(position).getNumLikes()));
        holder.txt_numlikes2.setText(Integer.toString(publicacionClassList.get(position).getNumDislikes()));
        holder.txt_numlikes3.setText(Integer.toString(publicacionClassList.get(position).getNumComments()));
        holder.btn_comments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast toast = Toast.makeText(context, "Comentarios", Toast.LENGTH_SHORT);
                toast.show();

            }
        });
        holder.btn_likes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast toast = Toast.makeText(context, "Like", Toast.LENGTH_SHORT);
                toast.show();
            }
        });
        holder.btn_dislikes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast toast = Toast.makeText(context, "Dislike", Toast.LENGTH_SHORT);
                toast.show();

            }
        });
    }


    @Override
    public int getItemCount() {
       return publicacionClassList.size();
    }


    public class PublicacionesPerfilViewHolder extends RecyclerView.ViewHolder {
        TextView txt_activityName;
        TextView txt_activityNameOutput;
        TextView txt_userId;
        TextView txt_userIdOutput;
        TextView txt_viento;
        TextView txt_vientoOutput;
        TextView txt_numlikes;
        TextView txt_numlikes2;
        TextView txt_numlikes3;
        ImageView btn_comments;
        ImageView btn_likes;
        ImageView btn_dislikes;

        public PublicacionesPerfilViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_activityName=itemView.findViewById(R.id.txt_activityName1);
            txt_activityNameOutput=itemView.findViewById(R.id.txt_activityNameOutput1);
            txt_userId=itemView.findViewById(R.id.txt_userId1);
            txt_userIdOutput=itemView.findViewById(R.id.txt_userIdOutput1);
            txt_viento=itemView.findViewById(R.id.txt_viento1);
            txt_vientoOutput=itemView.findViewById(R.id.txt_vientoOutput1);
            txt_numlikes=itemView.findViewById(R.id.txt_numLikes);
            txt_numlikes2=itemView.findViewById(R.id.txt_numLikes2);
            txt_numlikes3=itemView.findViewById(R.id.txt_numLikes3);
            btn_comments=itemView.findViewById(R.id.imageView4);
            btn_likes=itemView.findViewById(R.id.imageView2);
            btn_dislikes=itemView.findViewById(R.id.imageView3);
        }
    }
}
