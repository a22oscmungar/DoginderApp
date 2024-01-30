package com.example.doginder6;

public class Usuario2 {
    public Ubi ubiUsu;
    public int idUsu;
    public String nombreUsu;
    public String apellidosUsu;
    public String mailUsu;
    public String pass;
    public String genero;
    public int edadUsu;
    public int mascotaId;
    public String nombre;
    public int edad;
    public String sexo;
    public String foto;
    public String descripcion;
    public String relacionHumanos;
    public String relacionMascotas;
    public int idHumano;
    public String raza;


    /*public Usuario2(Ubi ubiUsu) {
        this.ubiUsu = ubiUsu;
    }*/

    public Usuario2(int idUsu, Ubi ubi, String nombreUsu, String apellidosUsu, String mailUsu, String pass, String genero, int edadUsu, int mascotaId, String nombre, int edad, String sexo, String foto, String descripcion, String relacionHumanos, String relacionMascotas, int idHumano, String raza){
        this.idUsu = idUsu;
        this.ubiUsu = ubi;
        this.nombreUsu = nombreUsu;
        this.apellidosUsu = apellidosUsu;
        this.mailUsu = mailUsu;
        this.pass = pass;
        this.genero = genero;
        this.edadUsu = edadUsu;
        this.mascotaId = mascotaId;
        this.nombre = nombre;
        this.edad = edad;
        this.sexo = sexo;
        this.foto = foto;
        this.descripcion = descripcion;
        this.relacionHumanos = relacionHumanos;
        this.relacionMascotas = relacionMascotas;
        this.idHumano = idHumano;
        this.raza = raza;
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

        @Override
        public String toString() {
            return "Ubi{" +
                    "x=" + x +
                    ", y=" + y +
                    '}';
        }
    }

    public Ubi getUbiUsu() {
        return ubiUsu;
    }

    public int getIdUsu() {
        return idUsu;
    }

    public String getNombreUsu() {
        return nombreUsu;
    }

    public String getApellidosUsu() {
        return apellidosUsu;
    }

    public String getMailUsu() {
        return mailUsu;
    }

    public String getPass() {
        return pass;
    }

    public String getGenero() {
        return genero;
    }

    public int getEdadUsu() {
        return edadUsu;
    }

    public int getMascotaId() {
        return mascotaId;
    }

    public String getNombre() {
        return nombre;
    }

    public int getEdad() {
        return edad;
    }

    public String getSexo() {
        return sexo;
    }

    public String getFoto() {
        return foto;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getRelacionHumanos() {
        return relacionHumanos;
    }

    public String getRelacionMascotas() {
        return relacionMascotas;
    }

    public int getIdHumano() {
        return idHumano;
    }

    public String getRaza() {
        return raza;
    }

    @Override
    public String toString() {
        return "Usuario2{" +
                "idUsu=" + idUsu +
                ", ubiUsu=" + ubiUsu +
                ", nombreUsu='" + nombreUsu + '\'' +
                ", apellidosUsu='" + apellidosUsu + '\'' +
                ", mailUsu='" + mailUsu + '\'' +
                ", pass='" + pass + '\'' +
                ", genero='" + genero + '\'' +
                ", edadUsu=" + edadUsu +
                ", mascotaId=" + mascotaId +
                ", nombre='" + nombre + '\'' +
                ", edad=" + edad +
                ", sexo='" + sexo + '\'' +
                ", foto='" + foto + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", relacionHumanos='" + relacionHumanos + '\'' +
                ", relacionMascotas='" + relacionMascotas + '\'' +
                ", idHumano=" + idHumano +
                ", raza='" + raza + '\'' +
                '}';
    }
}

