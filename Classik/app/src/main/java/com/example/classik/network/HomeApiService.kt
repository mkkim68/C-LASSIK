package com.example.classik.network

import com.example.classik.data.model.RecommendList
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header

interface HomeApiService {
    @GET("/recommends")
    fun getRecommends(
        @Header("Authorization") authorization: String,
    ): Call<List<RecommendList>>
}