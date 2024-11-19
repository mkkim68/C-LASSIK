package com.ssafy.Classik_Backend.service

import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.model.CannedAccessControlList
import com.amazonaws.services.s3.model.ObjectMetadata
import com.amazonaws.services.s3.model.PutObjectRequest
import com.ssafy.Classik_Backend.auth.dto.MemberInfoDto
import com.ssafy.Classik_Backend.auth.dto.MemberUpdateDto
import com.ssafy.Classik_Backend.entity.Member
import com.ssafy.Classik_Backend.repository.MemberRepository
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.io.FileOutputStream
import io.github.cdimascio.dotenv.Dotenv
import java.util.UUID

@Service
class MemberService(
    private val memberRepository: MemberRepository,
    private val bCryptPasswordEncoder: BCryptPasswordEncoder,
    private val amazonS3: AmazonS3
) {
    // Dotenv를 사용해 .env 파일에서 환경 변수 읽기
    private val dotenv: Dotenv = Dotenv.configure().load()
    private val bucketName: String = dotenv["AWS_S3_BUCKET"] ?: "default-bucket"
    private val s3BaseUrl: String = dotenv["AWS_S3_BASE_URL"] ?: "https://default-url"

    @Transactional
    fun getMemberInfo(memberId: Int): MemberInfoDto {
        val memberEntity = memberRepository.findById(memberId)
            .orElseThrow { NoSuchElementException("사용자를 찾을 수 없습니다.") }

        return MemberInfoDto(
            memberId = memberEntity?.id,
            email = memberEntity?.email,
            nickname = memberEntity?.nickname,
            profileUrl = memberEntity?.profileUrl,
            role = memberEntity?.role
        )
    }

    @Transactional
    fun updateMemberInfo(memberId: Int, memberUpdateDto: MemberUpdateDto): MemberInfoDto {
        val memberEntity = memberRepository.findById(memberId)
            .orElseThrow { IllegalArgumentException("사용자를 찾을 수 없습니다.") }

        memberUpdateDto.password?.let {
            val encodedPassword = bCryptPasswordEncoder.encode(it)
            memberEntity?.password = encodedPassword
        }

        memberUpdateDto.nickname?.let {
            memberEntity?.nickname = it
        }

        memberUpdateDto.profileImage?.takeIf { !it.isEmpty }?.let {
            if (memberEntity != null) {
                handleProfileImageUpdate(memberEntity, it)
            }
        }

        if (memberEntity != null) {
            memberRepository.save(memberEntity)
        }

        return MemberInfoDto(
            memberId = memberEntity?.id,
            email = memberEntity?.email,
            nickname = memberEntity?.nickname,
            profileUrl = memberEntity?.profileUrl,
            role = memberEntity?.role
        )
    }

    @Transactional
    fun deleteMemberInfo(memberId: Int) {
        val memberEntity = memberRepository.findById(memberId)
            .orElseThrow { IllegalArgumentException("사용자를 찾을 수 없습니다.") }

        memberEntity?.profileUrl?.let { profileUrl ->
            val key = profileUrl.substringAfterLast("/")
            if (amazonS3.doesObjectExist(bucketName, key)) {
                amazonS3.deleteObject(bucketName, key)
            }
        }

        memberRepository.deleteById(memberId)
    }

    private fun handleProfileImageUpdate(memberEntity: Member, newProfileImage: MultipartFile) {
        try {
            memberEntity.profileUrl?.let { oldProfileUrl ->
                val oldKey = oldProfileUrl.substringAfterLast("/")
                if (amazonS3.doesObjectExist(bucketName, oldKey)) {
                    amazonS3.deleteObject(bucketName, oldKey)
                }
            }

            val newFileName = "profileImage/${UUID.randomUUID()}_${newProfileImage.originalFilename}"

            val metadata = ObjectMetadata()
            metadata.contentType = newProfileImage.contentType

            amazonS3.putObject(
                PutObjectRequest(bucketName, newFileName, newProfileImage.inputStream, metadata)
            )

            memberEntity.profileUrl = "$s3BaseUrl/$newFileName"
        } catch (e: Exception) {
            println("S3 업로드 실패: ${e.message}")
            throw RuntimeException("프로필 이미지 업로드 중 오류가 발생했습니다.", e)
        }
    }



    private fun convertMultipartFileToFile(multipartFile: MultipartFile): File {
        val file = File(multipartFile.originalFilename ?: "tempFile")
        FileOutputStream(file).use { outputStream ->
            outputStream.write(multipartFile.bytes)
        }
        return file
    }
}
