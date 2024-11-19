package com.ssafy.Classik_Backend.auth.jwt

import com.ssafy.Classik_Backend.auth.dto.CustomMemberDetails
import com.ssafy.Classik_Backend.repository.MemberRepository
import com.ssafy.Classik_Backend.auth.service.TokenBlacklistService
import io.jsonwebtoken.ExpiredJwtException
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter
import java.io.IOException
import java.util.function.Supplier


class JWTFilter(
    jwtUtil: JWTUtil,
    private val memberRepository: MemberRepository,
    tokenBlacklistService: TokenBlacklistService
) : OncePerRequestFilter() {
    private val logger = LoggerFactory.getLogger(JWTFilter::class.java) // Logger 추가
    private val jwtUtil: JWTUtil = jwtUtil
    private val tokenBlacklistService: TokenBlacklistService = tokenBlacklistService

    @Throws(ServletException::class, IOException::class)
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        // 요청 URI 확인
        val requestURI = request.requestURI

        // /signup 및 /login 경로를 패스
        if (requestURI == "/signup" || requestURI == "/login") {
            logger.info("Request URI: $requestURI - Authentication not required") // 로그 추가
            filterChain.doFilter(request, response)
            return
        }

        // 헤더에서 access에 담긴 토큰 꺼내기
        var accessToken = request.getHeader("Authorization")

        // 토큰이 없는 경우 다음 필터로
        if (accessToken == null || !accessToken.startsWith("Bearer ")) {
            logger.warn("Authorization header missing or invalid for URI: $requestURI") // 로그 추가
            filterChain.doFilter(request, response)
            return
        }

        accessToken = accessToken.substring(7) // "Bearer " 제거
        logger.info("Extracted Access Token: $accessToken") // 로그 추가

        // 블랙리스트에 있는지 확인
        if (tokenBlacklistService.isBlacklisted(accessToken)) {
            logger.warn("Access Token is blacklisted: $accessToken") // 로그 추가
            response.status = HttpServletResponse.SC_UNAUTHORIZED
            response.contentType = "application/json;charset=UTF-8"
            response.writer.print("""{"error": "블랙리스트 토큰", "message": "해당 토큰은 블랙리스트에 포함되어 있습니다."}""")
            return
        }

        // 토큰 만료 여부 확인
        try {
            jwtUtil.isExpired(accessToken)
        } catch (e: ExpiredJwtException) {
            logger.error("Access Token expired: $accessToken") // 로그 추가
            response.status = HttpServletResponse.SC_UNAUTHORIZED
            response.contentType = "application/json;charset=UTF-8"
            response.writer.print("""{"error": "토큰 만료", "message": "Access Token이 만료되었습니다."}""")
            return
        }

        // 토큰이 access인지 확인
        val category: String = jwtUtil.getCategory(accessToken)
        if (category != "access") {
            logger.warn("Invalid Access Token category: $category for token $accessToken") // 로그 추가
            response.status = HttpServletResponse.SC_UNAUTHORIZED
            response.contentType = "application/json;charset=UTF-8"
            response.writer.print("""{"error": "유효하지 않은 토큰", "message": "유효하지 않은 Access Token입니다."}""")
            return
        }

        // email 값 획득 후 사용자 조회
        val email: String = jwtUtil.getEmail(accessToken)
        val memberEntity = memberRepository.findByEmail(email)
            ?.orElseThrow(Supplier { IllegalArgumentException("사용자를 찾을 수 없습니다.") })!!

        logger.info("Authenticated user: $email with roles: ${memberEntity.role}") // 로그 추가

        // 사용자 권한 설정
        val customMemberDetails = CustomMemberDetails(memberEntity)
        val authToken: Authentication =
            UsernamePasswordAuthenticationToken(customMemberDetails, null, customMemberDetails.authorities)
        SecurityContextHolder.getContext().authentication = authToken

        logger.info("SecurityContext updated with authenticated user: $email") // 로그 추가
        filterChain.doFilter(request, response)
    }
}
