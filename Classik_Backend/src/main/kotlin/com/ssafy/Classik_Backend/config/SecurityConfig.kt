package com.ssafy.Classik_Backend.config

import com.ssafy.Classik_Backend.auth.jwt.JWTFilter
import com.ssafy.Classik_Backend.auth.jwt.JWTUtil
import com.ssafy.Classik_Backend.repository.MemberRepository
import com.ssafy.Classik_Backend.auth.repository.RefreshRepository
import com.ssafy.Classik_Backend.auth.service.CustomMemberDetailService
import com.ssafy.Classik_Backend.auth.service.TokenBlacklistService
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.ProviderManager
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.cors.CorsConfiguration

@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val authenticationConfiguration: AuthenticationConfiguration,
    private val jwtUtil: JWTUtil,
    private val refreshRepository: RefreshRepository,
    private val customMemberDetailService: CustomMemberDetailService,
    private val tokenBlacklistService: TokenBlacklistService
) {
    private val logger = LoggerFactory.getLogger(SecurityConfig::class.java) // Logger 추가

    @Bean
    fun bCryptPasswordEncoder(): BCryptPasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun authenticationProvider(): DaoAuthenticationProvider {
        val authProvider = DaoAuthenticationProvider()
        authProvider.setUserDetailsService(customMemberDetailService)
        authProvider.setPasswordEncoder(bCryptPasswordEncoder())
        return authProvider
    }

    @Bean
    @Throws(Exception::class)
    fun authenticationManager(): AuthenticationManager {
        val authenticationManager = authenticationConfiguration.authenticationManager
        (authenticationManager as ProviderManager).providers.add(authenticationProvider())
        return authenticationManager
    }

    @Bean
    fun securityFilterChain(
        http: HttpSecurity,
        memberRepository: MemberRepository
    ): SecurityFilterChain {
        logger.info("Initializing SecurityFilterChain...") // SecurityFilterChain 초기화 로그

        http
            .csrf {
                it.disable()
                logger.info("CSRF protection disabled") // CSRF 설정 로그
            }
            .formLogin {
                it.disable()
                logger.info("Form login disabled") // Form login 설정 로그
            }
            .httpBasic {
                it.disable()
                logger.info("HTTP Basic authentication disabled") // HTTP Basic 인증 로그
            }
            .authorizeHttpRequests {
                it.requestMatchers("/login", "/**").permitAll()
                    .anyRequest().authenticated()
                logger.info("Authorization rules configured") // 인증 규칙 설정 로그
            }
            .addFilterBefore(
                JWTFilter(jwtUtil, memberRepository, tokenBlacklistService),
                UsernamePasswordAuthenticationFilter::class.java
            ).also {
                logger.info("JWTFilter added before UsernamePasswordAuthenticationFilter") // JWT 필터 추가 로그
            }
            .sessionManagement {
                it.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                logger.info("Session management set to STATELESS") // 세션 관리 로그
            }
            .cors {
                it.configurationSource {
                    val configuration = CorsConfiguration()
                    configuration.allowedOrigins = listOf("*")
                    configuration.allowedMethods = listOf("*")
                    configuration.allowCredentials = true
                    configuration.allowedHeaders = listOf("*")
                    configuration.maxAge = 3600L
                    configuration.exposedHeaders = listOf("Authorization")
                    logger.info("CORS configuration set with allowed origins: *") // CORS 설정 로그
                    configuration
                }
            }

        logger.info("SecurityFilterChain successfully initialized.") // SecurityFilterChain 완료 로그

        return http.build()
    }
}