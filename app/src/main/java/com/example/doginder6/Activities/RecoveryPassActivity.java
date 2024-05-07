package com.example.doginder6.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.doginder6.R;
import com.example.doginder6.Helpers.doginderAPI;
import com.google.gson.GsonBuilder;

import java.io.IOException;

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

    @SuppressLint("StaticFieldLeak")
    private void enviarMail(String email) {
        new AsyncTask<String, Void, Void>() {
            @Override
            protected Void doInBackground(String... emails) {
                try {
                    String email = emails[0];
                    retrofit = new Retrofit.Builder()
                            .baseUrl(URL)
                            .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().create()))
                            .build();

                    EmailRequest emailRequest = new EmailRequest(email);

                    doginderAPI = retrofit.create(doginderAPI.class);

                    Call<Void> call = doginderAPI.sendMail(emailRequest);
                    call.execute(); // Ejecuta la llamada de forma síncrona para enviar el correo electrónico
                } catch (IOException e) {
                    Log.e("Error", "Error al enviar el email: " + e.getMessage());
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                // Aquí puedes agregar cualquier lógica que necesites después de enviar el correo electrónico,
                // como mostrar un mensaje o cambiar la interfaz de usuario.
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
            }
        }.execute(email);
    }

    @SuppressLint("StaticFieldLeak")
    public void comprobarToken(String tokenIntroducido) {
        new AsyncTask<String, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(String... params) {
                try {
                    String email = params[0];
                    String token = params[1];

                    retrofit = new Retrofit.Builder()
                            .baseUrl(URL)
                            .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().create()))
                            .build();

                    doginderAPI = retrofit.create(doginderAPI.class);

                    Call<Void> call = doginderAPI.checkToken(email, token);
                    Response<Void> response = call.execute(); // Ejecuta la llamada de forma síncrona para comprobar el token

                    return response.isSuccessful();
                } catch (IOException e) {
                    Log.e("Error", "Error al comprobar el token: " + e.getMessage());
                    return false;
                }
            }

            @Override
            protected void onPostExecute(Boolean tokenCorrecto) {
                super.onPostExecute(tokenCorrecto);
                if (tokenCorrecto) {
                    Toast.makeText(RecoveryPassActivity.this, "Token correcto", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RecoveryPassActivity.this, ResetPasswordActivity.class);
                    intent.putExtra("mail", etEmail.getText().toString());
                    startActivity(intent);
                } else {
                    Toast.makeText(RecoveryPassActivity.this, "Token incorrecto", Toast.LENGTH_SHORT).show();
                }
            }
        }.execute(etEmail.getText().toString(), tokenIntroducido);
    }
}