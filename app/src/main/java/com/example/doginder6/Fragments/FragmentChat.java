package com.example.doginder6.Fragments;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doginder6.Helpers.ChatAdapter;
import com.example.doginder6.Helpers.Settings;
import com.example.doginder6.Helpers.UserNoAdapter;
import com.example.doginder6.Helpers.UsersGroupAdapter;
import com.example.doginder6.Objects.Usuario2;
import com.example.doginder6.R;
import com.example.doginder6.Helpers.doginderAPI;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FragmentChat extends Fragment {
    View rootView;
    RecyclerView recyclerView;
    TextView noChats;
    ChatAdapter chatAdapter;
    Button btnCrearGrupo;
    public final String URL = "http://doginder.dam.inspedralbes.cat:3745/";
    public Retrofit retrofit;
    public com.example.doginder6.Helpers.doginderAPI doginderAPI;
    int idUsu;
    List<Usuario2> matches = new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_chat, container, false);

        recyclerView = rootView.findViewById(R.id.rvChats);
        noChats = rootView.findViewById(R.id.tvNoChats);


        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        chatAdapter = new ChatAdapter(this.getContext());
        recyclerView.setAdapter(chatAdapter);
        btnCrearGrupo = rootView.findViewById(R.id.btnCrearGrupo);

        SharedPreferences preferences = rootView.getContext().getSharedPreferences("credenciales", rootView.getContext().MODE_PRIVATE);
        idUsu = preferences.getInt("id", 0);

        getMatches(idUsu);

        btnCrearGrupo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(rootView.getContext());
                if (matches.size() == 0) {
                    Toast.makeText(rootView.getContext(), "No tienes matches para crear un grupo", Toast.LENGTH_SHORT).show();
                } else {
                    builder.setTitle("Crear grupo");
                    builder.setMessage("Selecciona los integrantes del grupo");
                    // hacemos un recycler view con usersGroupAdapter

                    RecyclerView recyclerView = new RecyclerView(rootView.getContext());
                    recyclerView.setLayoutManager(new LinearLayoutManager(rootView.getContext()));
                    UsersGroupAdapter adapter = new UsersGroupAdapter(matches, rootView.getContext());
                    recyclerView.setAdapter(adapter);

                    builder.setView(recyclerView);
                    builder.setPositiveButton("Crear", null);
                    builder.setNegativeButton("Cancelar", null);
                    AlertDialog dialog = builder.create();
                    dialog.show();

                }
            }
        });

        return rootView;
    }

    public void getMatches(int idUsu){
        retrofit = new Retrofit.Builder()
                .baseUrl(Settings.URL2)
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().create()))
                .build();

        doginderAPI = retrofit.create(doginderAPI.class);

        Call<List<Usuario2>> call = doginderAPI.getMatches(idUsu);

        call.enqueue(new Callback<List<Usuario2>>() {
            @Override
            public void onResponse(Call<List<Usuario2>> call, Response<List<Usuario2>> response) {
                if (response.isSuccessful()){
                    matches = response.body();
                    chatAdapter.setChatItems(matches);
                    if(matches.size() == 0) {
                        noChats.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                    }else{
                        noChats.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                    }

                    Log.d("pruebaMatches", matches.toString());
                }else{
                    Toast.makeText(rootView.getContext(), "Ha habido un error al conseguir tus matches", Toast.LENGTH_SHORT).show();
                    Log.d("errorMatches", response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Usuario2>> call, Throwable t) {
                Toast.makeText(rootView.getContext(), "Ha habido un error con el servidor", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
