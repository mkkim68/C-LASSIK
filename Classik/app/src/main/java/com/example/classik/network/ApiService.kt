package com.example.classik.network

import android.content.Context
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiService {
    private const val BASE_URL = "https://k11a604.p.ssafy.io"

    // 일반 API 서비스
    lateinit var userApi: UserApiService
    lateinit var homeApi: HomeApiService
    lateinit var searchApi: SearchApiService
    lateinit var playlistApi: PlaylistApiService
    lateinit var musicPlaybackApi: MusicPlaybackApiService
    lateinit var myPageApiService: MyPageApiService

    // `NoReissue`를 위한 API 서비스
    lateinit var noReissueApi: NoReissueApiService

    fun init(context: Context) {
        val clientWithReissue = OkHttpClient.Builder()
            .addInterceptor(TokenInterceptor(context))
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()

        val clientWithoutReissue = OkHttpClient.Builder()
            .addInterceptor(NoReissueInterceptor())
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()

        val retrofitWithReissue = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(clientWithReissue)
            .build()

        val retrofitWithoutReissue = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(clientWithoutReissue)
            .build()

        userApi = retrofitWithReissue.create(UserApiService::class.java)
        homeApi = retrofitWithoutReissue.create(HomeApiService::class.java)
        searchApi = retrofitWithoutReissue.create(SearchApiService::class.java)
        playlistApi = retrofitWithoutReissue.create(PlaylistApiService::class.java)
        musicPlaybackApi = retrofitWithoutReissue.create(MusicPlaybackApiService::class.java)
        myPageApiService = retrofitWithReissue.create(MyPageApiService::class.java)
        noReissueApi = retrofitWithoutReissue.create(NoReissueApiService::class.java)
    }
}
