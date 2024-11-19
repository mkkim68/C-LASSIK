package com.ssafy.Classik_Backend.service

import com.ssafy.Classik_Backend.auth.util.getLoginUserId
import com.ssafy.Classik_Backend.dto.TrackSimpleResponseDto
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service

@Service
class PlaybackHistoryServiceImpl(private val redisTemplate: RedisTemplate<String, Any>,)
    : PlaybackHistoryService {

    private val REDIS_KEY_PREFIX: String = "playbackHistory-"

    override fun getHistoryList(): List<TrackSimpleResponseDto> {
        val zSetOps = redisTemplate.opsForZSet()
        val key = REDIS_KEY_PREFIX + getLoginUserId()
        val history = zSetOps.reverseRange(key, 0, -1)

        if (history.isNullOrEmpty()) {
            return emptyList()
        }

        return history.mapNotNull { mapToTrackSimpleResponseDto(it) }
    }

    private fun mapToTrackSimpleResponseDto(item: Any?): TrackSimpleResponseDto? {
        return when (item) {
            is LinkedHashMap<*, *> -> {
                try {
                    TrackSimpleResponseDto(
                        trackId = (item["trackId"] as? Int) ?: return null,
                        title = item["title"] as? String ?: return null,
                        composer = item["composer"] as? String ?: return null,
                        tags = item["tags"] as? List<String> ?: return null,
                        thumbnailUrl = item["thumbnailUrl"] as? String
                    )
                } catch (e: Exception) {
                    null
                }
            }
            is TrackSimpleResponseDto -> item
            else -> null
        }
    }

}