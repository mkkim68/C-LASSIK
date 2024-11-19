package com.ssafy.Classik_Backend.auth.service

import com.ssafy.Classik_Backend.auth.dto.SignupRequestDto
import com.ssafy.Classik_Backend.entity.Member
import com.ssafy.Classik_Backend.repository.MemberRepository
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths

@Service
class SignupService(
    private val memberRepository: MemberRepository,
    private val bCryptPasswordEncoder: BCryptPasswordEncoder
) {
    fun signupProcess(signupRequestDto: SignupRequestDto): Int? {
        val email = signupRequestDto.email
        val password = signupRequestDto.password
        val nickname = signupRequestDto.nickname

        // 이메일 중복 체크
        if (memberRepository.existsByEmail(email)!!) {
            throw DataIntegrityViolationException("이미 사용 중인 이메일입니다.")
        }

        // 비밀번호 암호화
        val encodedPassword = bCryptPasswordEncoder.encode(password)

        // MemberEntity 생성
        val memberEntity = Member.of(signupRequestDto, encodedPassword, null)

        // DB에 저장
        val savedMember = memberRepository.save(memberEntity)

        // 저장된 member_id 반환
        return savedMember.id
    }
}