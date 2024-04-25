package com.example.doginder6;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.doginder6.Objects.ChangePass;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ResetPasswordActivity extends AppCompatActivity {

    public EditText etPass1, etPass2;
    public String pass1, pass2;
    public Button btnResetPass;

    public Retrofit retrofit;
    public doginderAPI doginderAPI;
    public final String URL = "http://doginder.dam.inspedralbes.cat:3745/";
    public final String URL2 = "http://192.168.1.140:3745/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        etPass1 = findViewById(R.id.etPass1);
        etPass2 = findViewById(R.id.etPass2);
        btnResetPass = findViewById(R.id.btnRecuperar);

        btnResetPass.setOnClickListener(v -> {
            pass1 = etPass1.getText().toString();
            pass2 = etPass2.getText().toString();
            if (pass1.isEmpty() || pass2.isEmpty()) {
                Toast.makeText(this, "Introduce una contraseña", Toast.LENGTH_SHORT).show();
            } else if (!pass1.equals(pass2)) {
                Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
            } else {
                cambiarPass(pass1);
            }
        });
    }

    private void cambiarPass(String pass) {
        retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        doginderAPI = retrofit.create(doginderAPI.class);


        ChangePass changePass = new ChangePass(getIntent().getStringExtra("mail"), pass);
        retrofit2.Call<Void> call = doginderAPI.changePass(changePass);

        call.enqueue(new retrofit2.Callback<Void>() {
            @Override
            public void onResponse(retrofit2.Call<Void> call, retrofit2.Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(ResetPasswordActivity.this, "Contraseña cambiada correctamente", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ResetPasswordActivity.this, LoginActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(ResetPasswordActivity.this, "Error al cambiar la contraseña", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<Void> call, Throwable t) {
                Toast.makeText(ResetPasswordActivity.this, "Error al cambiar la contraseña", Toast.LENGTH_SHORT).show();
            }
        });

    }

}