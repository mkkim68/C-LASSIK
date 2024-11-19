package com.ssafy.Classik_Backend.repository

import com.ssafy.Classik_Backend.entity.Playlist
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PlaylistRepository : JpaRepository<Playlist, Int> {

    fun findByMemberId(userId: Int): List<Playlist>

}