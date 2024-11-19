package com.ssafy.Classik_Backend.repository

import com.ssafy.Classik_Backend.entity.Member
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface MemberRepository : JpaRepository<Member, Int> {
    fun existsByEmail(email: String?): Boolean?

    // username을 받아 DB 테이블에서 회원 조회하는 메서드 작성
    fun findByEmail(email: String?): Optional<Member?>?

}
