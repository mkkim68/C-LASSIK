package com.example.classik.network

import com.example.classik.data.model.MemberInfo
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.Part
import retrofit2.http.PartMap
import retrofit2.http.Query

interface MyPageApiService {
    // 시청 기록 조회
//    @GET("/history")
//    fun getHistory(
//        @Header("Authorization") authorization: String,
//    ): Call<List<BaseTrackItem>>

    // 회원 정보 조회
    @GET("/auth")
    fun getMemberInfo(
        @Header("Authorization") authorization: String,
        @Query("memberId") memberId: Int
    ): Call<MemberInfo>

    // 회원 정보 변경
    @Multipart
    @PATCH("/auth")
    fun patchMemberInfo(
        @Header("Authorization") authorization: String,
        @Query("memberId") memberId: Int,
        @PartMap params: Map<String, @JvmSuppressWildcards RequestBody>,
        @Part profileImage: MultipartBody.Part? = null
    ): Call<MemberInfo>

    // 회원 탈퇴
    @DELETE("/auth")
    fun deleteMember(
        @Header("Authorization") authorization: String,
        @Query("memberId") memberId: Int
    ): Call<Void>
}