package com.ssafy.Classik_Backend.auth.controller

import com.ssafy.Classik_Backend.auth.dto.SignupRequestDto
import com.ssafy.Classik_Backend.auth.dto.SignupResponseDto
import com.ssafy.Classik_Backend.auth.service.SignupService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController

@RestController
@ResponseBody
class SignupController(private val signupService: SignupService) {
    @PostMapping("/signup")
    fun signupProcess(
        @RequestBody signupRequestDto: @Valid SignupRequestDto
    ): ResponseEntity<SignupResponseDto> {
        println(signupRequestDto.email)
        val memberId = signupService.signupProcess(signupRequestDto)

        return ResponseEntity.ok(SignupResponseDto(memberId))
    }
}
