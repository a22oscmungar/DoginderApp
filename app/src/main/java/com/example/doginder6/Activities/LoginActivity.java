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

import com.example.doginder6.AsyncTasks.LoginTask;
import com.example.doginder6.Helpers.DataBaseHelper;
import com.example.doginder6.Helpers.Settings;
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
    TextView tvORegistrate, tvRecuperarContrasena, tvIniciarSesion;
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
        tvIniciarSesion = findViewById(R.id.tvIniciarSesion);

        String textoCompleto2 = tvRecuperarContrasena.getText().toString();
        SpannableString textoParcial2 = new SpannableString(textoCompleto2);

// Todo el texto es clicable
        ClickableSpan clickableSpan2 = new ClickableSpan() {
            @Override
            public void onClick(android.view.View widget) {
                Intent intent = new Intent(LoginActivity.this, RecoveryPassActivity.class);
                startActivity(intent);
            }
        };

        int color2 = ContextCompat.getColor(this, R.color.naranjaMain);
        ForegroundColorSpan colorSpan2 = new ForegroundColorSpan(color2);

// Aplica el span en todo el texto
        textoParcial2.setSpan(clickableSpan2, 0, textoCompleto2.length(), 0);
        textoParcial2.setSpan(colorSpan2, 0, textoCompleto2.length(), SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);

        tvRecuperarContrasena.setText(textoParcial2);
        tvRecuperarContrasena.setMovementMethod(LinkMovementMethod.getInstance());
        tvRecuperarContrasena.setHighlightColor(Color.TRANSPARENT);

        // añadimos el listener al botón
        btnLogin.setOnClickListener(v -> login());

        String textoCompleto = getString(R.string.oRegistrate);
        SpannableString textoParcial = new SpannableString(textoCompleto);

// Encuentra la posición del texto "regístrate aquí" dinámicamente
        String textoRegistrateAqui = getString(R.string.registrateAqui);
        int start = textoCompleto.indexOf(textoRegistrateAqui);
        int end = start + textoRegistrateAqui.length();

        if (start != -1) {  // Verifica que la subcadena exista en el texto completo
            ClickableSpan clickableSpan = new ClickableSpan() {
                @Override
                public void onClick(android.view.View widget) {
                    Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                    startActivity(intent);
                }
            };

            int color = ContextCompat.getColor(this, R.color.naranjaMain);
            ForegroundColorSpan colorSpan = new ForegroundColorSpan(color);

            // Aplica el span solo al texto "regístrate aquí"
            textoParcial.setSpan(clickableSpan, start, end, 0);
            textoParcial.setSpan(colorSpan, start, end, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);

            tvORegistrate.setText(textoParcial);
            tvORegistrate.setMovementMethod(LinkMovementMethod.getInstance());
            tvORegistrate.setHighlightColor(Color.TRANSPARENT);
        }else{
            tvORegistrate.setText(textoCompleto);
        }


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

    public void login() {
        // recogemos los datos de los campos
        String usuario = etUsuario.getText().toString();
        String pass = etContrasena.getText().toString();

        // comprobamos que los campos no estén vacíos
        if (usuario.isEmpty() || pass.isEmpty()) {
            if(usuario.isEmpty()){
                etUsuario.setError(getString(R.string.errorUsuarioVacio));
                Toast.makeText(this, R.string.toastIntroduceNombreUsuario, Toast.LENGTH_SHORT).show();
            }
            if(pass.isEmpty()){
                etContrasena.setError(getString(R.string.errorContrasenaVacia));
                Toast.makeText(this, R.string.toastIntroduceContrasena, Toast.LENGTH_SHORT).show();
            }
        } else {
            // creamos el objeto retrofit y llamamos al método loginUser del API
            retrofit = new Retrofit.Builder()
                    .baseUrl(Settings.URLlocal)
                    .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().create()))
                    .build();

            doginderAPI = retrofit.create(doginderAPI.class);

            UserRequest userRequest = new UserRequest(usuario, pass);

            Call<Usuario2> call = doginderAPI.loginUser(userRequest);
            call.enqueue(new Callback<Usuario2>() {
                @Override
                public void onResponse(Call<Usuario2> call, Response<Usuario2> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        // si la respuesta es correcta, guardamos el usuario en la base de datos y vamos a la pantalla principal
                        user = response.body();

                        DataBaseHelper db = new DataBaseHelper(LoginActivity.this, "MiPerfil", null, 1);
                        db.borrarTodosLosDatos();
                        db.insertUsu(user);

                        // Guardamos las credenciales en SharedPreferences
                        SharedPreferences preferences = getSharedPreferences("credenciales", MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        Log.d("usuario", "login " + user.getIdUsu());
                        editor.putInt("id", user.getIdUsu());
                        editor.putString("nombre", user.getNombre()); // Guarda el nombre del usuario si es necesario
                        editor.putString("token", user.getToken()); // Guarda el token si está disponible
                        editor.apply();

                        Log.d("pruebaLogin", user.toString());

                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish(); // Finaliza la LoginActivity para que no pueda volver al login con el botón de retroceso
                    } else {
                        Toast.makeText(LoginActivity.this, R.string.toastVaMal, Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Usuario2> call, Throwable t) {
                    Log.d("prueba", "onFailure: " + t.getMessage());
                }
            });
        }
    }
}