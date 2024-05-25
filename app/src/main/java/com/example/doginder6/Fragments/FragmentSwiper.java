package com.example.doginder6.Fragments;

import static android.content.Context.MODE_PRIVATE;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doginder6.Activities.LoginActivity;
import com.example.doginder6.Activities.MainActivity;
import com.example.doginder6.Helpers.LocationHelper;
import com.example.doginder6.Helpers.Settings;
import com.example.doginder6.Helpers.UserNoAdapter;
import com.example.doginder6.Objects.UserNearbyRequest;
import com.example.doginder6.Objects.Usuario2;
import com.example.doginder6.R;
import com.example.doginder6.Helpers.SwipeAdapter;
import com.example.doginder6.Helpers.doginderAPI;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.GsonBuilder;
import com.yalantis.library.Koloda;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FragmentSwiper extends Fragment implements LocationListener {
    public SwipeAdapter swipeAdapter;
    Button btnBuscar, btnFiltro;
    EditText etDistancia;
    private List<Usuario2> list;
    public static Koloda koloda;
    Retrofit retrofit;
    int distancia = 50;
    double latitude;
    double longitude;
    static com.example.doginder6.Helpers.doginderAPI doginderAPI;
    private static final int REQUEST_LOCATION_PERMISSION = 1;
    static View rootView;
    public LocationManager locationManager;
    public ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //iniciamos las variables
        rootView = inflater.inflate(R.layout.fragment_swiper, container, false);
        btnBuscar = rootView.findViewById(R.id.btnBuscar);
        etDistancia = rootView.findViewById(R.id.etDistancia);
        koloda = rootView.findViewById(R.id.koloda);
        btnFiltro = rootView.findViewById(R.id.btnFiltro);

        setHasOptionsMenu(true); // Indica que este fragmento tiene un menú de opciones
        Toolbar toolbar = rootView.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        Objects.requireNonNull(((AppCompatActivity) getActivity()).
                getSupportActionBar()).setDisplayShowTitleEnabled(false);


        //escondemos por defecto la distancia
        btnBuscar.setVisibility(View.GONE);
        etDistancia.setVisibility(View.GONE);


        //listener para el botón de filtro
        btnFiltro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btnBuscar.getVisibility() == View.VISIBLE) {
                    btnBuscar.setVisibility(View.GONE);
                    etDistancia.setVisibility(View.GONE);
                } else {
                    btnBuscar.setVisibility(View.VISIBLE);
                    etDistancia.setVisibility(View.VISIBLE);
                }
            }
        });

        // Verificar y solicitar permisos
        if (ContextCompat.checkSelfPermission(rootView.getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            // Si los permisos ya están otorgados, obtener la ubicación
            getLocation2();
        } else {
            // Si no, solicitar permisos
            ActivityCompat.requestPermissions(this.requireActivity(),
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION_PERMISSION);
        }
        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                distancia = Integer.parseInt(etDistancia.getText().toString());
                llamarUsuarios();
                etDistancia.setVisibility(View.GONE);
                btnBuscar.setVisibility(View.GONE);
            }
        });
        progressBar = rootView.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        return rootView;
    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_swiper, menu); // Infla el menú de opciones en la barra de herramientas
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // Maneja los eventos de clic en los elementos del menú
        switch (item.getItemId()) {
            case R.id.seeAll:
                // Maneja la acción de búsqueda
                AlertDialog.Builder builder = new AlertDialog.Builder(rootView.getContext());
                builder.setTitle("Ver todos");
                builder.setMessage("¿Quieres incluir a los usuarios a los que has rechazado?");
                builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Mostrar todos los usuarios
                        Toast.makeText(getContext(), "Ver todos", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Mostrar solo los usuarios que no has rechazado
                        Toast.makeText(getContext(), "Ver solo los que no has rechazado", Toast.LENGTH_SHORT).show();
                    }
                });

                // Mostrar el diálogo
                AlertDialog dialog = builder.create();
                dialog.show();

                return true;
            case R.id.unblock:
                getNo();
                return true;
            case R.id.filtro:
                etDistancia.setVisibility(View.VISIBLE);
                btnBuscar.setVisibility(View.VISIBLE);
                return true;
            case R.id.cerrarSesion:
                logout();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void logout() {
        // Borrar las credenciales almacenadas en SharedPreferences
        SharedPreferences preferences = rootView.getContext().getSharedPreferences("credenciales", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();

        // Redirigir al usuario a la pantalla de login
        Intent intent = new Intent(rootView.getContext(), LoginActivity.class);
        startActivity(intent);

        // Cerrar la MainActivity para que no esté en el back stack
        requireActivity().finish();
    }

    public void getLocation2(){
        locationManager = (LocationManager) rootView.getContext().getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(rootView.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(rootView.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5, this);
    }
    @Override
    public void onLocationChanged(@NonNull Location location) {
        Log.d("pruebaDistancia", "onLocationChanged: " + location.getLatitude() + " " + location.getLongitude());
        latitude = location.getLatitude();
        longitude = location.getLongitude();

        llamarUsuarios();
    }

    @Override
    public void onLocationChanged(@NonNull List<Location> locations) {
        LocationListener.super.onLocationChanged(locations);
    }

    @Override
    public void onFlushComplete(int requestCode) {
        LocationListener.super.onFlushComplete(requestCode);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        LocationListener.super.onStatusChanged(provider, status, extras);
    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {
        LocationListener.super.onProviderEnabled(provider);
    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {
        LocationListener.super.onProviderDisabled(provider);
    }


    //callback para recibir los usuarios
    public interface UserCallback {
        void onUsersReceived(List<Usuario2> users);

        void onFailure(String errorMessage);
    }

    //funcion para llamar a los usuarios cercanos
    public void llamarUsuarios() {
        //comprobamos que la distancia no sea 0
        if (distancia == 0) {
            Toast.makeText(rootView.getContext(), "Introduce una distancia", Toast.LENGTH_LONG).show();
        } else {
            //llamamos a la función que nos devuelve los usuarios

            recibirUsuarios(new UserCallback() {
                @Override
                public void onUsersReceived(List<Usuario2> users) {
                    // Actualizar la lista y el adaptador aquí
                    if (list == null) {
                        list = new ArrayList<>();
                    } else {
                        list.clear();
                    }
                    list.addAll(users);
                    //si hemos recibido algun usuario, creamos el adaptador
                    if (list.size() != 0 && list != null) {
                        SwipeAdapter.UserClickListener userClickListener = new SwipeAdapter.UserClickListener() {
                            @Override
                            public void onSwipeRight(Usuario2 user) {

                            }

                            @Override
                            public void onSwipeLeft(Usuario2 user) {

                            }

                            @Override
                            public void onClickRight(Usuario2 user) {

                            }

                            @Override
                            public void onClickLeft(Usuario2 user) {

                            }

                            @Override
                            public void onLongPress(Usuario2 user) {

                            }
                        };
                        SharedPreferences preferences = rootView.getContext().getSharedPreferences("credenciales", rootView.getContext().MODE_PRIVATE);
                        int idUsu = preferences.getInt("id", 0);
                        swipeAdapter = new SwipeAdapter(rootView.getContext(), list, koloda, userClickListener, idUsu);

                        koloda.setAdapter(swipeAdapter);
                        swipeAdapter.notifyDataSetChanged();
                        progressBar.setVisibility(View.GONE);

                    } else {
                        //si no hemos recibido ningun usuario, mostramos un mensaje
                        Toast.makeText(rootView.getContext(), "No hay usuarios cerca", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(String errorMessage) {
                    Log.d("TAG1", "onFailure: " + errorMessage);
                    progressBar.setVisibility(View.GONE);
                }
            });
        }
    }

    public void recibirUsuarios(UserCallback callback) {
        retrofit = new Retrofit.Builder()
                .baseUrl(Settings.URL2)
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().create()))
                .build();

        doginderAPI = retrofit.create(doginderAPI.class);

        SharedPreferences preferences = rootView.getContext().getSharedPreferences("credenciales", rootView.getContext().MODE_PRIVATE);
        int idUsu = preferences.getInt("id", 0);
        Log.d("pruebaDistancia", "recibirUsuarios: " + idUsu + " " + latitude + " " + longitude + " " + distancia);
        Call<List<Usuario2>> call = doginderAPI.getNearbyUsers(latitude, longitude, distancia, idUsu);
        //Call<List<Usuario2>> call = doginderAPI.getNearbyUsers(41.4983767, 1.8122077, distancia, idUsu);
        //Call<List<UserResponse.Usuario>> call = doginderAPI.getNearbyUsers(latitude, longitude, distancia);

        call.enqueue(new Callback<List<Usuario2>>() {
            @Override
            public void onResponse(Call<List<Usuario2>> call, Response<List<Usuario2>> response) {
                if (response.isSuccessful()) {
                    callback.onUsersReceived(response.body());
                    Log.d("TAG1", "onResponse: " + response.body());
                } else {
                    callback.onFailure("Error en la respuesta: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Usuario2>> call, Throwable t) {
                callback.onFailure("Error en la llamada: " + t.getMessage());
            }
        });

    }

    public void getNo(){
        retrofit = new Retrofit.Builder()
                .baseUrl(Settings.URL2)
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().create()))
                .build();

        doginderAPI = retrofit.create(doginderAPI.class);

        SharedPreferences preferences = rootView.getContext().getSharedPreferences("credenciales", rootView.getContext().MODE_PRIVATE);
        int idUsu = preferences.getInt("id", 0);

        Call<List<Usuario2>> call = doginderAPI.getNo(idUsu);

        call.enqueue(new Callback<List<Usuario2>>() {
            @Override
            public void onResponse(Call<List<Usuario2>> call, Response<List<Usuario2>> response) {
                if (response.isSuccessful()) {
                    Log.d("prueba", "onResponse getNo: " + response.body());
                    AlertDialog.Builder builder = new AlertDialog.Builder(rootView.getContext());
                    builder.setTitle("¿Una segunda oportunidad?");

                    // Crear RecyclerView y configurar el adaptador
                    RecyclerView recyclerView = new RecyclerView(rootView.getContext());
                    recyclerView.setLayoutManager(new LinearLayoutManager(rootView.getContext()));
                    UserNoAdapter adapter = new UserNoAdapter(response.body(), rootView.getContext());
                    recyclerView.setAdapter(adapter);

                    builder.setView(recyclerView);

                    // boton para cerrar el dialogo
                    builder.setPositiveButton("Cerrar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    // Mostrar el diálogo
                    AlertDialog dialog = builder.create();
                    dialog.show();
                } else {
                    Log.d("prueba", "onResponse: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Usuario2>> call, Throwable t) {
                Log.d("prueba", "onFailure: " + t.getMessage());
            }
        });

    }


}
