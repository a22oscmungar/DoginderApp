package com.example.doginder6;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {
    public EditText etUsuario, etContrasena;
    public Button btnLogin;
    public Retrofit retrofit;
    public doginderAPI doginderAPI;
    public Usuario2 user;
    public final String URL = "http://doginder.dam.inspedralbes.cat:3745/";
    public final String URL2 = "http://192.168.19.159:3745/";
    Button btnIrRegistro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUsuario = findViewById(R.id.etUsuario);
        etContrasena = findViewById(R.id.etContrasena);
        btnLogin = findViewById(R.id.btnLogin);
        btnIrRegistro = findViewById(R.id.btnIrRegistro);
        btnIrRegistro.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

        btnLogin.setOnClickListener(v -> login());
    }

    public void login(){
        String usuario = etUsuario.getText().toString();
        String pass = etContrasena.getText().toString();

        if(usuario.isEmpty()){
            Toast.makeText(this, "Introduce un nombre de usuario!", Toast.LENGTH_SHORT).show();
        }else if(pass.isEmpty()){
            Toast.makeText(this, "Introduce una contraseña!", Toast.LENGTH_LONG).show();
        }else{
            retrofit = new Retrofit.Builder()
                    .baseUrl(URL)
                    .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().create()))
                    .build();

            doginderAPI = retrofit.create(doginderAPI.class);

            UserRequest userRequest = new UserRequest(usuario, pass);
            Log.d("prueba", userRequest.getMailUsu() +" "+ userRequest.getPassUsu());

            Call<Usuario2> call = doginderAPI.loginUser(userRequest);
            call.enqueue(new Callback<Usuario2>() {
                @Override
                public void onResponse(Call<Usuario2> call, Response<Usuario2> response) {
                    if(response.isSuccessful() && response.body() != null){
                        Log.d("prueba", "call exitoso");
                        //Log.d("prueba", "onResponse response: "+ response.body());

                        user = response.body();
                        /*Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);*/
                        Log.d("prueba", "onResponse user: "+ user.toString());

                        DataBaseHelper db = new DataBaseHelper(LoginActivity.this, "MiPerfil", null, 1);
                        db.borrarTodosLosDatos();
                        db.insertUsu(user);

                        SharedPreferences preferences = getSharedPreferences("credenciales", MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        Log.d("usuario", "login "+ user.getIdUsu());
                        editor.putInt("id", user.getIdUsu());
                        editor.apply();

                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                    }else{
                    Toast.makeText(LoginActivity.this, "Algo va mal, revisa los datos", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Usuario2> call, Throwable t) {
                    Log.d("prueba", "onFailure: "+ t.getMessage());
                }
            });
        }
    }
}