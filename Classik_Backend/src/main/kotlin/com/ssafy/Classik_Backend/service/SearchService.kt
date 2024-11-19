package com.ssafy.Classik_Backend.service

import com.ssafy.Classik_Backend.dto.SearchHistoryResponseDto
import com.ssafy.Classik_Backend.dto.TrackSimpleResponseDto

interface SearchService {

    fun search(search: String): List<TrackSimpleResponseDto>
    fun getSearchHistory(): SearchHistoryResponseDto

}