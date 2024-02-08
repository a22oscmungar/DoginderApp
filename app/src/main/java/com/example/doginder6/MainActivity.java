package com.example.doginder6;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.GsonBuilder;
import com.yalantis.library.Koloda;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements SocketListener{
    public SwipeAdapter swipeAdapter;
    Button btnBuscar, btnRegistro;
    EditText etDistancia;
    private List<UserResponse.Usuario> list;
    public static Koloda koloda;
    Retrofit retrofit;
    int distancia = 25;
    double latitude;
    double longitude;
    doginderAPI doginderAPI;
    private static final int REQUEST_LOCATION_PERMISSION = 1;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationHelper locationHelper;
    public SocketManager socketManager;
    public final String URL = "http://doginder.dam.inspedralbes.cat:3745/";
    public final String URL2 = "http://192.168.19.159:3745/";
    public String socketId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        configurarSocket();

        BottomAppBar bottomAppBar = findViewById(R.id.bottomAppBar);

        bottomAppBar.setOnMenuItemClickListener(new BottomAppBar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent;
                switch(item.getItemId()){
                    case R.id.registro:
                        intent = new Intent(MainActivity.this, RegisterActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.login:
                        intent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.main:
                        Toast.makeText(MainActivity.this, "Ya estas en el main", Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });

        TabLayout tabLayout = findViewById(R.id.tabLayout);
        tabLayout.addTab(tabLayout.newTab().setText("Swipe"));
        tabLayout.addTab(tabLayout.newTab().setText("Mi perfil"));
        tabLayout.addTab(tabLayout.newTab().setText("Chat"));

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new FragmentSwiper())
                .commit();

        // Manejar eventos de cambio de pestaña
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Log.d("FragmentSwiper", "onTabSelected" + tab.getPosition());
                switch (tab.getPosition()) {
                    case 0:
                        Log.d("FragmentSwiper", "FragmentSwiper");

                        // Cargar el primer fragmento
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment_container, new FragmentSwiper())
                                .commit();
                        break;
                    case 1:
                        // Cargar el segundo fragmento
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment_container, new FragmentPerfil())
                                .commit();
                        break;
                    case 2:
                        // Cargar el segundo fragmento
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment_container, new FragmentChat())
                                .commit();
                        break;
                    // Añadir más casos según sea necesario
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // No se necesita implementar en este ejemplo
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // No se necesita implementar en este ejemplo
            }
        });

    }

    @Override
    public void onDestroy() {

        super.onDestroy();
        socketManager.disconnect();
    }

    public void updateSocket(){
        Log.d("socket", "updateSocket funcion");
        SharedPreferences preferences = getSharedPreferences("credenciales", MODE_PRIVATE);
        int id = preferences.getInt("id", 0);

        socketId = socketManager.getSocketId();
        Log.d("pruebaSocket", "updateSocket funcion: " + socketId);

        retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().create()))
                .build();

        doginderAPI = retrofit.create(doginderAPI.class);
        Call<Void> call = doginderAPI.socketUpdate(id, socketId);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()){
                    Log.d("socket", "socket actualizado");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d("socket", "onFailure: "+ t.getMessage());
            }
        });
    }


    @Override
    public void onSocketConnected() {
        String socketId = socketManager.getSocketId();
        Log.d("pruebaSocket", "updateSocket funcion: " + socketId);
        updateSocket();
    }

    @Override
    public void onNuevoMensaje(String mensaje, int idUsu1, int idUsu2) {

    }

    private void configurarSocket() {
        socketManager = new SocketManager(this);

        // Agrega la actividad como escuchador del socket
        socketManager.addSocketListener(this);

        // Conecta el socket
        socketManager.connect();
    }
}


