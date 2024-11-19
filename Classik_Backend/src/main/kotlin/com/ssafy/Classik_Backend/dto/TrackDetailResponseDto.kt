package com.ssafy.Classik_Backend.dto

import com.ssafy.Classik_Backend.entity.Track
import com.ssafy.Classik_Backend.util.splitByComma

data class TrackDetailResponseDto(
    val trackId: Int,
    val title: String,
    val composer: String,
    val description: String,
    val tags: List<String>,
    val videoId: String,
    val imageUrl: String?,
    val vrImageUrl: String?,
    val thumbnailUrl: String?,
    val hapticTime: List<String>,
    val hapticIntensity: List<String>,
)

fun Track.toDetailDto(): TrackDetailResponseDto {
    return TrackDetailResponseDto(
        trackId = this.id,
        title = this.title,
        composer = this.composer.name,
        description = this.description,
        tags = splitByComma(this.tags),
        videoId = this.videoId,
        imageUrl = this.imageUrl,
        vrImageUrl = this.vrImageUrl,
        thumbnailUrl = this.thumbnailUrl,
        hapticTime = splitByComma(this.hapticTime),
        hapticIntensity = splitByComma(this.hapticIntensity)
    )
}