package com.ssafy.Classik_Backend.auth.controller

import com.ssafy.Classik_Backend.auth.dto.LoginRequestDto
import com.ssafy.Classik_Backend.auth.dto.LoginResponseDto
import com.ssafy.Classik_Backend.auth.service.LoginService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/login")
class LoginController(private val loginService: LoginService) {

    @PostMapping
    fun login(@RequestBody loginRequestDto: LoginRequestDto): ResponseEntity<Any> {
        return try {
            val response = loginService.login(loginRequestDto.email, loginRequestDto.password)
            ResponseEntity.ok(response)
        } catch (ex: ResponseStatusException) {
            // ResponseStatusException을 처리하여 JSON 응답으로 반환
            ResponseEntity
                .status(ex.statusCode) // status 대신 statusCode 사용
                .body(mapOf("error" to ex.reason, "status" to ex.statusCode.value()))
        } catch (ex: Exception) {
            // 기타 예외 처리
            ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(mapOf("error" to "An unexpected error occurred", "details" to ex.message))
        }
    }
}