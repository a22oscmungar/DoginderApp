package com.example.doginder6.Fragments;

import android.Manifest;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
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

import com.example.doginder6.Helpers.LocationHelper;
import com.example.doginder6.Helpers.UserNoAdapter;
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

public class FragmentSwiper extends Fragment {
    public SwipeAdapter swipeAdapter;
    Button btnBuscar, btnFiltro;
    EditText etDistancia;
    private List<Usuario2> list;
    public static Koloda koloda;
    Retrofit retrofit;
    int distancia = 25;
    double latitude;
    double longitude;
    static com.example.doginder6.Helpers.doginderAPI doginderAPI;
    private static final int REQUEST_LOCATION_PERMISSION = 1;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationHelper locationHelper;
    static View rootView;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //iniciamos las variables
        //rootView --> vista que se va a inflar, en este caso es el propio fragmento
        rootView = inflater.inflate(R.layout.fragment_swiper, container, false);
        btnBuscar = rootView.findViewById(R.id.btnBuscar);
        etDistancia = rootView.findViewById(R.id.etDistancia);
        koloda = rootView.findViewById(R.id.koloda);
        btnFiltro = rootView.findViewById(R.id.btnFiltro);

        setHasOptionsMenu(true); // Indica que este fragmento tiene un menú de opciones
        Toolbar toolbar = rootView.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).
        getSupportActionBar().setDisplayShowTitleEnabled(false);


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

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(rootView.getContext());

        // Verificar y solicitar permisos para la localizacion
        if (ContextCompat.checkSelfPermission(rootView.getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            // Si los permisos ya están otorgados, obtener la ubicación
            getLastLocation();
        } else {
            // Si no, solicitar permisos
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION_PERMISSION);
        }

        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                distancia = Integer.parseInt(etDistancia.getText().toString());
                //koloda.reloadAdapterData();
                llamarUsuarios();
            }
        });

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
            default:
                return super.onOptionsItemSelected(item);
        }
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

                    } else {
                        //si no hemos recibido ningun usuario, mostramos un mensaje
                        Toast.makeText(rootView.getContext(), "No hay usuarios cerca", Toast.LENGTH_LONG).show();
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

        SharedPreferences preferences = rootView.getContext().getSharedPreferences("credenciales", rootView.getContext().MODE_PRIVATE);
        int idUsu = preferences.getInt("id", 0);
        //Call<List<Usuario2>> call = doginderAPI.getNearbyUsers(latitude, longitude, distancia, idUsu);
        Call<List<Usuario2>> call = doginderAPI.getNearbyUsers(41.4983767, 1.8122077, distancia, idUsu);
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

    public static void getNo() {
        new AsyncTask<Void, Void, List<Usuario2>>() {
            @Override
            protected List<Usuario2> doInBackground(Void... voids) {
                try {
                    SharedPreferences preferences = rootView.getContext().getSharedPreferences("credenciales", rootView.getContext().MODE_PRIVATE);
                    int idUsu = preferences.getInt("id", 0);

                    // Realizar la llamada a la API en segundo plano
                    Call<List<Usuario2>> call = doginderAPI.getNo(idUsu);
                    Response<List<Usuario2>> response = call.execute();
                    if (response.isSuccessful()) {
                        return response.body();
                    } else {
                        Log.d("prueba", "Error en la respuesta: " + response.message());
                        return null;
                    }
                } catch (IOException e) {
                    Log.d("prueba", "Error en la llamada: " + e.getMessage());
                    return null;
                }
            }

            @Override
            protected void onPostExecute(List<Usuario2> usuarios) {
                super.onPostExecute(usuarios);
                if (usuarios != null) {
                    // Procesar la respuesta en el hilo principal
                    Log.d("prueba", "onResponse getNo: " + usuarios);
                    AlertDialog.Builder builder = new AlertDialog.Builder(rootView.getContext());
                    builder.setTitle("¿Una segunda oportunidad?");

                    // Crear RecyclerView y configurar el adaptador
                    RecyclerView recyclerView = new RecyclerView(rootView.getContext());
                    recyclerView.setLayoutManager(new LinearLayoutManager(rootView.getContext()));
                    UserNoAdapter adapter = new UserNoAdapter(usuarios, rootView.getContext());
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
                }
            }
        }.execute();
    }



    // Método para obtener la última ubicación conocida
    private void getLastLocation() {
        if (ActivityCompat.checkSelfPermission(rootView.getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(rootView.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
                .addOnSuccessListener(requireActivity(), new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            // Aquí obtienes la ubicación del usuario
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                            // Puedes hacer lo que necesites con la ubicación aquí

                            llamarUsuarios();
                        }
                    }
                });
    }


}
