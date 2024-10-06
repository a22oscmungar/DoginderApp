package com.example.doginder6.Fragments;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doginder6.Helpers.ChatAdapter;
import com.example.doginder6.Helpers.Settings;
import com.example.doginder6.Helpers.UsersGroupAdapter;
import com.example.doginder6.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FragmentMasFunciones extends Fragment {
    View rootView;
    Button btnPasear, btnSocial;

    Retrofit retrofit;
    com.example.doginder6.Helpers.doginderAPI doginderAPI;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_mas_funciones, container, false);

        btnPasear = rootView.findViewById(R.id.btnPasear);
        btnSocial = rootView.findViewById(R.id.btnSocial);

        btnPasear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // se muestra un fragment con un texto y un boton para cerrar

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Funcion de paseo");
                builder.setMessage("Quizá se te complique pasear a tu mascota y no te juzgamos, o quizá quieras dedicarte a pasear las mascotas de otros y nos parece una gran idea. Presiona el boton de 'Estoy interesado' para hacernoslo saber");
                // boton con otra funcion
                builder.setPositiveButton("Estoy interesado!", (dialog, which) -> {
                    avisarPaseo();
                });
                // boton para cerrar
                builder.setNegativeButton("Cerrar", (dialog, which) -> {
                    dialog.dismiss();
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        btnSocial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // se muestra un fragment con un texto y un boton para cerrar

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Funcion social");
                builder.setMessage("Tu mascota puede ayudar a alguien más que a ti mismo, si te gustaria que tu mascota le alegre el dia a gente con discapacidades físicales/mentales o gente mayor, o te gustaria que una mascota de haga compañía, pulsa el boton de 'Estoy interesado' para hacernoslo saber");
                // boton con otra funcion
                builder.setPositiveButton("Estoy interesado!", (dialog, which) -> {
                    social();
                });
                // boton para cerrar
                builder.setNegativeButton("Cerrar", (dialog, which) -> {
                    dialog.dismiss();
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        return rootView;
    }

    public void avisarPaseo() {
        retrofit = new Retrofit.Builder()
                .baseUrl(Settings.URLlocal)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        doginderAPI = retrofit.create(com.example.doginder6.Helpers.doginderAPI.class);

        SharedPreferences preferences = rootView.getContext().getSharedPreferences("credenciales", rootView.getContext().MODE_PRIVATE);
        int idUsu = preferences.getInt("id", 0);
        Log.d("idUsuMail", String.valueOf(idUsu));
        // aqui se haria la llamada a la api para avisar de que se esta interesado en pasear
        Call<Void> call = doginderAPI.pasear(idUsu);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(rootView.getContext(), "Gracias por mostrar tu interés!", Toast.LENGTH_SHORT).show();
                } else {
                   mostrarDialogError();
                    // Toast.makeText(rootView.getContext(), "Error al avisar de que estás interesado en pasear", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                mostrarDialogError();
                //Toast.makeText(rootView.getContext(), "Error al avisar de que estás interesado en pasear", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void social() {
        retrofit = new Retrofit.Builder()
                .baseUrl(Settings.URLlocal)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        doginderAPI = retrofit.create(com.example.doginder6.Helpers.doginderAPI.class);

        SharedPreferences preferences = rootView.getContext().getSharedPreferences("credenciales", rootView.getContext().MODE_PRIVATE);
        int idUsu = preferences.getInt("id", 0);
        Log.d("idUsuMail", String.valueOf(idUsu));
        // aqui se haria la llamada a la api para avisar de que se esta interesado en pasear
        Call<Void> call = doginderAPI.social(idUsu);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                /*if (response.isSuccessful()) {
                    Toast.makeText(rootView.getContext(), "Gracias por mostrar tu interés!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(rootView.getContext(), "Error al avisar de que estás interesado en pasear", Toast.LENGTH_SHORT).show();
                }*/
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                mostrarDialogError();
                //Toast.makeText(rootView.getContext(), "Error al avisar de que estás interesado en pasear", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void mostrarDialogError(){
        AlertDialog.Builder builder = new AlertDialog.Builder(rootView.getContext());
        builder.setTitle(R.string.errorTitulo);
        builder.setMessage(R.string.errorMensaje);
        builder.setPositiveButton(R.string.builderEntendido, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
