package com.ssafy.Classik_Backend.controller

import com.ssafy.Classik_Backend.auth.dto.MemberInfoDto
import com.ssafy.Classik_Backend.auth.dto.MemberUpdateDto
import com.ssafy.Classik_Backend.service.MemberService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/auth")
class MemberController(private val memberService: MemberService) {

    @GetMapping
    fun getMemberInfo(@RequestParam memberId: Int?): ResponseEntity<Any> {
        return try {
            if (memberId == null) {
                throw IllegalArgumentException("회원 아이디가 null입니다.")
            }
            val memberInfo = memberService.getMemberInfo(memberId)
            ResponseEntity.ok(memberInfo)
        } catch (e: IllegalArgumentException) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(mapOf("error" to e.message))
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(mapOf("error" to "회원 정보를 불러오는 과정에서 문제가 발생했습니다."))
        }
    }

    @PatchMapping
    fun updateMemberInfo(
        @RequestParam memberId: Int?,
        @ModelAttribute memberUpdateDto: MemberUpdateDto?
    ): ResponseEntity<Any> {
        return try {
            if (memberId == null) {
                throw IllegalArgumentException("회원 아이디가 null입니다.")
            }
            if (memberUpdateDto == null) {
                throw IllegalArgumentException("MemberUpdateDto cannot be null")
            }
            val updatedMemberInfo = memberService.updateMemberInfo(memberId, memberUpdateDto)
            ResponseEntity.ok(updatedMemberInfo)
        } catch (e: IllegalArgumentException) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(mapOf("error" to e.message))
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(mapOf("error" to "회원 정보를 불러오는 과정에서 문제가 발생했습니다."))
        }
    }

    @DeleteMapping
    fun deleteMember(@RequestParam memberId: Int?): ResponseEntity<Any> {
        return try {
            if (memberId == null) {
                throw IllegalArgumentException("회원 아이디가 null입니다.")
            }
            memberService.deleteMemberInfo(memberId)
            ResponseEntity.ok(mapOf("message" to "회원 삭제 완료"))
        } catch (e: IllegalArgumentException) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(mapOf("error" to e.message))
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(mapOf("error" to "회원 정보를 불러오는 과정에서 문제가 발생했습니다."))
        }
    }
}
