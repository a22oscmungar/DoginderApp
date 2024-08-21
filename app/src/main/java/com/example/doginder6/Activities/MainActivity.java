package com.example.doginder6.Activities;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.doginder6.Fragments.FragmentChat;
import com.example.doginder6.Fragments.FragmentMasFunciones;
import com.example.doginder6.Fragments.FragmentPerfil;
import com.example.doginder6.Fragments.FragmentSwiper;
import com.example.doginder6.Helpers.MatchListener;
import com.example.doginder6.Helpers.Settings;
import com.example.doginder6.Helpers.SocketForegroundService;
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

import java.io.IOException;
import java.util.List;

import io.socket.emitter.Emitter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements SocketListener, MatchListener {
    Retrofit retrofit;
    com.example.doginder6.Helpers.doginderAPI doginderAPI;
    public SocketManager socketManager;
    public static String socketId;
    private static final String CHANNEL_ID = "match_notification_channel";
    public static final int NOTIFICATION_ID = 1;
    public static Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Check current theme
        int nightModeFlags = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        switch (nightModeFlags) {
            case Configuration.UI_MODE_NIGHT_YES:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                break;

            case Configuration.UI_MODE_NIGHT_NO:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                break;

            case Configuration.UI_MODE_NIGHT_UNDEFINED:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                break;
        }
        setContentView(R.layout.activity_main);

        // Verificar si hay una sesión guardada
        SharedPreferences preferences = getSharedPreferences("credenciales", MODE_PRIVATE);
        int userId = preferences.getInt("id", -1); // Si no hay id, devuelve -1
        Log.d("pruebaId", "id: " + userId);
        if (userId == -1) {
            // No hay usuario guardado, redirigir a la pantalla de login
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
            return; // Salir del método onCreate para no seguir ejecutando el código
        }

        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            askNotificationPermission();
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        configurarSocket();

        // Solicitar permiso si es necesario
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            askNotificationPermission();
        } else {
            startSocketService();
        }

        TabLayout tabLayout = findViewById(R.id.tabLayout);
        tabLayout.addTab(tabLayout.newTab().setText("Swipe"));
        tabLayout.addTab(tabLayout.newTab().setText("Chat"));
        tabLayout.addTab(tabLayout.newTab().setText("Mi perfil"));
        tabLayout.addTab(tabLayout.newTab().setText("Más funciones"));

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new FragmentSwiper())
                .commit();

        // Manejar eventos de cambio de pestaña
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //Log.d("FragmentSwiper", "onTabSelected" + tab.getPosition());
                switch (tab.getPosition()) {
                    case 0:
                        // Cargar el primer fragmento
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment_container, new FragmentSwiper())
                                .commit();
                        break;
                    case 1:
                        // Cargar el segundo fragmento
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment_container, new FragmentChat())
                                .commit();
                        break;
                    case 2:
                        // Cargar el segundo fragmento

                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment_container, new FragmentPerfil())
                                .commit();
                        break;
                    case 3:
                        // Cargar el segundo fragmento
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment_container, new FragmentMasFunciones())
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
            TabLayout.Tab chatTab = tabLayout.getTabAt(1); // Índice de la pestaña de chat
            if (chatTab != null) {
                chatTab.select();
            }
        }
    }

    // Declare the launcher at the top of your Activity/Fragment:
    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    // FCM SDK (and your app) can post notifications.
                } else {
                    // TODO: Inform user that that your app will not show notifications.
                }
            });

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    private void askNotificationPermission() {
        // This is only necessary for API level >= 33 (TIRAMISU)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                    PackageManager.PERMISSION_GRANTED) {
                // FCM SDK (and your app) can post notifications.
            } else if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
                // TODO: display an educational UI explaining to the user the features that will be enabled
                //       by them granting the POST_NOTIFICATION permission. This UI should provide the user
                //       "OK" and "No thanks" buttons. If the user selects "OK," directly request the permission.
                //       If the user selects "No thanks," allow the user to continue without notifications.
            } else {
                // Directly ask for the permission
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
            }
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, 1);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Intent serviceIntent = new Intent(this, SocketForegroundService.class);
        stopService(serviceIntent);
    }

    private ActivityResultLauncher<String> requestPermissionLauncher2 =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    // Permiso concedido, puedes proceder
                    startSocketService();
                } else {
                    // Permiso denegado, toma medidas adicionales si es necesario
                }
            });

    private void startSocketService() {
        // Iniciar el servicio en primer plano
        Intent serviceIntent = new Intent(this, SocketForegroundService.class);
        ContextCompat.startForegroundService(this, serviceIntent);
    }

    @SuppressLint("StaticFieldLeak")
    public void updateSocket() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    SharedPreferences preferences = getSharedPreferences("credenciales", MODE_PRIVATE);
                    int id = preferences.getInt("id", 0);

                    socketId = socketManager.getSocketId();

                    retrofit = new Retrofit.Builder()
                            .baseUrl(Settings.URLlocal)
                            .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().create()))
                            .build();

                    doginderAPI = retrofit.create(doginderAPI.class);
                    Call<Void> call = doginderAPI.socketUpdate(id, socketId);
                    Response<Void> response = call.execute();
                    if (response.isSuccessful()) {
                        Log.d("socket", "socket actualizado");
                    }
                } catch (IOException e) {
                    Log.d("socket", "Error al actualizar el socket: " + e.getMessage());
                }
                return null;
            }
        }.execute();
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

        Log.d("pruebaMatch", "onMatch");

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                .setSmallIcon(R.drawable.notificacion)
                .setContentTitle("Nuevo match!")
                .setContentText("Has hecho match con alguien!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        notificationManager.notify(NOTIFICATION_ID, builder.build());

    }


    private Emitter.Listener nuevoMatch = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.d("pruebaMatch", "Nuevo Match");

                    Toast.makeText(getApplicationContext(), "Nuevo match!", Toast.LENGTH_SHORT).show();
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                            .setSmallIcon(R.drawable.notificacion)
                            .setContentTitle("Nuevo match!")
                            .setContentText("Has hecho match con alguien!")
                            .setPriority(NotificationCompat.PRIORITY_DEFAULT);

                    NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());
                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                        Log.d("pruebaMatch", "No tiene permisos");
                        return;
                    }
                    notificationManager.notify(NOTIFICATION_ID, builder.build());


                }
            });
        }
    };



}


