package com.example.app_sepiem;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdapterEscolas extends RecyclerView.Adapter<AdapterEscolas.ViewHolderEscolas> {

    Context context;
    ArrayList<Escolas> list;

    public AdapterEscolas(Context context, ArrayList<Escolas> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolderEscolas onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.list_layout_escolas, parent, false);
        return new ViewHolderEscolas(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderEscolas holder, int position) {

        Escolas escolas = list.get(position);

        holder.designacao.setText(escolas.getDesignacao());
        holder.localizacao.setText(escolas.getLocalizacao());

        holder.v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, perfilEscola.class);

                intent.putExtra("designacao", escolas.getDesignacao());

                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);










            }
        });


    }

    @Override
    public int getItemCount() {

        return list.size();
    }

    public static class ViewHolderEscolas extends RecyclerView.ViewHolder{

       TextView designacao,localizacao;

       View v;

        public ViewHolderEscolas(@NonNull View itemView) {
            super(itemView);

            designacao = itemView.findViewById(R.id.txtDesignacaoEscola);
            localizacao = itemView.findViewById(R.id.txtLocalizacaoEscola);

            v = itemView;
        }
    }
}
