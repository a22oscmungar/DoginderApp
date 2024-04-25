package com.example.doginder6;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.doginder6.Objects.BloquearUsuario;
import com.example.doginder6.Objects.Mensaje;
import com.google.gson.GsonBuilder;

import java.util.List;

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
    private ChatDatabaseHelper dbHelper;

    Usuario2 usuario2 = null;

    public ImageButton btnAtras, btnBorrar, btnBloquear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        dbHelper = new ChatDatabaseHelper(this);
        configurarSocket();

        btnAtras = findViewById(R.id.btnAtras);
        btnBorrar = findViewById(R.id.btnBorrar);
        btnBloquear = findViewById(R.id.btnBloquear);

        btnBorrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                limpiarChat();
            }
        });

        btnAtras.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("Tab", "Chat"); // Puedes pasar un extra para indicar que quieres abrir la pestaña de chat directamente
            startActivity(intent);
        });


        SharedPreferences preferences = getSharedPreferences("credenciales", MODE_PRIVATE);
        idUsu1 = preferences.getInt("id", 0);

        Intent intent = getIntent();
        usuario2 = intent.getParcelableExtra("usuario");

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
            dbHelper.insertMessage(idUsu1, usuario2.getIdUsu(), mensaje);
            Log.d("pruebaSocket", "mensaje: " + mensaje + " " + idUsu1 + " " + usuario2.getIdUsu());
            socket.emitMensaje(mensaje, idUsu1, usuario2.getIdUsu());
            agregarMensajeAlScrollView("Yo", mensaje);
            //agregarMensajeAlScrollView("otro", "okey");
            etMensaje.setText("");

        });
        mostrarMensajesDelChat();
        Context context = this;

        btnBloquear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Confirmar bloqueo");
                builder.setMessage("¿Estás seguro de que quieres bloquear a este usuario?");

                builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        bloquearUsuario();
                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // No hacer nada, simplemente cerrar el diálogo
                        dialog.dismiss();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    public void bloquearUsuario(){
        retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().create()))
                .build();

        doginderAPI = retrofit.create(doginderAPI.class);

        BloquearUsuario bloquearUsuario = new BloquearUsuario(idUsu1, usuario2.getIdUsu());
        Call<Void> call = doginderAPI.bloquearUsuario(bloquearUsuario);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()){
                    Toast.makeText(ChatActivity.this, "Usuario bloqueado", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ChatActivity.this, MainActivity.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(ChatActivity.this, "Ha habido un error al bloquear el usuario", Toast.LENGTH_SHORT).show();
                    Log.d("errorBloquear", response.message());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(ChatActivity.this, "Ha habido un error al bloquear el usuario", Toast.LENGTH_SHORT).show();
                Log.d("errorBloquear", t.getMessage());
            }
        });
    }

    private void mostrarMensajesDelChat() {
        List<Mensaje> mensajes = dbHelper.getAllMessages(idUsu1, usuario2.getIdUsu());

        for (Mensaje mensaje : mensajes) {
            if(mensaje.getSenderId() == idUsu1){
                agregarMensajeAlScrollView("Yo", mensaje.getMessage());}
            else{
                agregarMensajeAlScrollView("otro", mensaje.getMessage());
            }
        }
    }

    private void limpiarChat() {
        dbHelper.limpiarChat(idUsu1, usuario2.getIdUsu());
        // Limpia también la interfaz de usuario si es necesario
        llContainer.removeAllViews();
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
        if (llContainer != null) {
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
        } else {
            Log.e("ChatActivity", "Error: llContainer es nulo al intentar agregar mensajes.");
        }
    }



    @Override
    public void onSocketConnected() {
        updateSocket();
    }

    private Emitter.Listener nuevoMensaje = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            // Tu lógica para manejar el nuevo mensaje aquí
            String mensaje = (String) args[0];
            String idUsu1 = String.valueOf(args[1]); // Convertir a String
            int idUsu2 = (int) (args[2]); // Convertir a String
            int idUsu = (int) (args[3]); // Convertir a String
            Log.d("pruebaSocket", "nuevoMensaje: " + mensaje + " " + idUsu1 + " " + idUsu2);

            dbHelper.insertMessage(idUsu, idUsu2, mensaje);
            // Ejemplo: mostrar el mensaje en la interfaz de usuario
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String mensajeMostrado = "Usuario " + idUsu + ": " + mensaje ;
                    Log.d("pruebaSocket", mensajeMostrado);
                    agregarMensajeAlScrollView("otro", mensaje);
                }
            });
        }
    };

}