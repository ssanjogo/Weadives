package com.example.weadives.PantallaPerfilAmigo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weadives.R;

import java.util.List;

public class CommentListAdapter extends RecyclerView.Adapter<CommentListAdapter.CommentListViewHolder> {

    public CommentListAdapter(List<String> commentList, Context context) {
        this.commentList = commentList;
        this.context = context;
    }


    List<String> commentList;
    Context context;

    @NonNull
    @Override
    public CommentListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.one_comment_list,parent,false);
        CommentListAdapter.CommentListViewHolder holder = new CommentListAdapter.CommentListViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CommentListViewHolder holder, int position) {
        holder.comment.setText(commentList.get(position));
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }


    public class CommentListViewHolder extends RecyclerView.ViewHolder{
    TextView comment;


        public CommentListViewHolder(@NonNull View itemView) {
            super(itemView);
            comment=itemView.findViewById(R.id.txt_commentInList);
        }
    }
}
