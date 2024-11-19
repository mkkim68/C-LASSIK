package com.ssafy.Classik_Backend.auth.util

import com.ssafy.Classik_Backend.auth.dto.CustomMemberDetails
import org.springframework.security.core.context.SecurityContextHolder

fun getLoginUserId(): Int {
    val authentication = SecurityContextHolder.getContext().authentication
    val userDetails = authentication.principal as CustomMemberDetails
    return userDetails.getMemberId()
}