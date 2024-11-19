package com.ssafy.Classik_Backend.auth.dto

data class LoginResponseDto(
    val accessToken: String?,
    val refreshToken: String?,
    val userInfo: MemberInfoDto?
)
