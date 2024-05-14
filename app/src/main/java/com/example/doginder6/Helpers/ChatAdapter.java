package com.example.doginder6.Helpers;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.doginder6.Activities.ChatActivity;
import com.example.doginder6.Objects.Usuario2;
import com.example.doginder6.R;

import java.util.ArrayList;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {

    private List<Usuario2> chatItems;
    public Context context;

    // Constructor
    public ChatAdapter(Context context) {
        this.chatItems = new ArrayList<>();
        this.context = context;
    }

    // Método para establecer la lista de elementos de chat
    public void setChatItems(List<Usuario2> chatItems) {
        this.chatItems = chatItems;
        notifyDataSetChanged(); // Notificar al adaptador que los datos han cambiado
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_usuario_chat, parent, false);
        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        Usuario2 chatItem = chatItems.get(position);

        // Aquí configuras tus vistas con los datos del ChatItem
        holder.tvNombreMascota.setText(chatItem.getNombre());
        holder.llChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChatActivity.class);
                intent.putExtra("usuario", chatItem);
                context.startActivity(intent);
            }
        });


        Glide.with(holder.itemView.getContext())
                .load(Settings.URL2 +chatItem.getFoto())  // Reemplaza con tu recurso de imagen
                .apply(RequestOptions.circleCropTransform())
                .into(holder.ivFotoMascota);
        // Puedes agregar más configuraciones según tus necesidades
    }

    @Override
    public int getItemCount() {
        return chatItems.size();
    }

    // Clase interna para el ViewHolder
    public static class ChatViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombreMascota;
        ImageView ivFotoMascota;
        LinearLayout llChat;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombreMascota = itemView.findViewById(R.id.tvNombreMascota);
            ivFotoMascota = itemView.findViewById(R.id.ivFotoMascota);
            llChat = itemView.findViewById(R.id.llChat);
            // Puedes agregar más inicializaciones de vistas aquí según tu diseño
        }
    }
}
