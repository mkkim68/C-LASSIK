package com.ssafy.Classik_Backend.dto

data class RecommendPlaylistResponseDto(
    val recommendTitle: String,
    val recommendImageUrl: String?,
    val recommendTracks: List<TrackSimpleResponseDto>,
)