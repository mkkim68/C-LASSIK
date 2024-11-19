package com.ssafy.Classik_Backend.controller

import com.ssafy.Classik_Backend.dto.TrackSimpleResponseDto
import com.ssafy.Classik_Backend.service.PlaybackHistoryService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/history")
class PlaybackHistoryController(private val playbackHistoryService: PlaybackHistoryService) {

    @GetMapping
    fun getHistory() : ResponseEntity<List<TrackSimpleResponseDto>>{
        return ResponseEntity(playbackHistoryService.getHistoryList(), HttpStatus.OK)
    }

}