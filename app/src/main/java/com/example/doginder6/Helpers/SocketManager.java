package com.example.doginder6.Helpers;


import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.doginder6.Activities.MainActivity;
import com.example.doginder6.R;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class SocketManager {
    public Socket socket;

    private SharedPreferences sharedPreferences;
    private static final String SOCKET_ID_KEY = "socketId";
    private List<SocketListener> socketListeners = new ArrayList<>();
    private List<MatchListener> matchListeners = new ArrayList<>();
    public Context context;

    private static final String CHANNEL_ID = "match_notification_channel";
    public static final String CHANNEL_NAME = "Match Notification";
    public static final String CHANNEL_DESCRIPTION = "Notificación de match";
    public static final int NOTIFICATION_ID = 0;

    public SocketManager(Context context) {
        this.context = context;
        try {
            sharedPreferences = context.getSharedPreferences("credentials", Context.MODE_PRIVATE);
            String storedSocketId = sharedPreferences.getString(SOCKET_ID_KEY, null);

            // Construir la URL del servidor con el socketId si está disponible
            String serverUrl = Settings.URL2;
            if (storedSocketId != null) {
                serverUrl += "?socketId=" + storedSocketId;
            }

            // Inicializar el socket con la URL construida
            socket = IO.socket(serverUrl);

            // Evento de conexión del socket
            socket.on(Socket.EVENT_CONNECT, args -> {
                String receivedSocketId = socket.id();

                // Guardar el socketId en SharedPreferences
                sharedPreferences.edit().putString(SOCKET_ID_KEY, receivedSocketId).apply();

                // Log para verificar que se ha recibido el socketId correctamente
                Log.d("pruebaSocket", "updateSocket: " + receivedSocketId);
            });

        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public void addSocketListener(SocketListener listener) {
        socketListeners.add(listener);
    }

    private void notifySocketConnected() {
        for (SocketListener listener : socketListeners) {
            listener.onSocketConnected();
        }
    }

    public void connect() {
        socket.connect();

        // Guardar el socketId recibido desde el servidor en SharedPreferences
        socket.on(Socket.EVENT_CONNECT, args -> {
            String receivedSocketId = socket.id();
            sharedPreferences.edit().putString(SOCKET_ID_KEY, receivedSocketId).apply();
            notifySocketConnected();
            Log.d("pruebaSocket", "updateSocket: " + receivedSocketId);
        });
    }

    public void disconnect() {
        socket.disconnect();
        Log.i("socket", "socket desconectado");

    }
    public void match(){
        socket.emit("match", "info");
    }

    public String getSocketId() {
        return socket.id();
    }

    public void emitMensaje(String mensaje, int idUsu, int idUsu2){
        Log.d("pruebaSocket", "emitMensaje: " + mensaje +" " + socket.id() + " " + idUsu2);
        socket.emit("nuevoMensaje", mensaje, socket.id(), idUsu2, idUsu);
    }

    /*private Emitter.Listener nuevoMensaje = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            // Tu lógica para manejar el nuevo mensaje aquí
            JSONObject data = (JSONObject) args[0];
            String username;
            String message;
            try {
                username = data.getString("username");
                message = data.getString("message");
            } catch (JSONException e) {
                return;
            }
        }
    };*/

    public void on(String event, Emitter.Listener listener) {
        socket.on(event, listener);
    }

    public void addMatchListener(MatchListener listener) {
        matchListeners.add(listener);
    }

    public void notifyMatch() {
        for (MatchListener listener : matchListeners) {
            listener.onMatch();

            Intent intent = new Intent(context, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("Tab", "Chat"); // Puedes pasar un extra para indicar que quieres abrir la pestaña de chat directamente
            //context.startActivity(intent);
            PendingIntent pendingIntent = PendingIntent.getActivity(context.getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context.getApplicationContext(), CHANNEL_ID)
                    .setSmallIcon(R.drawable.notificacion)
                    .setContentTitle("Nuevo match!")
                    .setContentText("Has hecho match con alguien!")
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);

            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context.getApplicationContext());
            if (ActivityCompat.checkSelfPermission(context.getApplicationContext(), Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
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
    }
}

