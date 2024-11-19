package com.example.classik.data.model

data class AllPlaylistItem (
    val playlistId: Int,
    val playlistTitle: String,
    val trackCount: Number,
    val thumbnailUrls: List<String>
)
