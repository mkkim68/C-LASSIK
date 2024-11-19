package com.ssafy.Classik_Backend.repository

import com.ssafy.Classik_Backend.entity.PlaybackHistory
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PlaybackHistoryRepository : JpaRepository<PlaybackHistory, Int> {
}