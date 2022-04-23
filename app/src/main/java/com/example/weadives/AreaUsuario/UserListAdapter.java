package com.example.weadives.AreaUsuario;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.weadives.PantallaPerfilAmigo.PantallaPerfilAmigo;
import com.example.weadives.R;
import com.example.weadives.ViewModel;

import java.util.List;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.UserListViewHolder> {

    private List<UserClass> userClassList;
    private Context context;
    private int limite;

    public UserListAdapter(List<UserClass> userClassList, Context context) {
        this.userClassList = userClassList;
        this.context = context;
        this.limite = 3;
    }

    @NonNull
    @Override
    public UserListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.one_frienduser_list,parent,false);
        return new UserListAdapter.UserListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserListViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.txt_friendname.setText(userClassList.get(position).getUsername());
        Glide.with(context).load(userClassList.get(position).getUrlImg()).into(holder.profilepic);
        if(position >= limite) {
            holder.btn_accept.setVisibility(View.GONE);
            holder.btn_deny.setVisibility(View.GONE);
        } else {
            holder.btn_accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    userClassList.get(position).acceptSolicitud();
                    holder.btn_accept.setVisibility(View.GONE);
                    holder.btn_deny.setVisibility(View.GONE);
                }
            });
            holder.btn_deny.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    holder.btn_accept.setVisibility(View.GONE);
                    holder.btn_deny.setVisibility(View.GONE);
                }
            });
        }

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, PantallaPerfilAmigo.class);
                intent.putExtra("username", userClassList.get(position).getUsername());
                intent.putExtra("id", userClassList.get(position).getId());
                intent.putExtra("Imagen", userClassList.get(position).getUrlImg());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (userClassList.size() == 0){
            return 0;
        }
        return userClassList.size();
    }

    public class UserListViewHolder extends RecyclerView.ViewHolder {
        TextView txt_friendname;
        ImageView profilepic;
        ImageButton btn_accept;
        ImageButton btn_deny;
        ConstraintLayout parentLayout;

        public UserListViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_friendname=itemView.findViewById(R.id.txt_friendname);
            profilepic=itemView.findViewById(R.id.profilepic);
            btn_accept=itemView.findViewById(R.id.btn_accept);
            btn_deny=itemView.findViewById(R.id.btn_deny);

            parentLayout = itemView.findViewById(R.id.friendLayout);
        }
    }
}
