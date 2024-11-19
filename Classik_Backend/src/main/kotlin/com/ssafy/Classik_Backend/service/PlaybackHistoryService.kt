package com.ssafy.Classik_Backend.service

import com.ssafy.Classik_Backend.dto.TrackSimpleResponseDto

interface PlaybackHistoryService {

    fun getHistoryList() : List<TrackSimpleResponseDto>

}