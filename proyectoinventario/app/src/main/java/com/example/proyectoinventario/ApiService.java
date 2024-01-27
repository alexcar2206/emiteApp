package com.example.proyectoinventario;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {

    @GET("users/{userId}")
    Call<User> getUser(@Path("userId") String userId);

    @POST("users")
    Call<Void> createUser(@Body User user);
}
