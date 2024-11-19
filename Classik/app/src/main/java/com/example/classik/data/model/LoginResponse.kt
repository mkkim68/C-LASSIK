package com.example.classik.data.model

data class LoginResponse (
    val accessToken: String,
    val refreshToken: String,
    val userInfo: MemberInfo
)