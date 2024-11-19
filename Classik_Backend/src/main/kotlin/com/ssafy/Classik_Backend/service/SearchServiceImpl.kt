package com.ssafy.Classik_Backend.service

import com.ssafy.Classik_Backend.auth.util.getLoginUserId
import com.ssafy.Classik_Backend.dto.*
import com.ssafy.Classik_Backend.repository.TrackRepository
import io.github.cdimascio.dotenv.Dotenv
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.core.ZSetOperations
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.client.RestClient
import java.time.Instant

@Service
class SearchServiceImpl(
    private val redisTemplate: RedisTemplate<String, Any>,
    private val trackRepository: TrackRepository,
) : SearchService {

    final val dotenv = Dotenv.load()
    val SEARCH_SERVER_URL = dotenv["SEARCH_SERVER_URL"] ?: throw IllegalArgumentException("SEARCH_SERVER_URL is missing")

    private val REDIS_SEARCH_KEY_PREFIX: String = "searchHistory-"
    private val restClient = RestClient.create()

    @Transactional
    override fun search(search: String): List<TrackSimpleResponseDto> {
        updateSearchHistory(search)
        return getSearchedTracks(search)
    }

    private fun getSearchedTracks(search: String): List<TrackSimpleResponseDto> {
        val searchedResult = restClient.get().uri(SEARCH_SERVER_URL, search).retrieve().body(TracksFromSearchingServerDto::class.java)!!
        var result: List<TrackSimpleResponseDto> = mutableListOf()
        for (items in searchedResult.items) {
            result = items.map { item -> trackRepository.findByTitle(item.title).toSimpleDto() }
        }
        return result
    }

    private fun updateSearchHistory(search: String) {
        val zSetOps: ZSetOperations<String, Any> = redisTemplate.opsForZSet()
        val key = REDIS_SEARCH_KEY_PREFIX + getLoginUserId()
        val score = Instant.now().epochSecond.toDouble()
        zSetOps.add(key, search, score)
    }

    override fun getSearchHistory(): SearchHistoryResponseDto {
        val zSetOps = redisTemplate.opsForZSet()
        val key = REDIS_SEARCH_KEY_PREFIX + getLoginUserId()
        val history = zSetOps.reverseRange(key, 0, 10)
        val searchHistory = mutableListOf<String>()
        if (history != null) {
            for (h in history) {
                searchHistory.add(h.toString())
            }
        }
        return SearchHistoryResponseDto(searchHistory)
    }

}