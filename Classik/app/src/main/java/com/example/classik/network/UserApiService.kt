package com.example.classik.network

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface UserApiService {
    @GET("/")
    fun getTest(): Call<String>

    @POST("/auth-logout")
    fun logout(
        @Header("Authorization") authorization: String,
        @Header("Cookie") refresh: String,
    ): Call<Void>
}