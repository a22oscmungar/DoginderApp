package com.example.doginder6;

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
        @SerializedName("imageUrl")
        private String imageUrl;

        public Usuario(int idUsu, String nombreUsu, double latitude, double longitude, double distance, String imageUrl) {
            this.idUsu = idUsu;
            this.nombreUsu = nombreUsu;
            this.latitude = latitude;
            this.longitude = longitude;
            this.distance = distance;
            this.imageUrl = imageUrl;
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

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }
    }
}
