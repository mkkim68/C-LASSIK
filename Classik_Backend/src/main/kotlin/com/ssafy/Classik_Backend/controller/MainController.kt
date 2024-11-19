package com.ssafy.Classik_Backend.controller

import com.ssafy.Classik_Backend.auth.dto.CustomMemberDetails
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController

@RestController
@ResponseBody
class MainController {

    @GetMapping("/")
    fun mainP(): String {
        val authentication = SecurityContextHolder.getContext().authentication
        println("Authentication: $authentication")
        println("Principal: ${authentication.principal}")

        val principal = authentication.principal
        val nickname = if (principal is CustomMemberDetails) {
            principal.nickname ?: "Anonymous"
        } else {
            "Anonymous"
        }

        return "main Controller: $nickname"
    }
}
