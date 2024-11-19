package com.ssafy.Classik_Backend.controller

import com.ssafy.Classik_Backend.dto.*
import com.ssafy.Classik_Backend.service.PlaylistService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/playlists")
class PlaylistController(private val playlistService: PlaylistService) {

    @GetMapping("/{playlistId}")
    fun getPlaylist(@PathVariable("playlistId") playlistId: Int): ResponseEntity<PlaylistResponseDto> {
        return ResponseEntity<PlaylistResponseDto>(playlistService.getPlaylist(playlistId), HttpStatus.OK)
    }

    @GetMapping
    fun getPlaylists(): ResponseEntity<List<PlaylistThumbnailResponseDto>> {
        return ResponseEntity<List<PlaylistThumbnailResponseDto>>(playlistService.getPlaylists(), HttpStatus.OK)
    }

    @PostMapping
    fun createPlaylist(@RequestBody requestDto: PlaylistCreateRequestDto)
            : ResponseEntity<PlaylistCreateResponseDto> {
        return ResponseEntity<PlaylistCreateResponseDto>(playlistService.createPlaylist(requestDto), HttpStatus.CREATED)
    }

    @PutMapping("/{playlistId}")
    fun updatePlaylist(@PathVariable("playlistId") playlistId: Int, @RequestBody requestDto: PlaylistUpdateRequestDto) : ResponseEntity<PlaylistResponseDto> {
        return ResponseEntity<PlaylistResponseDto>(playlistService.updatePlaylist(playlistId, requestDto), HttpStatus.OK)
    }

    @DeleteMapping("/{playlistId}")
    fun deletePlaylist(@PathVariable("playlistId") playlistId: Int) : ResponseEntity<Void> {
        playlistService.deletePlaylist(playlistId)
        return ResponseEntity<Void>(HttpStatus.OK)
    }

    @DeleteMapping("/{playlistId}/tracks")
    fun deletePlaylistTrack(
        @PathVariable("playlistId") playlistId: Int,
        @RequestBody playlistTracksDeleteRequestDto: PlaylistTracksDeleteRequestDto,
    ) : ResponseEntity<Void> {
        playlistService.deletePlaylistTrack(playlistTracksDeleteRequestDto)
        return ResponseEntity<Void>(HttpStatus.OK)
    }

}