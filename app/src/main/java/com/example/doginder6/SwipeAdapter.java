package com.example.doginder6;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import com.google.android.material.imageview.ShapeableImageView;
import com.yalantis.library.Koloda;
import com.yalantis.library.KolodaListener;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SwipeAdapter extends BaseAdapter {
    private Context context;
    public List<Usuario2> list;
    public Koloda koloda;
    private UserClickListener userClickListener;
    private int currentIndex = 0;
    public final String URL = "http://doginder.dam.inspedralbes.cat:3745/";
    public final String URL2 = "http://192.168.19.88:3745/";
    public Retrofit retrofit;
    public doginderAPI doginderAPI;
    public int idUsu;

    public SwipeAdapter(Context context, List<Usuario2> list, Koloda koloda, UserClickListener userClickListener, int idUsu){
        this.context = context;
        this.list = list;
        this.koloda = koloda;
        this.userClickListener = userClickListener;
        this.idUsu = idUsu;

        configureKolodaListener();

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
        View view;
        if (convertView==null){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_koloda,parent,false);
        }else {
            view = convertView;
        }
        Usuario2 user = list.get(position);
        Log.d("pruebaAdapter", "En el adapter: " +user.toString());
        ShapeableImageView imageView = view.findViewById(R.id.image);
        TextView tvNombre = view.findViewById(R.id.tvNombreMascota);
        ImageView btnNo = view.findViewById(R.id.btnNo);
        ImageView btnSi = view.findViewById(R.id.btnSi);
        TextView tvEdad = view.findViewById(R.id.tvEdad);
        TextView tvRaza = view.findViewById(R.id.tvRaza);
        TextView tvSexo = view.findViewById(R.id.tvSexo);
        ImageButton btnMas = view.findViewById(R.id.btnMas);
        ImageButton btnUser = view.findViewById(R.id.btnUser);

        tvNombre.setText(user.getNombre());
        String url = "http://doginder.dam.inspedralbes.cat:3745"+user.getFoto();
        Picasso.get().load(url).error(R.drawable.two).into(imageView);
        //http://doginder.dam.inspedralbes.cat:3745/uploads/perro1.jpg

        tvEdad.setText(String.valueOf(user.getEdad()) + " año/s");
        tvRaza.setText(user.getRaza());
        tvSexo.setText(user.getSexo());

        btnSi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                koloda.onClickRight();

            }
        });

        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                koloda.onClickLeft();
            }
        });

        btnMas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Aqui saldrá la información sobre " + user.getNombre(), Toast.LENGTH_SHORT).show();
            }
        });

        btnUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Aqui saldrá la información sobre " + user.getNombreUsu(), Toast.LENGTH_SHORT).show();
            }
        });
        Log.d("pruebaAdapter", "getView: " + user);


        return view;
    }

    public interface UserClickListener {
        void onSwipeRight(Usuario2 user);

        void onSwipeLeft(Usuario2 user);

        void onClickRight(Usuario2 user);

        void onClickLeft(Usuario2 user);

        void onLongPress(Usuario2 user);
    }

    private void configureKolodaListener() {
        koloda.setKolodaListener(new KolodaListener() {
            @Override
            public void onNewTopCard(int i) {
                currentIndex = i;
            }

            @Override
            public void onEmptyDeck() {

            }

            @Override
            public void onCardSingleTap(int i) {

            }

            @Override
            public void onCardDrag(int i, @NonNull View view, float v) {

            }

            @Override
            public void onCardDoubleTap(int i) {

            }

            @Override
            public void onCardSwipedRight(int i) {
                if (userClickListener != null && currentIndex >= 0 && currentIndex < list.size()) {
                    Usuario2 user = list.get(currentIndex);
                    userClickListener.onSwipeRight(user);
                    currentIndex++;
                    Log.d("pruebaListener", "Current index: " + currentIndex + " I: "+ i);
                    //Toast.makeText(context, "Has hecho swipe a la derecha a " + user.getNombre()+ " id: "+ user.getIdUsu(), Toast.LENGTH_SHORT).show();
                    darLike(idUsu, user.getIdUsu());
                    Log.d("pruebaSwipeLike", "idUsu1: " + idUsu + " idUsu2: " + user.getIdUsu());
                }
            }

            @Override
            public void onCardSwipedLeft(int i) {
                if (userClickListener != null && currentIndex >= 0 && currentIndex < list.size()) {
                    Usuario2 user = list.get(currentIndex);
                    userClickListener.onSwipeLeft(user);
                    currentIndex++;
                    //Log.d("pruebaListener", "Current index: " + currentIndex + " I: "+ i);
                    //Toast.makeText(context, "Has hecho swipe a la izquierda a " + user.getNombre() + " id: "+ user.getIdUsu(), Toast.LENGTH_SHORT).show();
                    darDislike(idUsu, user.getIdUsu());
                }
            }

            @Override
            public void onClickRight(int i) {
                if (userClickListener != null) {
                    Usuario2 user = list.get(i);
                    userClickListener.onClickRight(user);
                }
            }

            @Override
            public void onClickLeft(int i) {
                if (userClickListener != null) {
                    Usuario2 user = list.get(i);
                    userClickListener.onClickLeft(user);
                }
            }

            @Override
            public void onCardLongPress(int i) {
                if (userClickListener != null) {
                    Usuario2 user = list.get(i);
                    userClickListener.onLongPress(user);
                }
            }
        });
    }

    public void darLike(int idUsu1, int idUsu2){
        retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().create()))
                .build();

        doginderAPI = retrofit.create(doginderAPI.class);

        Call<Void> call = doginderAPI.enviarInteraccion(idUsu1, idUsu2, "LIKE");
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()){
                    Toast.makeText(context, "Has dado like!", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(context, "notSuccesfull", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(context, "onFailure", Toast.LENGTH_SHORT).show();
                Log.d("pruebaSwipe", t.getMessage());
            }
        });
    };

    public void darDislike(int idUsu1, int idUsu2){
        retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().create()))
                .build();

        doginderAPI = retrofit.create(doginderAPI.class);

        Call<Void> call = doginderAPI.enviarInteraccion(idUsu1, idUsu2, "DISLIKE");
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()){
                    Toast.makeText(context, "Has dado dislike!", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(context, "notSuccesfull", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(context, "onFailure", Toast.LENGTH_SHORT).show();
            }
        });
    };



}

