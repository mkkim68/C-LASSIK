package com.ssafy.Classik_Backend.entity

import jakarta.persistence.*

@Entity
class Composer(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "composer_id", nullable = false)
    val id: Int,

    val name: String,

    @Column(name = "image_url", nullable = false)
    val imageUrl: String,

    @Column(columnDefinition = "text")
    val description: String,

    @OneToMany(mappedBy = "composer", cascade = [CascadeType.ALL], orphanRemoval = true)
    val tracks: MutableSet<Track> = mutableSetOf(),
)