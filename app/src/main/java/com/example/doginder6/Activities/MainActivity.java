package com.example.doginder6.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.doginder6.Fragments.FragmentChat;
import com.example.doginder6.Fragments.FragmentPerfil;
import com.example.doginder6.Fragments.FragmentSwiper;
import com.example.doginder6.Helpers.LocationHelper;
import com.example.doginder6.Helpers.MatchListener;
import com.example.doginder6.Objects.UserResponse;
import com.example.doginder6.R;
import com.example.doginder6.Helpers.SocketListener;
import com.example.doginder6.Helpers.SocketManager;
import com.example.doginder6.Helpers.SwipeAdapter;
import com.example.doginder6.Helpers.doginderAPI;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.GsonBuilder;
import com.yalantis.library.Koloda;

import java.util.List;

import io.socket.emitter.Emitter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements SocketListener, MatchListener {
    public SwipeAdapter swipeAdapter;
    Button btnBuscar, btnRegistro;
    EditText etDistancia;
    private List<UserResponse.Usuario> list;
    public static Koloda koloda;
    Retrofit retrofit;
    int distancia = 25;
    double latitude;
    double longitude;
    com.example.doginder6.Helpers.doginderAPI doginderAPI;
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

        Intent intent = getIntent();
        String tabToOpen = intent.getStringExtra("Tab");
        if (tabToOpen != null && tabToOpen.equals("Chat")) {
            // Seleccionar la pestaña de chat
            TabLayout.Tab chatTab = tabLayout.getTabAt(2); // Índice de la pestaña de chat
            if (chatTab != null) {
                chatTab.select();
            }
        }
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

    /*@Override
    public void onNuevoMensaje(String mensaje, int idUsu1, int idUsu2) {
        Toast.makeText(this, "Tienes un nuevo mensaje!", Toast.LENGTH_SHORT).show();
    }*/

    private void configurarSocket() {
        socketManager = new SocketManager(this);

        // Agrega la actividad como escuchador del socket
        socketManager.addSocketListener(this);
        socketManager.addMatchListener(this);
        // Conecta el socket
        socketManager.connect();
        socketManager.on("match", nuevoMatch);


    }

    @Override
    public void onMatch() {
        Toast.makeText(this, "¡Match!", Toast.LENGTH_SHORT).show();
        socketManager.notifyMatch(); // Asegúrate de agregar esto
    }

    private Emitter.Listener nuevoMatch = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), "Nuevo match!", Toast.LENGTH_SHORT).show();
                }
            });
        }
    };



}


