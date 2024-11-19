package com.example.classik.network

import com.example.classik.data.model.BaseTrackItem
import com.example.classik.data.model.SearchHistory
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface SearchApiService {
    @GET("/search/{search_item}")
    fun getSearch(
        @Header("Authorization") authorization: String,
        @Path("search_item") searchKeyword: String
    ): Call<List<BaseTrackItem>>

    @GET("/search")
    fun getSearchHistory(
        @Header("Authorization") authorization: String,
    ) : Call<SearchHistory>
}