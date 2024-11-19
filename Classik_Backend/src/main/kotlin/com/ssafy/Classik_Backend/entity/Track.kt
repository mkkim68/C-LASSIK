package com.ssafy.Classik_Backend.entity

import jakarta.persistence.*

@Entity
class Track(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "track_id", nullable = false)
    val id: Int,
    val title: String,
    val tags: String,
    val description: String,

    @Column(name = "video_id")
    val videoId: String,
    @Column(name = "image_url")
    val imageUrl: String?,
    @Column(name = "vr_image_url")
    val vrImageUrl: String?,
    @Column(name = "thumbnail_url")
    val thumbnailUrl: String?,

    @Lob
    @Column(name = "haptic_time", columnDefinition = "text")
    val hapticTime: String,
    @Lob
    @Column(name = "haptic_intensity", columnDefinition = "text")
    val hapticIntensity: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "composer_id")
    val composer: Composer,

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "track")
    val playlistTracks: MutableList<PlaylistTrack> = mutableListOf(),
)