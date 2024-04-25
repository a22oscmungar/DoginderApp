package com.example.doginder6;

import com.google.gson.annotations.SerializedName;

public class Usuario {
    @SerializedName("idUsu")
    public int idUsu;
    @SerializedName("nombreUsu")
    public String nombreUsu;
    @SerializedName("ubiUsu")
    public Ubi ubiUsu;
    @SerializedName("apellidosUsu")
    public String apellidosUsu;
    @SerializedName("mailUsu")
    public String mailUsu;
    @SerializedName("pass")
    public String pass;
    @SerializedName("genero")
    public String genero;
    @SerializedName("edadUsu")
    public int edadUsu;

    @SerializedName("mascotaId")
    public int mascotaId;
    @SerializedName("nombre")
    public String nombre;
    @SerializedName("edad")
    public String edad;
    @SerializedName("sexo")
    public String sexo;
    @SerializedName("foto")
    public String foto;
    @SerializedName("descripcion")
    public String descripcion;
    @SerializedName("relacionHumanos")
    public String relacionHumanos;
    @SerializedName("relacionMascotas")
    public String relacionMascotas;
    @SerializedName("raza")
    public String raza;
    @SerializedName("idHumano")
    public int idHumano;


    public Usuario(int idUsu, String nombreUsu, Ubi ubiUsu, String apellidosUsu, String mailUsu, String pass, int edadUsu, String genero, int mascotaId, String nombre, String edad, String sexo, String descripcion, String foto, String raza, String relacionMascotas, String relacionHumanos, int idHumano) {
        this.idUsu = idUsu;
        this.nombreUsu = nombreUsu;
        this.ubiUsu = ubiUsu;
        this.apellidosUsu = apellidosUsu;
        this.mailUsu = mailUsu;
        this.pass = pass;
        this.edadUsu = edadUsu;
        this.genero = genero;
        this.mascotaId = mascotaId;
        this.nombre = nombre;
        this.edad = edad;
        this.sexo = sexo;
        this.descripcion = descripcion;
        this.foto = foto;
        this.raza = raza;
        this.relacionMascotas = relacionMascotas;
        this.relacionHumanos = relacionHumanos;
        this.idHumano = idHumano;
    }

    public int getIdUsu() {
        return idUsu;
    }

    public void setIdUsu(int idUsu) {
        this.idUsu = idUsu;
    }

    public String getNombreUsu() {
        return nombreUsu;
    }

    public void setNombreUsu(String nombreUsu) {
        this.nombreUsu = nombreUsu;
    }

    public Ubi getUbi() {
        return ubiUsu;
    }

    public void setUbi(Ubi ubi) {
        this.ubiUsu = ubi;
    }

    public String getApellidosUsu() {
        return apellidosUsu;
    }

    public void setApellidosUsu(String apellidosUsu) {
        this.apellidosUsu = apellidosUsu;
    }

    public String getMailUsu() {
        return mailUsu;
    }

    public void setMailUsu(String mailUsu) {
        this.mailUsu = mailUsu;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public class Ubi{
        public double x;
        public double y;

        public Ubi(double x, double y) {
            this.x = x;
            this.y = y;
        }

        public double getX() {
            return x;
        }

        public void setX(double x) {
            this.x = x;
        }

        public double getY() {
            return y;
        }

        public void setY(double y) {
            this.y = y;
        }


    }

    public Ubi getUbiUsu() {
        return ubiUsu;
    }

    public void setUbiUsu(Ubi ubiUsu) {
        this.ubiUsu = ubiUsu;
    }

    public int getEdadUsu() {
        return edadUsu;
    }

    public void setEdadUsu(int edadUsu) {
        this.edadUsu = edadUsu;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public int getMascotaId() {
        return mascotaId;
    }

    public void setMascotaId(int mascotaId) {
        this.mascotaId = mascotaId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEdad() {
        return edad;
    }

    public void setEdad(String edad) {
        this.edad = edad;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getRaza() {
        return raza;
    }

    public void setRaza(String raza) {
        this.raza = raza;
    }

    public String getRelacionMascotas() {
        return relacionMascotas;
    }

    public void setRelacionMascotas(String relacionMascotas) {
        this.relacionMascotas = relacionMascotas;
    }

    public String getRelacionHumanos() {
        return relacionHumanos;
    }

    public void setRelacionHumanos(String relacionHumanos) {
        this.relacionHumanos = relacionHumanos;
    }

    public int getIdHumano() {
        return idHumano;
    }

    public void setIdHumano(int idHumano) {
        this.idHumano = idHumano;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "idUsu=" + idUsu +
                ", nombreUsu='" + nombreUsu + '\'' +
                ", ubiUsu=" + ubiUsu +
                ", apellidosUsu='" + apellidosUsu + '\'' +
                ", mailUsu='" + mailUsu + '\'' +
                ", pass='" + pass + '\'' +
                ", edadUsu='" + edadUsu + '\'' +
                ", genero='" + genero + '\'' +
                ", mascotaId=" + mascotaId +
                ", nombre='" + nombre + '\'' +
                ", edad='" + edad + '\'' +
                ", sexo='" + sexo + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", foto='" + foto + '\'' +
                ", raza='" + raza + '\'' +
                ", relacionMascotas='" + relacionMascotas + '\'' +
                ", relacionHumanos='" + relacionHumanos + '\'' +
                ", idHumano=" + idHumano +
                '}';
    }
}

