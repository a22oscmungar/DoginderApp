package com.example.doginder6;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.tasks.CancellationToken;

import java.util.concurrent.Executor;

public class LocationHelper {

    private Context context;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;
    private Location lastKnownLocation;

    public interface CustomLocationListener {
        void onLocationObtained(Location location);
        void onLocationNotAvailable();
    }

    public LocationHelper(Context context) {
        this.context = context;
        fusedLocationClient = new FusedLocationProviderClient(context);
        locationCallback = new MyLocationCallback();
    }

    public void requestLocationUpdates() {
        try {
            LocationRequest locationRequest = new LocationRequest()
                    .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                    .setInterval(1000)
                    .setFastestInterval(500);

            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    public void requestSingleUpdate(CustomLocationListener locationCallback) {
        try {
            LocationRequest locationRequest = new LocationRequest()
                    .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

            fusedLocationClient.requestLocationUpdates(locationRequest, new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    fusedLocationClient.removeLocationUpdates(this);
                    if (locationResult != null && locationResult.getLastLocation() != null) {
                        locationCallback.onLocationObtained(locationResult.getLastLocation());
                    } else {
                        locationCallback.onLocationNotAvailable();
                    }
                }
            }, null);
        } catch (SecurityException e) {
            e.printStackTrace();
            locationCallback.onLocationNotAvailable();
        }
    }

    public void removeLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback);
    }

    public Location getLastKnownLocation() {
        return lastKnownLocation;
    }

    private class MyLocationCallback extends LocationCallback {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            if (locationResult != null && locationResult.getLastLocation() != null) {
                lastKnownLocation = locationResult.getLastLocation();
            } else {
                // Manejar la falta de ubicación
            }
        }
    }
}
