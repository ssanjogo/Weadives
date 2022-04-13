package com.example.weadives.PantallaMiPerfil;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weadives.ParametrosClass;
import com.example.weadives.R;

import java.util.List;

public class ParametrosPerfilAdapter extends RecyclerView.Adapter<ParametrosPerfilAdapter.ParametrosPerfilViewHolder> {


    private List<ParametrosClass> parametrosClassList;
    private Context context;


    public ParametrosPerfilAdapter(List<ParametrosClass> parametrosClassList, Context context) {
        this.parametrosClassList = parametrosClassList;
        this.context = context;
    }

    @NonNull
    @Override
    public ParametrosPerfilViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.one_parameter_list,parent,false);
        return new ParametrosPerfilViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ParametrosPerfilViewHolder holder, int position) {
        //final Context contextPref;
        //final Resources resources;
        //contextPref = LocaleHelper.setLocale(context, cargarPreferencias());
        //resources = contextPref.getResources();

        //holder.txt_viento.setText(resources.getString(R.string.viento)+ " : ");
        //holder.txt_activityName.setText(resources.getString(R.string.NombreActividad) + " : ");
        holder.txt_viento.setText("Wind :");
        holder.txt_userIdOutput.setText(Integer.toString(parametrosClassList.get(position).getUserID()));
        holder.txt_activityName.setText("Activity Name :");
        holder.txt_vientoOutput.setText(Float.toString(parametrosClassList.get(position).getViento()));
        holder.txt_activityNameOutput.setText(parametrosClassList.get(position).getNombreActividad());
        holder.txt_userId.setText("ID");
    }

    @Override
    public int getItemCount() {
        return parametrosClassList.size();
    }

    public class ParametrosPerfilViewHolder extends RecyclerView.ViewHolder {
        TextView txt_activityName;
        TextView txt_activityNameOutput;
        TextView txt_userId;
        TextView txt_userIdOutput;
        TextView txt_viento;
        TextView txt_vientoOutput;


        public ParametrosPerfilViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_activityName=itemView.findViewById(R.id.txt_activityName);
            txt_activityNameOutput=itemView.findViewById(R.id.txt_activityNameOutput);
            txt_userId=itemView.findViewById(R.id.txt_userId);
            txt_userIdOutput=itemView.findViewById(R.id.txt_userIdOutput);
            txt_viento=itemView.findViewById(R.id.txt_viento);
            txt_vientoOutput=itemView.findViewById(R.id.txt_vientoOutput);
        }
    }
    private String cargarPreferencias() {//TODO-------------------------------------------------------------------------------TODO TODO TODO Getshared preferences funciona mal
        //SharedPreferences preferencias = getSharedPreferences("idioma",Context.MODE_PRIVATE);
        //return preferencias.getString("idioma","en");
        return "en";
    }
}
