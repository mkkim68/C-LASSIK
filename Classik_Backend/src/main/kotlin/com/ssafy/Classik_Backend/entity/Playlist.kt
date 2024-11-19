package com.ssafy.Classik_Backend.entity

import jakarta.persistence.*

@Entity
class Playlist(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "playlist_id", nullable = false)
    val id: Int = 0,

    val title: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    val member: Member,

    @OneToMany(mappedBy = "playlist", cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.LAZY)
    val playlistTracks: MutableList<PlaylistTrack> = mutableListOf(),
)