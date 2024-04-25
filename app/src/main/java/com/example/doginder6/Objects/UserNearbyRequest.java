package com.example.doginder6.Objects;

import com.google.gson.annotations.SerializedName;

public class UserNearbyRequest {
    @SerializedName("latitude")
    public double latitude;
    @SerializedName("longitude")
    public double longitude;
    @SerializedName("radius")
    public float radius;
    public UserNearbyRequest(double latitude, double longitude, float radius) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.radius = radius;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public float getRadius() {
        return radius;
    }
}
