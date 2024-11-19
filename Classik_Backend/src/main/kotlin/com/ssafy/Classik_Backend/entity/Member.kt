package com.ssafy.Classik_Backend.entity

import com.ssafy.Classik_Backend.auth.dto.SignupRequestDto
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity
class Member(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id", nullable = false)
    var id: Int = 0,

    @Column(name = "email", nullable = false)
    var email: String?,

    @Column(name = "password", nullable = false)
    var password: String?,

    @Column(name = "nickname", nullable = false)
    var nickname: String?,

    @Column(name = "profile_url")
    var profileUrl: String? = null,

    @Column(name = "role", nullable = false)
    var role: String?
) {
    companion object {
        fun of(requestDto: SignupRequestDto, encodedPassword: String, profileUrl: String?): Member {
            return Member(
                email = requestDto.email,
                password = encodedPassword,
                nickname = requestDto.nickname,
                profileUrl = profileUrl,
                role = "USER"
            )
        }
    }
}
