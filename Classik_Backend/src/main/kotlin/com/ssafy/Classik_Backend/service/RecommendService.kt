package com.ssafy.Classik_Backend.service

import com.ssafy.Classik_Backend.dto.RecommendPlaylistResponseDto

interface RecommendService {

    fun getRecommendLists(): List<RecommendPlaylistResponseDto>

}