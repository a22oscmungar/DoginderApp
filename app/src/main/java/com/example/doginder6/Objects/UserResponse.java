package com.example.doginder6.Objects;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UserResponse {
    public List<Usuario> usuarios;

    public UserResponse(List<Usuario> usuarios) {
        this.usuarios = usuarios;
    }

    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    public class Usuario {
        @SerializedName("idUsu")
        private int idUsu;
        @SerializedName("nombreUsu")
        private String nombreUsu;
        @SerializedName("latitude")
        private double latitude;
        @SerializedName("longitude")
        private double longitude;
        @SerializedName("distance")
        private double distance;

        //datos de la mascota
        @SerializedName("nombreMascota")
        private String nombreMascota;
        @SerializedName("edadMascota")
        private int edadMascota;
        @SerializedName("sexoMascota")
        private String sexoMascota;
        @SerializedName("fotoMascota")
        private String fotoMascota;
        @SerializedName("descripcionMascota")
        private String descripcionMascota;
        @SerializedName("relacionHumanosMascota")
        private String relacionHumanosMascota;
        @SerializedName("relacionMascotasMascota")
        private String relacionMascotasMascota;

        public Usuario(int idUsu, String nombreUsu, double latitude, double longitude, double distance, String imageUrl) {
            this.idUsu = idUsu;
            this.nombreUsu = nombreUsu;
            this.latitude = latitude;
            this.longitude = longitude;
            this.distance = distance;
        }

        // Agrega getters y setters según sea necesario

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

        public double getLatitude() {
            return latitude;
        }

        public void setLatitude(double latitude) {
            this.latitude = latitude;
        }

        public double getLongitude() {
            return longitude;
        }

        public void setLongitude(double longitude) {
            this.longitude = longitude;
        }

        public double getDistance() {
            return distance;
        }

        public void setDistance(double distance) {
            this.distance = distance;
        }

        public String getNombreMascota() {
            return nombreMascota;
        }

        public void setNombreMascota(String nombreMascota) {
            this.nombreMascota = nombreMascota;
        }

        public int getEdadMascota() {
            return edadMascota;
        }

        public void setEdadMascota(int edadMascota) {
            this.edadMascota = edadMascota;
        }

        public String getSexoMascota() {
            return sexoMascota;
        }

        public void setSexoMascota(String sexoMascota) {
            this.sexoMascota = sexoMascota;
        }

        public String getFotoMascota() {
            return fotoMascota;
        }

        public void setFotoMascota(String fotoMascota) {
            this.fotoMascota = fotoMascota;
        }

        public String getDescripcionMascota() {
            return descripcionMascota;
        }

        public void setDescripcionMascota(String descripcionMascota) {
            this.descripcionMascota = descripcionMascota;
        }

        public String getRelacionHumanosMascota() {
            return relacionHumanosMascota;
        }

        public void setRelacionHumanosMascota(String relacionHumanosMascota) {
            this.relacionHumanosMascota = relacionHumanosMascota;
        }

        public String getRelacionMascotasMascota() {
            return relacionMascotasMascota;
        }

        public void setRelacionMascotasMascota(String relacionMascotasMascota) {
            this.relacionMascotasMascota = relacionMascotasMascota;
        }

        @Override
        public String toString() {
            return "Usuario{" +
                    "idUsu=" + idUsu +
                    ", nombreUsu='" + nombreUsu + '\'' +
                    ", latitude=" + latitude +
                    ", longitude=" + longitude +
                    ", distance=" + distance +
                    ", nombreMascota='" + nombreMascota + '\'' +
                    ", edadMascota=" + edadMascota +
                    ", sexoMascota='" + sexoMascota + '\'' +
                    ", fotoMascota='" + fotoMascota + '\'' +
                    ", descripcionMascota='" + descripcionMascota + '\'' +
                    ", relacionHumanosMascota='" + relacionHumanosMascota + '\'' +
                    ", relacionMascotasMascota='" + relacionMascotasMascota + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "UserResponse{" +
                "usuarios=" + usuarios +
                '}';
    }
}
