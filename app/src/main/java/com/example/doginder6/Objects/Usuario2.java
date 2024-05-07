package com.example.doginder6.Objects;

import android.os.Parcel;
import android.os.Parcelable;

public class Usuario2 implements Parcelable {
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
    public String tamano;
    public String imgProfile;

    public Usuario2(int idUsu, Ubi ubi, String nombreUsu, String apellidosUsu, String mailUsu, String pass, String genero, int edadUsu, int mascotaId, String nombre, int edad, String sexo, String foto, String descripcion, String relacionHumanos, String relacionMascotas, int idHumano, String raza, String tamano, String imgProfile){
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
        this.tamano = tamano;
        this.imgProfile = imgProfile;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(ubiUsu, flags);
        dest.writeInt(idUsu);
        dest.writeString(nombreUsu);
        dest.writeString(apellidosUsu);
        dest.writeString(mailUsu);
        dest.writeString(pass);
        dest.writeString(genero);
        dest.writeInt(edadUsu);
        dest.writeInt(mascotaId);
        dest.writeString(nombre);
        dest.writeInt(edad);
        dest.writeString(sexo);
        dest.writeString(foto);
        dest.writeString(descripcion);
        dest.writeString(relacionHumanos);
        dest.writeString(relacionMascotas);
        dest.writeInt(idHumano);
        dest.writeString(raza);
        dest.writeString(tamano);
        dest.writeString(imgProfile);
    }

    public static final Parcelable.Creator<Usuario2> CREATOR = new Parcelable.Creator<Usuario2>() {
        @Override
        public Usuario2 createFromParcel(Parcel source) {
            return new Usuario2(source);
        }

        @Override
        public Usuario2[] newArray(int size) {
            return new Usuario2[size];
        }
    };

    private Usuario2(Parcel in) {
        ubiUsu = in.readParcelable(Ubi.class.getClassLoader());
        idUsu = in.readInt();
        nombreUsu = in.readString();
        apellidosUsu = in.readString();
        mailUsu = in.readString();
        pass = in.readString();
        genero = in.readString();
        edadUsu = in.readInt();
        mascotaId = in.readInt();
        nombre = in.readString();
        edad = in.readInt();
        sexo = in.readString();
        foto = in.readString();
        descripcion = in.readString();
        relacionHumanos = in.readString();
        relacionMascotas = in.readString();
        idHumano = in.readInt();
        raza = in.readString();
        tamano = in.readString();
        imgProfile = in.readString();
    }

    public static class Ubi implements Parcelable{
        public double x;
        public double y;

        public Ubi(double x, double y) {
            this.x = x;
            this.y = y;
        }

        private Ubi(Parcel in) {
            x = in.readDouble();
            y = in.readDouble();
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

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeDouble(x);
            dest.writeDouble(y);
        }
        public static final Parcelable.Creator<Ubi> CREATOR = new Parcelable.Creator<Ubi>() {
            @Override
            public Ubi createFromParcel(Parcel in) {
                return new Ubi(in);
            }

            @Override
            public Ubi[] newArray(int size) {
                return new Ubi[size];
            }
        };
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

    public String getTamano() {
        return tamano;
    }

    public String getImgProfile() {
        return imgProfile;
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
                ", imgProfile='" + imgProfile + '\'' +
                '}';
    }
}

