package com.ssafy.Classik_Backend.dto

import com.ssafy.Classik_Backend.entity.Track
import com.ssafy.Classik_Backend.util.splitByComma

data class TrackSimpleResponseDto(
    val trackId: Int,
    val title: String,
    val composer: String,
    val tags: List<String>,
    val thumbnailUrl: String?
)

fun Track.toSimpleDto(): TrackSimpleResponseDto {
    return TrackSimpleResponseDto(
        trackId = this.id,
        title = this.title,
        composer = this.composer.name,
        tags = splitByComma(this.tags),
        thumbnailUrl = this.thumbnailUrl
    )
}