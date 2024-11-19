package com.ssafy.Classik_Backend.repository

import com.ssafy.Classik_Backend.entity.Track
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TrackRepository : JpaRepository<Track, Int> {

    fun findAllByTitle(title: String): List<Track>
    fun findByTagsContaining(content: String): List<Track>
    fun findByTitle(title: String): Track

}
