package com.example.doginder6.Activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.res.ResourcesCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.doginder6.Helpers.ChatDatabaseHelper;
import com.example.doginder6.Helpers.Settings;
import com.example.doginder6.Objects.BloquearUsuario;
import com.example.doginder6.Objects.Mensaje;
import com.example.doginder6.Objects.Usuario2;
import com.example.doginder6.R;
import com.example.doginder6.Helpers.SocketListener;
import com.example.doginder6.Helpers.SocketManager;
import com.example.doginder6.Helpers.doginderAPI;
import com.google.gson.GsonBuilder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.socket.emitter.Emitter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ChatActivity extends AppCompatActivity implements SocketListener {

    public SocketManager socket;
    public final String URL = "http://doginder.dam.inspedralbes.cat:3745/";
    public Retrofit retrofit;
    public com.example.doginder6.Helpers.doginderAPI doginderAPI;
    public String socketId;
    public int idUsu1;
    ScrollView svMensajes;
    LinearLayout llContainer;
    private ChatDatabaseHelper dbHelper;

    Usuario2 usuario2 = null;

    public ImageButton btnAtras;
    String motivo = "";
    public static final int NOTIFICATION_ID = 1;
    private static final String CHANNEL_ID = "message_notification_channel";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        dbHelper = new ChatDatabaseHelper(this);
        configurarSocket();


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        

        btnAtras = findViewById(R.id.btnAtras);


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

        tvNombre.setText(usuario2.getNombre().toUpperCase());
        Glide.with(this)
                .load(Settings.URL2 +usuario2.getFoto())  // Reemplaza con tu recurso de imagen
                .apply(RequestOptions.circleCropTransform())
                .into(ivPerro);

        btnEnviar.setOnClickListener(v -> {
            String mensaje = etMensaje.getText().toString();
            Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();

            // Obtener el timestamp actual
            String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());

            dbHelper.insertMessage(idUsu1, usuario2.getIdUsu(), mensaje);
            Log.d("pruebaSocket", "mensaje: " + mensaje + " " + idUsu1 + " " + usuario2.getIdUsu());
            socket.emitMensaje(mensaje, idUsu1, usuario2.getIdUsu());
            agregarMensajeAlScrollView("Yo", mensaje, timestamp);
            etMensaje.setText("");
        });
        mostrarMensajesDelChat();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_chat, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_bloquear) {
            // Acción para el botón Bloquear
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
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
                    dialog.dismiss();
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();

            return true;
        } else if (id == R.id.action_limpiar_chat) {
            // Acción para el botón Limpiar Chat
            limpiarChat();
            return true;
        } else if (id == R.id.action_reportar) {
            // Acción para el botón Reportar
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Confirmar reporte");
            builder.setMessage("¿Estás seguro de que quieres reportar a este usuario?");

// Inflar el layout personalizado
            LayoutInflater inflater = this.getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.report_layout, null);
            builder.setView(dialogView);

// Obtener referencias a los elementos del layout
            ListView listView = dialogView.findViewById(R.id.list_motivos);
            EditText editTextMotivoPersonalizado = dialogView.findViewById(R.id.edittext_motivo_personalizado);

// Configurar el ListView con los motivos
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                    R.array.motivos_reporte, android.R.layout.simple_list_item_multiple_choice);
            listView.setAdapter(adapter);

// Almacenar los motivos seleccionados
            List<String> motivosSeleccionados = new ArrayList<>();
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String motivo = (String) parent.getItemAtPosition(position);
                    if (motivosSeleccionados.contains(motivo)) {
                        motivosSeleccionados.remove(motivo);
                    } else {
                        motivosSeleccionados.add(motivo);
                    }
                }
            });

