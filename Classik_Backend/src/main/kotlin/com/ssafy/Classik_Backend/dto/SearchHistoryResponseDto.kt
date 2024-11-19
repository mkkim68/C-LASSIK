package com.ssafy.Classik_Backend.dto

data class SearchHistoryResponseDto(
    val searchHistory: MutableList<String> = mutableListOf()
)