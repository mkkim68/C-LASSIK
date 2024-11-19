package com.ssafy.Classik_Backend.dto

data class PlaylistResponseDto(
    val playlistTitle: String,
    val playlistId: Int,
    val trackCount: Int,
    val tracks: MutableList<PlaylistTrackResponseDto>,
)