// Configurar los botones del diálogo
            builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // Obtener el motivo personalizado
                    String motivoPersonalizado = editTextMotivoPersonalizado.getText().toString();
                    if (!motivoPersonalizado.isEmpty()) {
                        motivosSeleccionados.add(motivoPersonalizado);
                    }

                    // Aquí puedes combinar los motivos seleccionados en un solo string, si es necesario
                    motivo = TextUtils.join(", ", motivosSeleccionados);

                    // Lógica para reportar al usuario
                    Toast.makeText(ChatActivity.this, "Usuario reportado por " + motivo, Toast.LENGTH_SHORT).show();
                    reportarUsuario();
                }
            });

            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();
        }

        return super.onOptionsItemSelected(item);
    }

    public void reportarUsuario(){
        retrofit = new Retrofit.Builder()
                .baseUrl(Settings.URLlocal)
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().create()))
                .build();

        doginderAPI = retrofit.create(doginderAPI.class);
        Log.d("pruebaReportar", "idUsu1: " + idUsu1 + " idUsu2: " + usuario2.getIdUsu() + " motivo: " + motivo);
        Call<Void> call = doginderAPI.reportarUsuario(idUsu1, usuario2.getIdUsu(), motivo);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()){
                    Toast.makeText(ChatActivity.this, "Usuario reportado", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ChatActivity.this, MainActivity.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(ChatActivity.this, "Ha habido un error al reportar el usuario", Toast.LENGTH_SHORT).show();
                    Log.d("errorReportar", response.message());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(ChatActivity.this, "Ha habido un error al reportar el usuario", Toast.LENGTH_SHORT).show();
                Log.d("errorReportar", t.getMessage());
            }
        });
    }

    public void bloquearUsuario(){
        retrofit = new Retrofit.Builder()
                .baseUrl(Settings.URL2)
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
                agregarMensajeAlScrollView("Yo", mensaje.getMessage(), mensaje.getTimesStamp());
            } else {
                agregarMensajeAlScrollView("otro", mensaje.getMessage(), mensaje.getTimesStamp());
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
                .baseUrl(Settings.URL2)
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
    private void agregarMensajeAlScrollView(String remitente, String mensaje, String timestamp) {
        if (llContainer != null) {
            try {
                String formattedDate = "";
                String formattedTime = "";

                // Verificar si el timestamp es numérico
                if (timestamp.matches("\\d+")) {
                    long timestampMillis = Long.parseLong(timestamp);
                    Date date = new Date(timestampMillis);
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                    SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
                    formattedDate = dateFormat.format(date);
                    formattedTime = timeFormat.format(date);
                } else {
                    // Asumir que el timestamp está en formato legible
                    String[] dateTimeParts = timestamp.split(" ");
                    if (dateTimeParts.length < 2) {
                        throw new IllegalArgumentException("El formato del timestamp es incorrecto: " + timestamp);
                    }
                    formattedDate = dateTimeParts[0]; // Obtener solo la fecha
                    formattedTime = dateTimeParts[1]; // Obtener solo la hora
                }

                // Crear un TextView para la fecha si es un nuevo día
                if (!lastDate.equals(formattedDate)) {
                    TextView dateView = new TextView(this);
                    dateView.setText(formattedDate);
                    dateView.setTypeface(ResourcesCompat.getFont(this, R.font.poppins_bold));
                    dateView.setTextSize(18);
                    dateView.setGravity(Gravity.CENTER);
                    dateView.setTextColor(getColor(R.color.black));
                    llContainer.addView(dateView);
                    lastDate = formattedDate;
                }

                TextView textView = new TextView(this);
                textView.setText(mensaje);

                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );

                // Ajusta la gravedad del contenedor para posicionar el mensaje a la derecha o izquierda
                if (remitente.equals("Yo")) {
                    layoutParams.gravity = Gravity.END;
                    textView.setBackground(getDrawable(R.drawable.background_mensaje));
                    textView.setTextColor(getColor(R.color.white));
                    textView.setGravity(Gravity.END);
                    textView.setPadding(120, 40, 40, 20);
                } else {
                    layoutParams.gravity = Gravity.START;
                    textView.setBackground(getDrawable(R.drawable.background_mensaje_recibido));
                    textView.setTextColor(getColor(R.color.black));
                    textView.setPadding(40, 40, 120, 40);
                }

                // Añadir hora de envío del mensaje
                TextView timeView = new TextView(this);
                if(remitente.equals("Yo")){
                    timeView.setGravity(Gravity.END);
                }else{
                    timeView.setGravity(Gravity.START);
                }
                timeView.setText(formattedTime);
                timeView.setTextSize(12);
                timeView.setTextColor(getColor(R.color.black));

                // Agregar márgenes alrededor de cada mensaje
                int marginInPixels = 20;
                layoutParams.setMargins(marginInPixels, marginInPixels, marginInPixels, marginInPixels);

                textView.setTextSize(20);
                textView.setPadding(40, 40, 80, 40);
                textView.setLayoutParams(layoutParams);

                // Cambiar la fuente del textView
                textView.setTypeface(ResourcesCompat.getFont(this, R.font.poppins_light));

                // Agregar el TextView al contenedor de mensajes
                llContainer.addView(textView);
                llContainer.addView(timeView);

                // Desplazar la ScrollView hasta el final para mostrar el nuevo mensaje
                svMensajes.post(() -> svMensajes.fullScroll(View.FOCUS_DOWN));
            } catch (IllegalArgumentException e) {
                Log.e("ChatActivity", "Error de formato en timestamp: " + e.getMessage());
            } catch (Exception e) {
                Log.e("ChatActivity", "Error inesperado al agregar mensaje: ", e);
            }
        } else {
            Log.e("ChatActivity", "Error: llContainer es nulo al intentar agregar mensajes.");
        }
    }




    private String lastDate = ""; // Variable para almacenar la última fecha mostrada




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
            String timestamp = (String) args[4]; // Obtener timestamp

            Log.d("pruebaSocket", "nuevoMensaje: " + mensaje + " " + idUsu1 + " " + idUsu2);

            dbHelper.insertMessage(idUsu, idUsu2, mensaje);
            // Ejemplo: mostrar el mensaje en la interfaz de usuario
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String mensajeMostrado = "Usuario " + idUsu + ": " + mensaje ;
                    Log.d("pruebaSocket", mensajeMostrado);
                    agregarMensajeAlScrollView("otro", mensaje, timestamp);
                }
            });

            NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                    .setSmallIcon(R.drawable.notificacion)
                    .setContentTitle("Nuevo mensaje!")
                    .setContentText("Has recibido un nuevo mensaje, entra en la app para verlo!")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);

            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());
            if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            notificationManager.notify(NOTIFICATION_ID, builder.build());

        }
    };


}