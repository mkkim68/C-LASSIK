package com.ssafy.Classik_Backend.auth.dto

data class MemberInfoDto(
    val memberId: Int? = null,
    val email: String? = null,
    val nickname: String? = null,
    val profileUrl: String? = null,
    val role: String? = null
)
