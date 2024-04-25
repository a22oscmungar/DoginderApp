package com.example.doginder6.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.doginder6.R;
import com.example.doginder6.Helpers.doginderAPI;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecoveryPassActivity extends AppCompatActivity {

    EditText etEmail;
    public Button btnRecuperar;

    public Retrofit retrofit;
    public com.example.doginder6.Helpers.doginderAPI doginderAPI;
    public final String URL = "http://doginder.dam.inspedralbes.cat:3745/";
    public final String URL2 = "http://192.168.1.140:3745/";

    public String token ="";
    public LinearLayout llEmail, llToken;
    public EditText etToken;
    public Button btnConfirmar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recovery_pass);

        etEmail = findViewById(R.id.etEmail);
        btnRecuperar = findViewById(R.id.btnRecuperar);
        etToken = findViewById(R.id.etToken);
        btnConfirmar = findViewById(R.id.btnConfirmar);
        llEmail = findViewById(R.id.llEmail);
        llToken = findViewById(R.id.llToken);

        btnRecuperar.setOnClickListener(v -> {
            String email = etEmail.getText().toString();
            if (email.isEmpty()) {
                etEmail.setError("Introduce un email");
            } else {
                enviarMail(email);
            }
        });
    }

    public void enviarMail2(String email){
        Intent intent = new Intent(RecoveryPassActivity.this, ResetPasswordActivity.class);
        intent.putExtra("mail", email);
        startActivity(intent);
    }

    // Define el objeto para enviar el correo electrónico
    public class EmailRequest {
        private String mail;

        public EmailRequest(String mail) {
            this.mail = mail;
        }
    }

    private void enviarMail(String email) {
        //aqui haremos la peticion por retrofit a la ruta /sendMail enviando por body el email

        retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().create()))
                .build();

        EmailRequest emailRequest = new EmailRequest(email);


        doginderAPI = retrofit.create(doginderAPI.class);

        Call<Void> call = doginderAPI.sendMail(emailRequest);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    //si la respuesta es correcta mostramos un toast
                    Toast.makeText(RecoveryPassActivity.this, "Email enviado", Toast.LENGTH_SHORT).show();


                    llEmail.setVisibility(View.GONE);
                    llToken.setVisibility(View.VISIBLE);
                    btnConfirmar.setOnClickListener(v -> {
                        String tokenIntroducido = etToken.getText().toString();
                        if (tokenIntroducido.isEmpty()) {
                            etToken.setError("Introduce un token");
                        } else {
                           comprobarToken(tokenIntroducido);
                        }
                    });

                } else {
                    //si la respuesta no es correcta mostramos un toast
                    Toast.makeText(RecoveryPassActivity.this, "Error al enviar el email", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                //si la peticion falla mostramos un toast
                Toast.makeText(RecoveryPassActivity.this, "Error al enviar el email", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void comprobarToken(String tokenIntroducido){
        retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().create()))
                .build();

        doginderAPI = retrofit.create(doginderAPI.class);

        Call<Void> call = doginderAPI.checkToken(etEmail.getText().toString(), tokenIntroducido);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(RecoveryPassActivity.this, "Token correcto", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RecoveryPassActivity.this, ResetPasswordActivity.class);
                    intent.putExtra("mail", etEmail.getText().toString());
                    startActivity(intent);
                } else {
                    Toast.makeText(RecoveryPassActivity.this, "Token incorrecto", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(RecoveryPassActivity.this, "Error al comprobar el token", Toast.LENGTH_SHORT).show();
            }
        });
    }
}