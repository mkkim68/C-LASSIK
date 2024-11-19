package com.ssafy.Classik_Backend.dto

import com.ssafy.Classik_Backend.entity.PlaylistTrack
import com.ssafy.Classik_Backend.util.splitByComma

data class PlaylistTrackResponseDto(
    val trackId: Int,
    val playlistTrackId: Int,
    val title: String,
    val composer: String,
    val tags: List<String>,
    val description: String,
    val thumbnailUrl: String?
)

fun PlaylistTrack.toPlaylistTrackDto(): PlaylistTrackResponseDto {
    return PlaylistTrackResponseDto(
        trackId = this.track.id,
        playlistTrackId = this.id,
        title = this.track.title,
        composer = this.track.composer.name,
        tags = splitByComma(this.track.tags),
        description = this.track.description,
        thumbnailUrl = this.track.thumbnailUrl
    )
}