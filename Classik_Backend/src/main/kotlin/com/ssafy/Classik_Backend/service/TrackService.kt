package com.ssafy.Classik_Backend.service

import com.ssafy.Classik_Backend.dto.TrackDetailResponseDto
import com.ssafy.Classik_Backend.entity.Track

interface TrackService {

    fun getTrack(trackId: Int): TrackDetailResponseDto
    fun searchTracks(word: String): List<Track>

}