package com.example.doginder6;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import io.socket.emitter.Emitter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ChatActivity extends AppCompatActivity implements SocketListener{

    public SocketManager socket;
    public final String URL = "http://doginder.dam.inspedralbes.cat:3745/";
    public Retrofit retrofit;
    public doginderAPI doginderAPI;
    public String socketId;
    public int idUsu1;
    ScrollView svMensajes;
    LinearLayout llContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        configurarSocket();

        SharedPreferences preferences = getSharedPreferences("credenciales", MODE_PRIVATE);
        idUsu1 = preferences.getInt("id", 0);

        Intent intent = getIntent();
        Usuario2 usuario2 = intent.getParcelableExtra("usuario");

        TextView tvNombre = findViewById(R.id.tvNombrePerro);
        ImageView ivPerro = findViewById(R.id.ivFoto);
        EditText etMensaje = findViewById(R.id.etMensaje);
        Button btnEnviar = findViewById(R.id.btnEnviar);
        svMensajes = findViewById(R.id.svMensajes);
        llContainer = findViewById(R.id.llContainer);

        tvNombre.setText(usuario2.getNombre());
        Glide.with(this)
                .load("http://doginder.dam.inspedralbes.cat:3745"+usuario2.getFoto())  // Reemplaza con tu recurso de imagen
                .apply(RequestOptions.circleCropTransform())
                .into(ivPerro);

        btnEnviar.setOnClickListener(v -> {
            String mensaje = etMensaje.getText().toString();
            Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();

            socket.emitMensaje(mensaje, idUsu1, usuario2.getIdUsu());
            agregarMensajeAlScrollView("Yo", mensaje);
            //agregarMensajeAlScrollView("otro", "okey");
            etMensaje.setText("");

        });
    }

    private void configurarSocket() {
        socket = new SocketManager(this);

        // Agrega la actividad como escuchador del socket
        socket.addSocketListener(this);

        // Conecta el socket
        socket.connect();

        socket.on("nuevoMensaje", nuevoMensaje);
    }

    public void updateSocket(){
        Log.d("socket", "updateSocket funcion");
        SharedPreferences preferences = getSharedPreferences("credenciales", MODE_PRIVATE);
        int id = preferences.getInt("id", 0);

        socketId = socket.getSocketId();
        Log.d("pruebaSocket", "updateSocket funcion: " + socketId);

        retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().create()))
                .build();

        doginderAPI = retrofit.create(doginderAPI.class);
        Call<Void> call = doginderAPI.socketUpdate(id, socketId);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()){
                    Log.d("socket", "socket actualizado");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d("socket", "onFailure: "+ t.getMessage());
            }
        });
    }

    private void agregarMensajeAlScrollView(String remitente, String mensaje) {
        TextView textView = new TextView(this);
        textView.setText(mensaje);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );

        // Ajusta la gravedad del contenedor para posicionar el mensaje a la derecha o izquierda
        if (remitente.equals("Yo")) {
            layoutParams.gravity = Gravity.END;
        } else {
            layoutParams.gravity = Gravity.START;
        }

        // Agrega márgenes alrededor de cada mensaje
        int marginInPixels = 20;
        layoutParams.setMargins(marginInPixels, marginInPixels, marginInPixels, marginInPixels);

        textView.setTextSize(20);
        textView.setBackground(getDrawable(R.drawable.background_mensaje));
        textView.setPadding(20, 20, 20, 20);
        textView.setTextColor(getColor(R.color.white));
        textView.setLayoutParams(layoutParams);

        // Agrega el TextView al contenedor de mensajes
        llContainer.addView(textView);

        // Desplaza la ScrollView hasta el final para mostrar el nuevo mensaje
        svMensajes.post(() -> svMensajes.fullScroll(View.FOCUS_DOWN));
    }


    @Override
    public void onSocketConnected() {
        Log.d("pruebaSocket", "socket conectado");

        updateSocket();
    }

    @Override
    public void onNuevoMensaje(String mensaje, int idUsu1, int idUsu2) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.d("pruebaSocket", "llega el mensaje");
                // Aquí puedes actualizar tu interfaz de usuario con el mensaje recibido
                String mensajeMostrado = "Usuario " + idUsu1 + ": " + mensaje;
                agregarMensajeAlScrollView("otro", mensajeMostrado);
            }
        });
    }

    private Emitter.Listener nuevoMensaje = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            // Tu lógica para manejar el nuevo mensaje aquí
            String mensaje = (String) args[0];
            String idUsu1 = String.valueOf(args[1]); // Convertir a String
            int idUsu2 = (int) (args[2]); // Convertir a String

            // Ejemplo: mostrar el mensaje en la interfaz de usuario
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.d("pruebaSocket", "llega el mensaje");

                    String mensajeMostrado = "Usuario " + idUsu1 + ": " + mensaje;
                    Log.d("pruebaSocket", mensajeMostrado);
                    agregarMensajeAlScrollView("otro", mensaje);
                }
            });
        }
    };

}