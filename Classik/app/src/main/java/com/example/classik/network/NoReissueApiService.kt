package com.example.classik.network

import com.example.classik.data.model.BaseTrackItem
import com.example.classik.data.model.LoginResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

data class SignupRequest(
    val email: String,
    val password: String,
    val nickname: String,
    val profileImage: String?
)

data class LoginRequest(
    val email: String,
    val password: String
)

interface NoReissueApiService {

    @POST("/login")
    fun login(@Body loginRequest: LoginRequest): Call<LoginResponse>

    @POST("/signup")
    fun signup(
        @Body signupRequest: SignupRequest,
    ): Call<Void>


    @GET("/history")
    fun getHistory(
        @Header("Authorization") authorization: String,
    ): Call<List<BaseTrackItem>>
}