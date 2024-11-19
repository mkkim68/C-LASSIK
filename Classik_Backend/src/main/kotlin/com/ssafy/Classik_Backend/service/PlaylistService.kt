package com.ssafy.Classik_Backend.service

import com.ssafy.Classik_Backend.dto.*

interface PlaylistService {

    fun getPlaylist(playlistId: Int): PlaylistResponseDto
    fun getPlaylists(): List<PlaylistThumbnailResponseDto>
    fun createPlaylist(requestDto: PlaylistCreateRequestDto): PlaylistCreateResponseDto
    fun updatePlaylist(playlistId: Int, requestDto: PlaylistUpdateRequestDto): PlaylistResponseDto
    fun deletePlaylist(playlistId: Int)
    fun deletePlaylistTrack(playlistTracksDeleteRequestDto: PlaylistTracksDeleteRequestDto)

}