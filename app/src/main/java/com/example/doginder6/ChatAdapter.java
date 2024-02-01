package com.example.doginder6;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {

    private ArrayList<ChatItem> chatItems;

    // Constructor
    public ChatAdapter() {
        this.chatItems = new ArrayList<>();
    }

    // Método para establecer la lista de elementos de chat
    public void setChatItems(ArrayList<ChatItem> chatItems) {
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
        ChatItem chatItem = chatItems.get(position);

        // Aquí configuras tus vistas con los datos del ChatItem
        holder.tvNombreMascota.setText(chatItem.getNombreUsuario());

        Glide.with(holder.itemView.getContext())
                .load("http://doginder.dam.inspedralbes.cat:3745"+chatItem.getFotoPerfil())  // Reemplaza con tu recurso de imagen
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

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombreMascota = itemView.findViewById(R.id.tvNombreMascota);
            ivFotoMascota = itemView.findViewById(R.id.ivFotoMascota);
            // Puedes agregar más inicializaciones de vistas aquí según tu diseño
        }
    }
}
