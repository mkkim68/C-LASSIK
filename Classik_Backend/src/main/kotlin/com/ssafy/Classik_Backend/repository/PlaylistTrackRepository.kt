package com.ssafy.Classik_Backend.repository

import com.ssafy.Classik_Backend.entity.PlaylistTrack
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PlaylistTrackRepository : JpaRepository<PlaylistTrack, Int> {

    fun findAllByPlaylistId(playlistId: Int): List<PlaylistTrack>

}