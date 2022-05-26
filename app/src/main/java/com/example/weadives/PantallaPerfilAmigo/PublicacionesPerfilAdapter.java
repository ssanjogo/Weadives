package com.example.weadives.PantallaPerfilAmigo;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weadives.AreaUsuario.UserClass;
import com.example.weadives.ParametrosClass;
import com.example.weadives.R;
import com.example.weadives.SingletonIdioma;
import com.example.weadives.ViewModel;
import com.example.weadives.ViewModelParametros;

import java.util.ArrayList;
import java.util.List;
//Si no se pone el exetend correctamente, el onbind peta
public class PublicacionesPerfilAdapter extends RecyclerView.Adapter<PublicacionesPerfilAdapter.PublicacionesPerfilViewHolder> {
    List<PublicacionClass> publicacionClassList;
    Context context;
    LayoutInflater Linflater;

    public PublicacionesPerfilAdapter(List<PublicacionClass> publicacionClassList, Context context) {
        this.publicacionClassList = publicacionClassList;
        this.context = context;
    }
    public void updateData2(final ArrayList<PublicacionClass> stationArrivalPOJO ) {
        publicacionClassList= new ArrayList<>();
        publicacionClassList.addAll(stationArrivalPOJO);
    }


    @NonNull
    @Override
    public PublicacionesPerfilAdapter.PublicacionesPerfilViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.one_publicacion_list,parent,false);
        Linflater=LayoutInflater.from(parent.getContext());
        PublicacionesPerfilViewHolder holder = new PublicacionesPerfilAdapter.PublicacionesPerfilViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull PublicacionesPerfilViewHolder holder, @SuppressLint("RecyclerView") int position) {
        SingletonIdioma s= SingletonIdioma.getInstance();
        Resources resources=s.getResources();
        holder.txt_activityName.setText(resources.getString(R.string.NombreActividad)+" : ");
        holder.txt_viento.setText(resources.getString(R.string.viento)+" : ");
        holder.txt_presion.setText(resources.getString(R.string.presion)+" : ");
        holder.txt_temperatura.setText(resources.getString(R.string.temperatura)+" :");
        holder.txt_dirviento.setText(resources.getString(R.string.dirViento)+" : ");
        holder.txt_alturaOla.setText(resources.getString(R.string.alturaOla)+" : ");
        holder.txt_periodoOla.setText(resources.getString(R.string.periodoOla)+" : ");
        holder.txt_dirOla.setText(resources.getString(R.string.dirOlas)+" : ");



        holder.txt_activityNameOutput.setText(publicacionClassList.get(position).getParametros().getNombreActividad());
        holder.txt_vientoOutput.setText(Float.toString(publicacionClassList.get(position).getParametros().getViento())+" kt");
        holder.txt_presionOutput.setText(Float.toString(publicacionClassList.get(position).getParametros().getPresion())+" P");
        holder.txt_temperaturaOutput.setText(Float.toString(publicacionClassList.get(position).getParametros().getTemperatura())+" Cº");
        holder.txt_dirvientoOutput.setText((publicacionClassList.get(position).getParametros().getDirectionViento().toString()));
        holder.txt_alturaOlaOutput.setText(Float.toString(publicacionClassList.get(position).getParametros().getAlturaOla())+" m");
        holder.txt_periodoOutput.setText(Float.toString(publicacionClassList.get(position).getParametros().getPeriodoOla())+" s");

        holder.txt_dirOlaOutput.setText((publicacionClassList.get(position).getParametros().getDirectionOlas().toString()));

        loadSocial(holder, position);


        holder.btn_comments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast toast = Toast.makeText(context, "Comentarios", Toast.LENGTH_SHORT);
                toast.show();
                List commentList=publicacionClassList.get(position).getCommentInList();
                holder.createNewCommentListDialog(holder,position);
                loadSocial(holder, position);

            }
        });
        holder.btn_likes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(publicacionClassList.get(position).like(ViewModel.getInstance().getUserId(),1)){
                    Toast toast = Toast.makeText(context, "Like", Toast.LENGTH_SHORT);
                    toast.show();
                    loadSocial(holder, position);
                    updatePublications(publicacionClassList.get(position));
                    holder.btn_likes.setImageDrawable(resources.getDrawable(R.drawable.btn_like_color));
                }else{
                    Toast toast = Toast.makeText(context, resources.getString(R.string.AlreadyLike), Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });
        holder.btn_dislikes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(publicacionClassList.get(position).like(ViewModel.getInstance().getUserId(),0)){
                    Toast toast = Toast.makeText(context, "Dislike", Toast.LENGTH_SHORT);
                    toast.show();
                    loadSocial(holder, position);
                    updatePublications(publicacionClassList.get(position));
                    holder.btn_dislikes.setImageDrawable(resources.getDrawable(R.drawable.btn_dislike_color));
                }else{
                    Toast toast = Toast.makeText(context, (resources.getString(R.string.AlreadyLike)), Toast.LENGTH_SHORT);
                    toast.show();
                }


            }
        });
    }

    private void loadSocial(PublicacionesPerfilViewHolder holder, @SuppressLint("RecyclerView") int position) {
        SingletonIdioma s= SingletonIdioma.getInstance();
        Resources resources=s.getResources();
        if(!publicacionClassList.get(position).like(ViewModel.getInstance().getUserId(),1)){
            holder.btn_likes.setImageDrawable(resources.getDrawable(R.drawable.btn_like_color));
        } if(publicacionClassList.get(position).like(ViewModel.getInstance().getUserId(),0)){
            holder.btn_dislikes.setImageDrawable(resources.getDrawable(R.drawable.btn_dislike_color));
        }
        holder.txt_numlikes.setText(Integer.toString(publicacionClassList.get(position).getNumLikes()));
        holder.txt_numlikes2.setText(Integer.toString(publicacionClassList.get(position).getNumDislikes()));
        holder.txt_numlikes3.setText(Integer.toString(publicacionClassList.get(position).getNumComments()));
    }


    @Override
    public int getItemCount() {
       return publicacionClassList.size();
    }


    public class PublicacionesPerfilViewHolder extends RecyclerView.ViewHolder {
        TextView txt_activityName;
        TextView txt_activityNameOutput;


        TextView txt_presion;
        TextView txt_presionOutput;
        TextView txt_temperatura;
        TextView txt_temperaturaOutput;
        TextView txt_viento;
        TextView txt_vientoOutput;
        TextView txt_dirviento;
        TextView txt_dirvientoOutput;
        TextView txt_alturaOla;
        TextView txt_alturaOlaOutput;
        TextView txt_periodoOla;
        TextView txt_periodoOutput;
        TextView txt_dirOla;
        TextView txt_dirOlaOutput;




        TextView txt_numlikes;
        TextView txt_numlikes2;
        TextView txt_numlikes3;
        ImageView btn_comments;
        ImageView btn_likes;
        ImageView btn_dislikes;


        AlertDialog.Builder dialogBuilder;
        AlertDialog dialog;
        Button btn_test;
        RecyclerView rv_commentList;
        RecyclerView.Adapter mAdapter;
        RecyclerView.LayoutManager layoutManager;


        AlertDialog.Builder dialogBuilder2;
        AlertDialog dialog2;
        Button btn_addcomment;
        EditText comment;


        public PublicacionesPerfilViewHolder(@NonNull View itemView) {
            super(itemView);

            txt_activityName=itemView.findViewById(R.id.txt_activityName1);
            txt_activityNameOutput=itemView.findViewById(R.id.txt_activityNameOutput1);
            txt_viento=itemView.findViewById(R.id.txt_viento1);
            txt_vientoOutput=itemView.findViewById(R.id.txt_vientoOutput1);
            txt_presion=itemView.findViewById(R.id.txt_pression1);
            txt_presionOutput=itemView.findViewById(R.id.txt_pressionOutput1);
            txt_temperatura=itemView.findViewById(R.id.txt_temperatura1);
            txt_temperaturaOutput=itemView.findViewById(R.id.txt_temperaturaOutput1);
            txt_dirviento=itemView.findViewById(R.id.txt_dirViento1);
            txt_dirvientoOutput=itemView.findViewById(R.id.txt_dirVientoOutput1);
            txt_alturaOla=itemView.findViewById(R.id.txt_alturaOla1);
            txt_alturaOlaOutput=itemView.findViewById(R.id.txt_alturaOlaOutput1);
            txt_periodoOla=itemView.findViewById(R.id.txt_periodoOla1);
            txt_periodoOutput=itemView.findViewById(R.id.txt_periodoOlaOutput1);
            txt_dirOla=itemView.findViewById(R.id.txt_dirOla1);
            txt_dirOlaOutput=itemView.findViewById(R.id.txt_dirOlaOutput1);




            txt_numlikes=itemView.findViewById(R.id.txt_numLikes);
            txt_numlikes2=itemView.findViewById(R.id.txt_numLikes2);
            txt_numlikes3=itemView.findViewById(R.id.txt_numLikes3);
            btn_comments=itemView.findViewById(R.id.imageView4);
            btn_likes=itemView.findViewById(R.id.imageView2);
            btn_dislikes=itemView.findViewById(R.id.imageView3);


        }

        public void createNewCommentListDialog(PublicacionesPerfilViewHolder holder, @SuppressLint("RecyclerView") int position){

            //Creamos el dialog
            dialogBuilder = new AlertDialog.Builder(context);
            View popupView=Linflater.inflate(R.layout.popup_listacomentarios,null);
//___

            SingletonIdioma s= SingletonIdioma.getInstance();
            Resources resources=s.getResources();


            //Definicion de los items
            btn_test=popupView.findViewById(R.id.btn_test);
            btn_test.setText(resources.getString(R.string.añadircomment));

            rv_commentList=popupView.findViewById(R.id.rv_commentList);
            //mejorar performance
            rv_commentList.hasFixedSize();
            //lineal layout
            layoutManager = new LinearLayoutManager(context);
            rv_commentList.setLayoutManager(layoutManager);
            //especificar adapter
            mAdapter= new CommentListAdapter(publicacionClassList.get(position).getCommentInList(), context);
            rv_commentList.setAdapter(mAdapter);


            //Hacemos aparecer la ventana
            dialogBuilder.setView(popupView);
            dialog=dialogBuilder.create();
            dialog.setOnCancelListener(new DialogInterface.OnCancelListener(){
                @Override
                public void onCancel(DialogInterface dialogInterface) {
                    loadSocial(holder, position);
                }

            });
            dialog.show();

            //Metodos adicionales
            btn_test.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast toast = Toast.makeText(context, "TestButton", Toast.LENGTH_SHORT);
                    toast.show();
                    createNewAddCommentDialog(holder,position);


                }
            });


        }

        private void updateComments(PublicacionesPerfilViewHolder holder, @SuppressLint("RecyclerView") int position) {
            mAdapter= new CommentListAdapter(publicacionClassList.get(position).getCommentInList(), context);
            System.out.println(publicacionClassList.get(position).getCommentInList());
            rv_commentList.setAdapter(mAdapter);
        }


        public void createNewAddCommentDialog(PublicacionesPerfilViewHolder holder, @SuppressLint("RecyclerView") int position){

            //Creamos el dialog
            dialogBuilder2 = new AlertDialog.Builder(context);
            View popupView2=Linflater.inflate(R.layout.popup_addcomment,null);


            SingletonIdioma s= SingletonIdioma.getInstance();
            Resources resources=s.getResources();


            //Definicion de los items
            btn_addcomment=popupView2.findViewById(R.id.btn_addcomment);
            btn_addcomment.setText(resources.getString(R.string.añadircomment));
            comment=popupView2.findViewById(R.id.txt_comment);
            //Hacemos aparecer la ventana
            dialogBuilder2.setView(popupView2);
            dialog2=dialogBuilder2.create();
            dialog2.show();

            //Metodos adicionales
            btn_addcomment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast toast = Toast.makeText(context, "Se ha añadido el comentario", Toast.LENGTH_SHORT);
                    toast.show();
                    publicacionClassList.get(position).addComment(ViewModel.getInstance().getNom(),comment.getText().toString().replaceAll("[^A-Za-z0-9 ]",""));
                    updatePublications(publicacionClassList.get(position));
                    updateComments(holder,position);
                    dialog2.dismiss();

                }


            });


        }
    }
    private void updatePublications(PublicacionClass publicacionClass) {
        ViewModelParametros.getSingletonInstance().updatePublicacion(publicacionClass);
    }


}
