package com.example.classik.data.model

data class RecommendList (
    val recommendTitle: String,
    val recommendImageUrl: String,
    val recommendTracks: List<BaseTrackItem>,
)

data class BaseTrackItem (
    val trackId: Int,
    val title: String,
    val composer: String,
    val thumbnailUrl: String,
    val tags: List<String>
)