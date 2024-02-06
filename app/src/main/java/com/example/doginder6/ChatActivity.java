package com.example.doginder6;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

public class ChatActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Intent intent = getIntent();
        Usuario2 usuario2 = intent.getParcelableExtra("usuario");

        TextView tvNombre = findViewById(R.id.tvNombrePerro);
        ImageView ivPerro = findViewById(R.id.ivFoto);
        EditText etMensaje = findViewById(R.id.etMensaje);
        Button btnEnviar = findViewById(R.id.btnEnviar);

        tvNombre.setText(usuario2.getNombre());
        Log.d("pruebaFoto", usuario2.getNombre());
        Glide.with(this)
                .load("http://doginder.dam.inspedralbes.cat:3745"+usuario2.getFoto())  // Reemplaza con tu recurso de imagen
                .apply(RequestOptions.circleCropTransform())
                .into(ivPerro);

    }
}