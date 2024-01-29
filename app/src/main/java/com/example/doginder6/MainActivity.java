package com.example.doginder6;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.GsonBuilder;
import com.yalantis.library.Koloda;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnBuscar = findViewById(R.id.btnBuscar);
        etDistancia = findViewById(R.id.etDistancia);
        btnRegistro = findViewById(R.id.btnRegistro);

        btnRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // Verificar y solicitar permisos
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            // Si los permisos ya están otorgados, obtener la ubicación
            getLastLocation();
        } else {
            // Si no, solicitar permisos
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION_PERMISSION);
        }

        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                distancia = Integer.parseInt(etDistancia.getText().toString());
                Log.d("prueba", "onCreate: " + distancia);
                //koloda.reloadAdapterData();
                llamarUsuarios();
            }
        });
    }

    public void llamarUsuarios() {
        if (distancia == 0) {
            Toast.makeText(this, "Introduce una distancia", Toast.LENGTH_LONG).show();
        } else {
            //Log.d("prueba", "onCreate: " + distancia);
            recibirUsuarios(new UserCallback() {
                @Override
                public void onUsersReceived(List<UserResponse.Usuario> users) {
                    // Actualizar la lista y el adaptador aquí
                    if (list == null) {
                        list = new ArrayList<>();
                    } else {
                        list.clear();
                    }
                    list.addAll(users);
                    for (int i = 0; i < list.size(); i++) {
                        //Log.d("pruebaLista", "onUsersReceived: " + list.get(i).getNombreUsu());
                        Log.d("PruebaLista", "Response: " + users.get(i).getNombreUsu());
                    }
                    Log.d("prueba", "onUsersReceived: " + list.size());
                    if (list.size() != 0 && list != null) {
                        koloda = findViewById(R.id.koloda);
                        swipeAdapter = new SwipeAdapter(MainActivity.this, list);
                        koloda.setAdapter(swipeAdapter);
                        swipeAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(MainActivity.this, "No hay usuarios cerca", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(String errorMessage) {
                    Log.d("TAG1", "onFailure: " + errorMessage);
                }
            });
        }
    }


    public void recibirUsuarios(UserCallback callback) {
        retrofit = new Retrofit.Builder()
                .baseUrl("http://doginder.dam.inspedralbes.cat:3745/")
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().create()))
                .build();

        doginderAPI = retrofit.create(doginderAPI.class);

        //UserNearbyRequest userNearbyRequest = new UserNearbyRequest(41.3851, 2.1734, 15);
        //Location lastKnownLocation = locationHelper.getLastKnownLocation();

        Log.d("prueba", "datos pre call: " + latitude + " " + longitude);
        Call<List<UserResponse.Usuario>> call = doginderAPI.getNearbyUsers(latitude, longitude, distancia);
        //Call<List<UserResponse.Usuario>> call = doginderAPI.getNearbyUsers(latitude, longitude, distancia);

        call.enqueue(new Callback<List<UserResponse.Usuario>>() {
            @Override
            public void onResponse(Call<List<UserResponse.Usuario>> call, Response<List<UserResponse.Usuario>> response) {
                if (response.isSuccessful()) {
                    callback.onUsersReceived(response.body());
                } else {
                    callback.onFailure("Error en la respuesta: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<UserResponse.Usuario>> call, Throwable t) {
                callback.onFailure("Error en la llamada: " + t.getMessage());
            }
        });

    }

    public interface UserCallback {
        void onUsersReceived(List<UserResponse.Usuario> users);

        void onFailure(String errorMessage);
    }

    // Método para obtener la última ubicación conocida
    private void getLastLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            // Aquí obtienes la ubicación del usuario
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                            Log.d("prueba", "Es esta ubi: " + latitude + " " + longitude);
                            // Puedes hacer lo que necesites con la ubicación aquí

                            llamarUsuarios();
                        }
                    }
                });
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permiso concedido, obtener la ubicación
                getLastLocation();
            } else {
                // Permiso denegado, puedes mostrar un mensaje o tomar otras acciones
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Detener las actualizaciones de ubicación al destruir la actividad
        if (locationHelper != null) {
            locationHelper.removeLocationUpdates();
        }
    }

}


