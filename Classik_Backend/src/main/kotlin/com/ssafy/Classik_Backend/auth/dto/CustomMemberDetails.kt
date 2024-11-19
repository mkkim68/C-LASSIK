package com.ssafy.Classik_Backend.auth.dto

import com.ssafy.Classik_Backend.entity.Member
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class CustomMemberDetails(private val memberEntity: Member) : UserDetails {
    override fun getAuthorities(): Collection<GrantedAuthority> {
        val collection: MutableCollection<GrantedAuthority> = ArrayList()
        collection.add(GrantedAuthority { memberEntity.role!! })

        return collection
    }

    override fun getPassword(): String? {
        return memberEntity.password
    }

    override fun getUsername(): String? {
        return memberEntity.email
    }

    fun getMemberId(): Int {
        return memberEntity.id
    }

    val nickname: String?
        get() = memberEntity.nickname

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return true
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun isEnabled(): Boolean {
        return true
    }
}
