package com.example.classik.network

import android.content.Context
import com.example.classik.utils.LocalStorageManager
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Header
import retrofit2.http.POST

class TokenInterceptor(private val context: Context): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val accessToken = LocalStorageManager.getAccessToken(context)
        val refreshToken = LocalStorageManager.getRefreshToken(context)
        var request = chain.request()

        if (!accessToken.isNullOrEmpty() && !refreshToken.isNullOrEmpty()) {
            request = addAuthorizationHeaders(request, accessToken, refreshToken)
        }

        val response = chain.proceed(request)

        if (response.code == 401) {
            synchronized(this) {
                val newAccessToken = LocalStorageManager.getAccessToken(context)

                if (newAccessToken != accessToken) {
                    request = addAuthorizationHeaders(request, newAccessToken!!, refreshToken!!)
                    return chain.proceed(request)
                }

                val reissueResponse = reissueTokens(refreshToken!!)
                if (reissueResponse.isSuccessful) {
                    reissueResponse.body()?.let {
                        tokenResponse ->
                        LocalStorageManager.saveAccessToken(context, tokenResponse.accessToken)
                        LocalStorageManager.saveRefreshToken(context, tokenResponse.refreshToken)

                        request = addAuthorizationHeaders(request, tokenResponse.accessToken, tokenResponse.refreshToken)
                        return chain.proceed(request)
                    }
                }
            }
        }

        return response
    }

    private fun addAuthorizationHeaders(request: Request, accessToken: String, refreshToken: String): Request {
        return request.newBuilder()
            .header("Authorization", accessToken)
            .header("Cookie", "refresh=$refreshToken")
            .build()
    }

    private fun reissueTokens(refreshToken: String): retrofit2.Response<TokenResponse> {
        val tokenApiService = TokenApiService.create()
        val call = tokenApiService.reissue("Bearer $refreshToken")
        return call.execute()
    }
}

data class TokenResponse(val accessToken: String, val refreshToken: String)

interface TokenApiService {
    @POST("/auth/reissue")
    fun reissue(@Header("Authorization") refreshToken: String): Call<TokenResponse>

    companion object {
        fun create(): TokenApiService {
            return Retrofit.Builder()
                .baseUrl("https://k11a604.p.ssafy.io")
                .addConverterFactory(GsonConverterFactory.create())
                .client(OkHttpClient())
                .build()
                .create(TokenApiService::class.java)
        }
    }
}
