package com.ssafy.Classik_Backend.dto

data class TracksFromSearchingServerDto(
    val items: ArrayList<List<SearchedTrack>> = ArrayList()
)

data class SearchedTrack(
    val composer: String = "",
    val tags: String = "",
    val title: String = "",
)
