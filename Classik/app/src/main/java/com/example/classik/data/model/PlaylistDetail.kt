package com.example.classik.data.model

data class PlaylistDetail (
    val playlistId: Int,
    val playlistTitle: String,
    val trackCount: Number,
    val tracks: List<PlaylistItem>
)
