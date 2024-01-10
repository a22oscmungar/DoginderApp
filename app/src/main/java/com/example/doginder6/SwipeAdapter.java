package com.example.doginder6;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;

import com.google.android.material.imageview.ShapeableImageView;

import java.util.List;

public class SwipeAdapter extends BaseAdapter {
    private Context context;
    public List<UserResponse.Usuario> list;


    public SwipeAdapter(Context context, List<UserResponse.Usuario> list) {
        this.context = context;
        this.list = list;

    }

    @Override
    public int getCount() {
        return list != null ? list.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        // Devuelve el objeto en la posición dada
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return list.get(position).getIdUsu(); // Puedes cambiar esto según la estructura real de tu objeto Usuario
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Log.d("prueba", "getView: "+list.get(position).getNombreUsu());
        View view;
        if (convertView==null){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_koloda,parent,false);
        }else {
            view = convertView;
        }
        UserResponse.Usuario user = list.get(position);
        ShapeableImageView imageView = view.findViewById(R.id.image);
        TextView tvNombre = view.findViewById(R.id.tvNombre);
        ImageView btnNo = view.findViewById(R.id.btnNo);
        ImageView btnSi = view.findViewById(R.id.btnSi);

        tvNombre.setText(user.getNombreUsu());
        String url = "http://doginder.dam.inspedralbes.cat:3745"+user.getImageUrl();
        Picasso.get().load(url).error(R.drawable.two).into(imageView);
        //http://doginder.dam.inspedralbes.cat:3745/uploads/perro1.jpg

        btnSi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("prueba", "onClick: " + user.getIdUsu());
                MainActivity.koloda.onButtonClick(true);
            }
        });
        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("prueba", "onClick: " + user.getIdUsu());
                MainActivity.koloda.onButtonClick(false);
            }
        });


        Log.d("pruebaUrl", url);

        return view;
    }
}

