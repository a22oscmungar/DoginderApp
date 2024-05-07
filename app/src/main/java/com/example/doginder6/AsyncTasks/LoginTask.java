package com.example.doginder6.AsyncTasks;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.doginder6.Activities.LoginActivity;
import com.example.doginder6.Activities.MainActivity;
import com.example.doginder6.Helpers.DataBaseHelper;
import com.example.doginder6.Helpers.doginderAPI;
import com.example.doginder6.Objects.Usuario2;
import com.google.gson.GsonBuilder;
import com.example.doginder6.Objects.UserRequest;


import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginTask extends AsyncTask<Void, Void, Usuario2> {

    private String usuario;
    private String pass;
    private LoginActivity activity;
    public com.example.doginder6.Helpers.doginderAPI doginderAPI;

    public LoginTask(LoginActivity activity, String usuario, String pass) {
        this.activity = activity;
        this.usuario = usuario;
        this.pass = pass;
    }

    @Override
    protected Usuario2 doInBackground(Void... voids) {
        // Aquí realizas el login en segundo plano

        // Crea el objeto Retrofit y llama al método loginUser del API
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://doginder.dam.inspedralbes.cat:3745/")
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().create()))
                .build();

        doginderAPI = retrofit.create(doginderAPI.class);

        UserRequest userRequest = new UserRequest(usuario, pass);

        Call<Usuario2> call = doginderAPI.loginUser(userRequest);
        try {
            Response<Usuario2> response = call.execute();
            if (response.isSuccessful() && response.body() != null) {
                return response.body();
            }
        } catch (IOException e) {
            Log.e("LoginTask", "Error en la ejecución de la llamada", e);
        }

        return null;
    }

    @Override
    protected void onPostExecute(Usuario2 user) {
        super.onPostExecute(user);
        if (user != null) {
            // Si la respuesta es correcta, guardamos el usuario en la base de datos y vamos a la pantalla principal
            DataBaseHelper db = new DataBaseHelper(activity, "MiPerfil", null, 1);
            db.borrarTodosLosDatos();
            db.insertUsu(user);

            SharedPreferences preferences = activity.getSharedPreferences("credenciales", MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt("id", user.getIdUsu());
            editor.apply();


            Intent intent = new Intent(activity, MainActivity.class);
            activity.startActivity(intent);
        } else {
            Toast.makeText(activity, "Algo va mal, revisa los datos", Toast.LENGTH_SHORT).show();
        }
    }
}
