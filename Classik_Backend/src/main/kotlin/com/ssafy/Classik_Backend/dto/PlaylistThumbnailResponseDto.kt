package com.ssafy.Classik_Backend.dto

data class PlaylistThumbnailResponseDto(
    val playlistId: Int,
    val playlistTitle: String,
    val trackCount: Int,
    val thumbnailUrls: List<String?>,
)