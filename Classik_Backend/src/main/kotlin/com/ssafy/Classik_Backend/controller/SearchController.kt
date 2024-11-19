package com.ssafy.Classik_Backend.controller

import com.ssafy.Classik_Backend.dto.SearchHistoryResponseDto
import com.ssafy.Classik_Backend.dto.TrackSimpleResponseDto
import com.ssafy.Classik_Backend.service.SearchService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/search")
class SearchController(
    private val searchService: SearchService
) {

    @GetMapping("/{search}")
    fun search(@PathVariable search: String): ResponseEntity<List<TrackSimpleResponseDto>> {
        return ResponseEntity(searchService.search(search), HttpStatus.OK)
    }

    @GetMapping
    fun getSearchHistory(): ResponseEntity<SearchHistoryResponseDto> {
        return ResponseEntity(searchService.getSearchHistory(), HttpStatus.OK)
    }
}