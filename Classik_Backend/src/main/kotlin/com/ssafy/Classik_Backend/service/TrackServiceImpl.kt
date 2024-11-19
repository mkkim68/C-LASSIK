package com.ssafy.Classik_Backend.service

import com.ssafy.Classik_Backend.repository.MemberRepository
import com.ssafy.Classik_Backend.auth.util.getLoginUserId
import com.ssafy.Classik_Backend.dto.TrackDetailResponseDto
import com.ssafy.Classik_Backend.dto.toDetailDto
import com.ssafy.Classik_Backend.dto.toSimpleDto
import com.ssafy.Classik_Backend.entity.PlaybackHistory
import com.ssafy.Classik_Backend.entity.Track
import com.ssafy.Classik_Backend.repository.PlaybackHistoryRepository
import com.ssafy.Classik_Backend.repository.TrackRepository
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.core.ZSetOperations
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant

@Service
class TrackServiceImpl(
    private val playbackHistoryRepository: PlaybackHistoryRepository,
    private val trackRepository : TrackRepository,
    private val memberRepository : MemberRepository,
    private val redisTemplate: RedisTemplate<String, Any>,
) : TrackService {

    private val REDIS_KEY_PREFIX: String = "playbackHistory-"

    @Transactional
    override fun getTrack(trackId: Int): TrackDetailResponseDto {
        val track = trackRepository.findById(trackId).orElseThrow { IllegalArgumentException("Track Not Found. TrackId: $trackId") }
        saveHistory(track)
        updateHistoryList(track)
        return track.toDetailDto()
    }

    private fun saveHistory(track : Track) {
        playbackHistoryRepository.save(
            PlaybackHistory(member = memberRepository.findById(getLoginUserId()).orElseThrow { IllegalArgumentException("Track Not Found.") }, track = track)
        )
    }

    private fun updateHistoryList(track : Track) {
        val zSetOps: ZSetOperations<String, Any> = redisTemplate.opsForZSet()
        val key = REDIS_KEY_PREFIX + getLoginUserId()
        val score = Instant.now().epochSecond.toDouble()
        zSetOps.add(key, track.toSimpleDto(), score)
    }

    @Transactional
    override fun searchTracks(word: String): List<Track> {
        return trackRepository.findAllByTitle(word)
    }

}