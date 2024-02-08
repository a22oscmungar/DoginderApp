package com.example.doginder6;


import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

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

    public SocketManager(Context context) {
        try {
            sharedPreferences = context.getSharedPreferences("credentials", Context.MODE_PRIVATE);
            String storedSocketId = sharedPreferences.getString(SOCKET_ID_KEY, null);

            // Construir la URL del servidor con el socketId si está disponible
            String serverUrl = "http://doginder.dam.inspedralbes.cat:3745";
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
        socket.emit("nuevoMensaje", mensaje, socket.id(), idUsu2);
    }

    private Emitter.Listener nuevoMensaje = new Emitter.Listener() {
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

            // Ejemplo: mostrar el mensaje en la interfaz de usuario
            //addMessage(username, message);
        }
    };

    public void on(String event, Emitter.Listener listener) {
        socket.on(event, listener);
    }

}

