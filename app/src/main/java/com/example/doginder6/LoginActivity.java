package com.example.doginder6;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.bottomappbar.BottomAppBar;
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
    public Usuario user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        BottomAppBar bottomAppBar = findViewById(R.id.bottomAppBar);

        bottomAppBar.setOnMenuItemClickListener(new BottomAppBar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent;
                switch(item.getItemId()){
                    case R.id.registro:
                        intent = new Intent(LoginActivity.this, RegisterActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.login:
                        Toast.makeText(LoginActivity.this, "Ya estas en el login", Toast.LENGTH_SHORT).show();
                    case R.id.main:
                        intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        break;
                }
                return false;
            }
        });

        etUsuario = findViewById(R.id.etUsuario);
        etContrasena = findViewById(R.id.etContrasena);
        btnLogin = findViewById(R.id.btnLogin);

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
                    .baseUrl("http://doginder.dam.inspedralbes.cat:3745/")
                    .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().create()))
                    .build();

            doginderAPI = retrofit.create(doginderAPI.class);

            UserRequest userRequest = new UserRequest(usuario, pass);
            Log.d("prueba", userRequest.getMailUsu() +" "+ userRequest.getPassUsu());

            Call<Usuario> call = doginderAPI.loginUser(userRequest);
            call.enqueue(new Callback<Usuario>() {
                @Override
                public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                    if(response.body() != null){
                        Log.d("prueba", "call exitoso");
                        user = response.body();
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                    }else{
                    Toast.makeText(LoginActivity.this, "Algo va mal, revisa los datos", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Usuario> call, Throwable t) {
                    Log.d("prueba", "onFailure: "+ t.getMessage());
                }
            });
        }
    }
}