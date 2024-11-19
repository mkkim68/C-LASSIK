package com.ssafy.Classik_Backend.exception

import lombok.RequiredArgsConstructor
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
@RequiredArgsConstructor
class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException::class)
    fun exceptionHandler(illegalArgumentException: IllegalArgumentException): ResponseEntity<Any?> {
        val resHeaders = HttpHeaders()
        resHeaders.add("Content-Type", "application/json;charset=UTF-8")
        return ResponseEntity<Any?>(illegalArgumentException.message, resHeaders, HttpStatus.BAD_REQUEST)
    }

}