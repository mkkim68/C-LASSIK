package com.ssafy.Classik_Backend.auth.controller

import com.ssafy.Classik_Backend.auth.entity.RefreshToken
import com.ssafy.Classik_Backend.auth.jwt.JWTUtil
import com.ssafy.Classik_Backend.auth.repository.RefreshRepository
import io.jsonwebtoken.ExpiredJwtException
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@ResponseBody
class ReissueController(private val jwtUtil: JWTUtil, private val refreshRepository: RefreshRepository) {
    @PostMapping("/reissue")
    fun reissue(request: HttpServletRequest, response: HttpServletResponse): ResponseEntity<Map<String, String>> {
        // get refresh token
        var refresh: String? = null
        val cookies = request.cookies
        if (cookies != null) {
            for (cookie in cookies) {
                if (cookie.name == "refresh") {
                    refresh = cookie.value
                    break
                }
            }
        }

        if (refresh == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(mapOf("status" to "fail", "message" to "리프레시 토큰이 없습니다."))
        }

        // 만료 체크
        try {
            jwtUtil.isExpired(refresh)
        } catch (e: ExpiredJwtException) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(mapOf("status" to "fail", "message" to "리프레시 토큰이 만료되었습니다."))
        }

        // 토큰이 refresh인지 확인 (발급 시 페이로드에 명시)
        val category = jwtUtil.getCategory(refresh)
        if (category != "refresh") {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(mapOf("status" to "fail", "message" to "유효하지 않은 리프레시 토큰입니다."))
        }

        // DB에 저장되어 있는지 확인
        val isExist = refreshRepository.existsByRefresh(refresh)
        if (!isExist!!) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(mapOf("status" to "fail", "message" to "존재하지 않는 리프레시 토큰입니다."))
        }

        val email = jwtUtil.getEmail(refresh)
        val role = jwtUtil.getRole(refresh)

        // JWT 재발급
        val newAccess = jwtUtil.createJwt("access", email, role, 864000000L)
        val newRefresh = jwtUtil.createJwt("refresh", email, role, 8640000000L)

        // refresh 토큰 저장 DB에 기존의 refresh 토큰 삭제 후 새 refresh 토큰 저장
        refreshRepository.deleteByEmail(email)
        addRefreshEntity(email, newRefresh, 8640000000L)

        // response
        response.setHeader("access", newAccess)
        response.addCookie(createCookie("refresh", newRefresh))

        return ResponseEntity.ok(
            mapOf(
                "status" to "success",
                "message" to "토큰이 성공적으로 재발급되었습니다.",
                "accessToken" to newAccess,
                "refreshToken" to newRefresh
            )
        )
    }

    private fun createCookie(key: String, value: String): Cookie {
        val cookie = Cookie(key, value)
        cookie.maxAge = 24 * 60 * 60
        cookie.isHttpOnly = true
        return cookie
    }

    private fun addRefreshEntity(email: String, newRefresh: String, expiredMs: Long) {
        val date = Date(System.currentTimeMillis() + expiredMs)
        val refreshEntity = RefreshToken()
        refreshEntity.refresh = newRefresh
        refreshEntity.email = email
        refreshEntity.expiration = date.toString()
        refreshRepository.save(refreshEntity)
    }
}