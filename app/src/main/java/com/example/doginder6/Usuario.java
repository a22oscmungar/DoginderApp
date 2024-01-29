package com.example.doginder6;

public class Usuario {
    public int idUsu;
    public String nombreUsu;
    public Ubi ubi;
    public String apellidosUsu;
    public String mailUsu;
    public String imageURL;
    public String pass;

    public Usuario(int idUsu, String nombreUsu, Ubi ubi, String apellidosUsu, String mailUsu, String imageURL, String pass ){
        this.idUsu = idUsu;
        this.nombreUsu = nombreUsu;
        this.ubi = ubi;
        this.apellidosUsu = apellidosUsu;
        this.mailUsu = mailUsu;
        this.imageURL = imageURL;
        this.pass = pass;
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
        return ubi;
    }

    public void setUbi(Ubi ubi) {
        this.ubi = ubi;
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

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
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

    @Override
    public String toString() {
        return "Usuario{" +
                "idUsu=" + idUsu +
                ", nombreUsu='" + nombreUsu + '\'' +
                ", ubi=" + ubi +
                ", apellidosUsu='" + apellidosUsu + '\'' +
                ", mailUsu='" + mailUsu + '\'' +
                ", imageURL='" + imageURL + '\'' +
                ", pass='" + pass + '\'' +
                '}';
    }
}

