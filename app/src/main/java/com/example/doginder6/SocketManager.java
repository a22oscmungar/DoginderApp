package com.example.doginder6;


import android.util.Log;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;

public class SocketManager {
    public Socket socket;

    public SocketManager() {
        try {
            socket = IO.socket("http://doginder.dam.inspedralbes.cat:3745");

        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public void connect() {
        socket.connect();
        Log.i("socket", "socket conectado");

    }

    public void disconnect() {
        socket.disconnect();
        Log.i("socket", "socket desconectado");

    }
    public void match(){
        socket.emit("match", "info");
    }
}
