package com.ssafy.Classik_Backend.service

import com.ssafy.Classik_Backend.dto.RecommendPlaylistResponseDto
import com.ssafy.Classik_Backend.dto.TrackSimpleResponseDto
import com.ssafy.Classik_Backend.dto.toSimpleDto
import com.ssafy.Classik_Backend.repository.ComposerRepository
import com.ssafy.Classik_Backend.repository.TrackRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class RecommendServiceImpl(
    private val playbackHistoryService: PlaybackHistoryService,
    private val composerRepository: ComposerRepository,
    private val trackRepository: TrackRepository,
) : RecommendService {

    private final val CATEGORY_COMPOSER = "composer"
    private final val CATEGORY_TAG = "tag"
    private final val MAX_RECOMMEND_NUMBER = 10

    @Transactional
    override fun getRecommendLists(): List<RecommendPlaylistResponseDto> {
        val recommendRanking = getRecommendRanking(playbackHistoryService.getHistoryList())
        val recommendPlaylists = mutableListOf<RecommendPlaylistResponseDto>()
        for (recommend in recommendRanking) {
            recommendPlaylists.add(getRecommendPlaylist(recommend))
        }
        return recommendPlaylists
    }

    private fun getRecommendRanking(historyList: List<TrackSimpleResponseDto>): List<RecommendItem> {
        val recommends: MutableMap<RecommendItem, Int> = mutableMapOf()
        for (history in historyList) {
            getRecommendItem(history).forEach { (k, v) -> recommends.merge(k, v, Int::plus) }
        }

        if (recommends.size < MAX_RECOMMEND_NUMBER) {
            recommends.putAll(getRandomComposer(recommends))
        }

        return recommends.entries
            .sortedByDescending { it.value }
            .take(MAX_RECOMMEND_NUMBER)
            .map { it.key }
    }

    private fun getRecommendItem(history: TrackSimpleResponseDto): MutableMap<RecommendItem, Int> {
        val recommendItems = mutableMapOf<RecommendItem, Int>()
        var recommendItem = RecommendItem(history.composer, CATEGORY_COMPOSER)
        recommendItems.put(recommendItem, recommendItems.getOrDefault(recommendItem, 0) + 1)
        for (tag in history.tags) {
            recommendItem = RecommendItem(tag, CATEGORY_TAG)
            recommendItems.put(recommendItem, recommendItems.getOrDefault(recommendItem, 0) + 1)
        }
        return recommendItems
    }

    private fun getRandomComposer(recommends: MutableMap<RecommendItem, Int>): Map<RecommendItem, Int> {
        while (recommends.size < MAX_RECOMMEND_NUMBER) {
            var isContains = false
            val randomComposer = composerRepository.findRandomComposer()
            for (key in recommends.keys) {
                if (key.content.equals(randomComposer.name)) {
                    isContains = true
                    break
                }
            } 
            if (!isContains) {
                recommends.put(RecommendItem(content = randomComposer.name, category = CATEGORY_COMPOSER), 1)
            }
        }
        return recommends
    }

    private fun getRecommendPlaylist(recommend: RecommendItem): RecommendPlaylistResponseDto {
        if (recommend.category == CATEGORY_COMPOSER) {
            return getComposerPlaylist(recommend.content)
        }
        return getTagPlaylist(recommend.content)
    }

    private fun getComposerPlaylist(composerName: String): RecommendPlaylistResponseDto {
        val composer = composerRepository.findByName(composerName)
        val imageUrl = composer.imageUrl
        val tracks = ArrayList<TrackSimpleResponseDto>()
        composer.tracks.take(MAX_RECOMMEND_NUMBER).forEach {
            tracks.add(it.toSimpleDto())
        }
        return RecommendPlaylistResponseDto(recommendTitle = composer.name, recommendImageUrl = imageUrl, recommendTracks = tracks)
    }

    private fun getTagPlaylist(content: String): RecommendPlaylistResponseDto {
        val matchingTracks = trackRepository.findByTagsContaining(content)
        val imageUrl = matchingTracks[0].thumbnailUrl
        val tracks = ArrayList<TrackSimpleResponseDto>()
        matchingTracks.take(MAX_RECOMMEND_NUMBER).forEach {
            tracks.add(it.toSimpleDto())
        }
        return RecommendPlaylistResponseDto(recommendTitle = content, recommendImageUrl = imageUrl, recommendTracks = tracks)
    }

    private data class RecommendItem(
        val content: String,
        val category: String,
    )

}