package com.example.doginder6;

public class BloquearUsuario {
    public int usuarioBloqueador, usuarioBloqueado;

    public BloquearUsuario(int usuarioBloqueador, int usuarioBloqueado) {
        this.usuarioBloqueador = usuarioBloqueador;
        this.usuarioBloqueado = usuarioBloqueado;
    }

    public int getUsuarioBloqueador() {
        return usuarioBloqueador;
    }

    public void setUsuarioBloqueador(int usuarioBloqueador) {
        this.usuarioBloqueador = usuarioBloqueador;
    }

    public int getUsuarioBloqueado() {
        return usuarioBloqueado;
    }

    public void setUsuarioBloqueado(int usuarioBloqueado) {
        this.usuarioBloqueado = usuarioBloqueado;
    }

    @Override
    public String toString() {
        return "BloquearUsuario{" +
                "usuarioBloqueador=" + usuarioBloqueador +
                ", usuarioBloqueado=" + usuarioBloqueado +
                '}';
    }
}
