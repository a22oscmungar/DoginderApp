package com.example.doginder6;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.GET;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface doginderAPI {

    @Multipart
    @POST("/users")
    Call<Void> registerUser(
            @Part("name") RequestBody name,
            @Part("latitude") RequestBody latitude,
            @Part("longitude") RequestBody longitude,
            @Part("surname") RequestBody surname,
            @Part("mailUsu") RequestBody mailUsu,
            @Part MultipartBody.Part imagenFile
    );
    @GET("/users/nearby")
    Call<List<UserResponse.Usuario>> getNearbyUsers(@Query("latitude") double latitude, @Query("longitude") double longitude, @Query("radius") int radius);



}