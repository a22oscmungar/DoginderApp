package com.example.doginder6;

public interface SocketListener {
    void onSocketConnected();

    void onNuevoMensaje(String mensaje, int idUsu1, int idUsu2);}
