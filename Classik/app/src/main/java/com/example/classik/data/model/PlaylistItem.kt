package com.example.classik.data.model

data class PlaylistItem(
    val trackId: Int,
    val title: String,
    val composer: String,
    val thumbnailUrl: String,
    val playlistTrackId: Int,
    val tags: List<String>,
    val description: String
)