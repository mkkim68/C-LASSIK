package com.example.classik.data.model

data class MusicDetail (
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