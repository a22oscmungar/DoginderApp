package com.example.doginder6.Objects;

public class Mascota {
    public String nombre;
    public int edad;
    public String sexo;
    public String foto;
    public String descripcion;
    public String relacionHumanos;
    public String relacionMascotas;
    public int idHumano;

    public Mascota(String nombre, int edad, String sexo, String foto, String descripcion, String relacionHumanos, String relacionMascotas, int idHumano) {
        this.nombre = nombre;
        this.edad = edad;
        this.sexo = sexo;
        this.foto = foto;
        this.descripcion = descripcion;
        this.relacionHumanos = relacionHumanos;
        this.relacionMascotas = relacionMascotas;
        this.idHumano = idHumano;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getRelacionHumanos() {
        return relacionHumanos;
    }

    public void setRelacionHumanos(String relacionHumanos) {
        this.relacionHumanos = relacionHumanos;
    }

    public String getRelacionMascotas() {
        return relacionMascotas;
    }

    public void setRelacionMascotas(String relacionMascotas) {
        this.relacionMascotas = relacionMascotas;
    }

    public int getIdHumano() {
        return idHumano;
    }

    public void setIdHumano(int idHumano) {
        this.idHumano = idHumano;
    }

    @Override
    public String toString() {
        return "Mascota{" +
                "nombre='" + nombre + '\'' +
                ", edad=" + edad +
                ", sexo='" + sexo + '\'' +
                ", foto='" + foto + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", relacionHumanos='" + relacionHumanos + '\'' +
                ", relacionMascotas='" + relacionMascotas + '\'' +
                ", idHumano=" + idHumano +
                '}';
    }
}
