package com.example.classik.data.model

data class LastPlayedTrack (
    val index: Int,  // playlist에서 어느 index인지
    val trackId: Int,
    val title: String,
    val composer: String,
    val description: String,
    val tags: List<String>,
    val videoId: String,
    val imageUrl: String?,
    val vrImageUrl: String,
    val thumbnailUrl: String,
    val hapticTime: List<String>,
    val hapticIntensity: List<String>
)

data class NowPlayingList (
    val playlistType: String, // "default"(default인지) or "custom" (사용자가 생성한 playlist인지)
    val playlistId: Int, // 사용자가 생성한 playlist면 그 playlistId를 할 건데 default면 무슨 값을 넣어주지
    val playlist: List<MusicDetail>
)