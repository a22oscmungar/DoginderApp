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
    int distancia = 50000;
    double latitude;
    double longitude;
    doginderAPI doginderAPI;
    private static final int REQUEST_LOCATION_PERMISSION = 1;
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

        // Verificar y solicitar permisos si es necesario
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION_PERMISSION);
            Log.d("prueba", "onCreate: No hay permisos");
        } else {
            // Inicializar y comenzar las actualizaciones de ubicación
            startLocationUpdates();
            Log.d("pruebaLocation", locationHelper.toString());
            //Log.d("pruebaLocation", locationHelper.getLastKnownLocation().toString());
            Log.d("pruebaOnCreate", "Hay permisos");
        }


        llamarUsuarios();
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

    public void llamarUsuarios(){
            if(distancia == 0){
                Toast.makeText(this, "Introduce una distancia", Toast.LENGTH_LONG).show();
            }else{
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
                        for(int i=0; i<list.size(); i++){
                            //Log.d("pruebaLista", "onUsersReceived: " + list.get(i).getNombreUsu());
                            Log.d("PruebaLista", "Response: "+users.get(i).getNombreUsu());
                        }
                        Log.d("prueba", "onUsersReceived: " + list.size());
                        if(list.size()!=0 && list!=null){
                            koloda = findViewById(R.id.koloda);
                            swipeAdapter = new SwipeAdapter(MainActivity.this, list);
                            koloda.setAdapter(swipeAdapter);
                            swipeAdapter.notifyDataSetChanged();
                        }else{
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


            Call<List<UserResponse.Usuario>> call = doginderAPI.getNearbyUsers(41.3751, 2.1499, distancia);
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

    private void startLocationUpdates() {
        locationHelper = new LocationHelper(this);
        locationHelper.requestLocationUpdates();
        Log.d("prueba", "startLocationUpdates: "+locationHelper.getLastKnownLocation());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permiso concedido, iniciar actualizaciones de ubicación
                startLocationUpdates();
                Log.d("prueba", "onRequestPermissionsResult: Permiso concedido");
            } else {
                Log.d("prueba", "onRequestPermissionsResult: Permiso denegado");
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


