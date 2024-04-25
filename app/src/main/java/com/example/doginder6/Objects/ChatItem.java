package com.example.doginder6.Objects;

public class ChatItem {
    private String nombreUsuario;
    private String fotoPerfil;  // Puedes usar el tipo de dato que necesites para la foto
    // Puedes agregar más campos según tus necesidades

    public ChatItem(String nombreUsuario, String fotoPerfil) {
        this.nombreUsuario = nombreUsuario;
        this.fotoPerfil = fotoPerfil;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public String getFotoPerfil() {
        return fotoPerfil;
    }
    // Puedes agregar más métodos y campos según tus necesidades
}

