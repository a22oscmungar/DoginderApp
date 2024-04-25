package com.example.doginder6.Helpers;

import android.app.PendingIntent;
import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.internal.ApiKey;
import com.google.android.gms.location.CurrentLocationRequest;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LastLocationRequest;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.tasks.CancellationToken;
import com.google.android.gms.tasks.Task;

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
        fusedLocationClient = new FusedLocationProviderClient() {
            @NonNull
            @Override
            public ApiKey<Api.ApiOptions.NoOptions> getApiKey() {
                return null;
            }

            @NonNull
            @Override
            public Task<Void> flushLocations() {
                return null;
            }

            @NonNull
            @Override
            public Task<Location> getCurrentLocation(int i, @Nullable CancellationToken cancellationToken) {
                return null;
            }

            @NonNull
            @Override
            public Task<Location> getCurrentLocation(@NonNull CurrentLocationRequest currentLocationRequest, @Nullable CancellationToken cancellationToken) {
                return null;
            }

            @NonNull
            @Override
            public Task<Location> getLastLocation() {
                return null;
            }

            @NonNull
            @Override
            public Task<Location> getLastLocation(@NonNull LastLocationRequest lastLocationRequest) {
                return null;
            }

            @NonNull
            @Override
            public Task<LocationAvailability> getLocationAvailability() {
                return null;
            }

            @NonNull
            @Override
            public Task<Void> removeLocationUpdates(@NonNull PendingIntent pendingIntent) {
                return null;
            }

            @NonNull
            @Override
            public Task<Void> removeLocationUpdates(@NonNull LocationCallback locationCallback) {
                return null;
            }

            @NonNull
            @Override
            public Task<Void> removeLocationUpdates(@NonNull LocationListener locationListener) {
                return null;
            }

            @NonNull
            @Override
            public Task<Void> requestLocationUpdates(@NonNull LocationRequest locationRequest, @NonNull PendingIntent pendingIntent) {
                return null;
            }

            @NonNull
            @Override
            public Task<Void> requestLocationUpdates(@NonNull LocationRequest locationRequest, @NonNull LocationCallback locationCallback, @Nullable Looper looper) {
                return null;
            }

            @NonNull
            @Override
            public Task<Void> requestLocationUpdates(@NonNull LocationRequest locationRequest, @NonNull LocationListener locationListener, @Nullable Looper looper) {
                return null;
            }

            @NonNull
            @Override
            public Task<Void> requestLocationUpdates(@NonNull LocationRequest locationRequest, @NonNull Executor executor, @NonNull LocationCallback locationCallback) {
                return null;
            }

            @NonNull
            @Override
            public Task<Void> requestLocationUpdates(@NonNull LocationRequest locationRequest, @NonNull Executor executor, @NonNull LocationListener locationListener) {
                return null;
            }

            @NonNull
            @Override
            public Task<Void> setMockLocation(@NonNull Location location) {
                return null;
            }

            @NonNull
            @Override
            public Task<Void> setMockMode(boolean b) {
                return null;
            }
        };
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
