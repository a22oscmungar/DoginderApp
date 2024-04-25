package com.example.doginder6.Objects;

import com.google.gson.annotations.SerializedName;

import java.io.File;

public class UserRegistrationRequest {
    @SerializedName("name")
    private String name;
    @SerializedName("latitude")
    private double latitude;
    @SerializedName("longitude")
    private double longitude;
    @SerializedName("surname")
    private String surname;
    private File imagenFile;

    public UserRegistrationRequest(String name, double latitude, double longitude, String surname, File imagenFile) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.surname = surname;
        this.imagenFile = imagenFile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }
}
