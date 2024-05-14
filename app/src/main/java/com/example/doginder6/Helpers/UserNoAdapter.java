package com.example.doginder6.Helpers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.doginder6.Objects.Usuario2;
import com.example.doginder6.R;

import java.util.List;

public class UserNoAdapter extends RecyclerView.Adapter<UserNoAdapter.UserViewHolder> {

    private List<Usuario2> userList;
    private Context context;

    public UserNoAdapter(List<Usuario2> userList, Context context) {
        this.userList = userList;
        this.context = context;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dialog_item_layout, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        Usuario2 user = userList.get(position);
        holder.textViewItem.setText(user.getNombre());
        holder.btnRecuperar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Recuperar " + user.getNombre(), Toast.LENGTH_SHORT).show();
            }
        });
        String url = Settings.URL2+ user.getFoto();

        Glide.with(context)
                .load(url)  // Reemplaza con tu recurso de imagen
                .apply(RequestOptions.bitmapTransform(new RoundedCorners(150))) // Ajusta el radio según tus preferencias
                .into(holder.imgMascota);
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView textViewItem;
        Button btnRecuperar;
        ImageView imgMascota;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewItem = itemView.findViewById(R.id.tvMascota);
            btnRecuperar = itemView.findViewById(R.id.btnRecuperar);
            imgMascota = itemView.findViewById(R.id.ivMascota);

        }
    }
}
