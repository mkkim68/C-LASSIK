package com.ssafy.Classik_Backend.auth.repository

import com.ssafy.Classik_Backend.auth.entity.RefreshToken
import jakarta.transaction.Transactional
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface RefreshRepository : JpaRepository<RefreshToken?, Long?> {
    fun existsByRefresh(refresh: String?): Boolean?

    // 이메일로 리프레시 토큰을 조회
    fun findByEmail(email: String?): Optional<RefreshToken?>?

    // 이메일로 리프레시 토큰을 삭제
    @Transactional
    fun deleteByEmail(email: String?)
}