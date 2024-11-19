package com.ssafy.Classik_Backend.controller

import com.ssafy.Classik_Backend.dto.RecommendPlaylistResponseDto
import com.ssafy.Classik_Backend.service.RecommendService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/recommends")
class RecommendController(private val recommendService: RecommendService) {

    @GetMapping
    fun recommendTracks(): ResponseEntity<List<RecommendPlaylistResponseDto>> {
        return ResponseEntity<List<RecommendPlaylistResponseDto>>(recommendService.getRecommendLists(), HttpStatus.OK)
    }

}