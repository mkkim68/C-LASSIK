package com.ssafy.Classik_Backend.entity

import jakarta.persistence.*

@Entity(name="PlaybackHistory")
class PlaybackHistory(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "history_id", nullable = false)
    val id: Int = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    val member: Member,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "track_id", nullable = false)
    val track: Track,

    @Column(name = "score", nullable = false)
    val score: Double = System.currentTimeMillis().toDouble(),

    @Column(name = "is_deleted", nullable = false)
    var isDeleted: Boolean = false,
)
