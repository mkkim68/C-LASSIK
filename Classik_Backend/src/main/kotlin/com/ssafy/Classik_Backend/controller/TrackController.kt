package com.ssafy.Classik_Backend.controller

import com.ssafy.Classik_Backend.dto.TrackDetailResponseDto
import com.ssafy.Classik_Backend.entity.Track
import com.ssafy.Classik_Backend.service.TrackService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/tracks")
class TrackController(private val trackService: TrackService) {

    @GetMapping("/search")
    fun searchTrack(@RequestParam word: String): ResponseEntity<List<Track>> {
        return ResponseEntity<List<Track>>(trackService.searchTracks(word), HttpStatus.OK)
    }

    @GetMapping("/{trackId}")
    fun getTrack(@PathVariable trackId: Int): ResponseEntity<TrackDetailResponseDto> {
        return ResponseEntity<TrackDetailResponseDto>(trackService.getTrack(trackId), HttpStatus.OK)
    }

}