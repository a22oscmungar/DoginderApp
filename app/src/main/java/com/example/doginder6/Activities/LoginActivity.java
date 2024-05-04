package com.example.doginder6.Activities;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentContainerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.doginder6.Helpers.DataBaseHelper;
import com.example.doginder6.Objects.UserRequest;
import com.example.doginder6.Objects.Usuario2;
import com.example.doginder6.R;
import com.example.doginder6.Helpers.doginderAPI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
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
    public com.example.doginder6.Helpers.doginderAPI doginderAPI;
    public Usuario2 user;
    public final String URL = "http://doginder.dam.inspedralbes.cat:3745/";
    public final String URL2 = "http://192.168.19.159:3745/";
    TextView tvORegistrate, tvRecuperarContrasena;
    FragmentContainerView fragmentContainer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        registrarDispositivo();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // inicializamos las variables
        etUsuario = findViewById(R.id.etUsuario);
        etContrasena = findViewById(R.id.etContrasena);
        btnLogin = findViewById(R.id.btnLogin);
        tvORegistrate = findViewById(R.id.tvORegistrate);
        tvRecuperarContrasena = findViewById(R.id.tvRecuperarContrasenya);
        fragmentContainer = findViewById(R.id.fv_recuperarContrasenya);

        String textoCompleto2 = tvRecuperarContrasena.getText().toString();
        SpannableString textoParcial2 = new SpannableString(textoCompleto2);
        ClickableSpan clickableSpan2 = new ClickableSpan() {
            @Override
            public void onClick(android.view.View widget) {
                Intent intent = new Intent(LoginActivity.this, RecoveryPassActivity.class);
                startActivity(intent);
            }
        };

        int color2 = ContextCompat.getColor(this, R.color.naranjaMain);
        ForegroundColorSpan colorSpan2 = new ForegroundColorSpan(color2);
        textoParcial2.setSpan(clickableSpan2, 0, 25, 0);
        textoParcial2.setSpan(colorSpan2, 0, 25, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);

        tvRecuperarContrasena.setText(textoParcial2);
        tvRecuperarContrasena.setMovementMethod(LinkMovementMethod.getInstance());
        tvRecuperarContrasena.setHighlightColor(Color.TRANSPARENT);

        // añadimos el listener al botón
        btnLogin.setOnClickListener(v -> login());

        // editamos el texto para ir al registro
        String textoCompleto = getString(R.string.oRegistrate);
        SpannableString textoParcial = new SpannableString(textoCompleto);
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(android.view.View widget) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        };

        int color = ContextCompat.getColor(this, R.color.naranjaMain);
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(color);
        textoParcial.setSpan(clickableSpan, 2, 17, 0);
        textoParcial.setSpan(colorSpan, 2, 17, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);

        tvORegistrate.setText(textoParcial);
        tvORegistrate.setMovementMethod(LinkMovementMethod.getInstance());
        tvORegistrate.setHighlightColor(Color.TRANSPARENT);


    }

    private void registrarDispositivo(){
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult();
                        Log.d(TAG, "Token: "+ token);
                    }
                });
    }

    // método para hacer login
    public void login(){
        // recogemos los datos de los campos
        String usuario = etUsuario.getText().toString();
        String pass = etContrasena.getText().toString();

        // comprobamos que los campos no estén vacíos
        if(usuario.isEmpty()){
            Toast.makeText(this, "Introduce un nombre de usuario!", Toast.LENGTH_SHORT).show();
        }else if(pass.isEmpty()){
            Toast.makeText(this, "Introduce una contraseña!", Toast.LENGTH_LONG).show();
        }else{
            // creamos el objeto retrofit y llamamos al método loginUser del API
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
                        // si la respuesta es correcta, guardamos el usuario en la base de datos y vamos a la pantalla principal
                        user = response.body();

                        DataBaseHelper db = new DataBaseHelper(LoginActivity.this, "MiPerfil", null, 1);
                        db.borrarTodosLosDatos();
                        db.insertUsu(user);

                        SharedPreferences preferences = getSharedPreferences("credenciales", MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        Log.d("usuario", "login "+ user.getIdUsu());
                        editor.putInt("id", user.getIdUsu());
                        editor.apply();

                        Log.d("pruebaLogin", user.toString());

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