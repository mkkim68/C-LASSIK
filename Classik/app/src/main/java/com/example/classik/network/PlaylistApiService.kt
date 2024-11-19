package com.example.classik.network

import com.example.classik.data.model.AllPlaylistItem
import com.example.classik.data.model.CreatePlaylistResponse
import com.example.classik.data.model.PlaylistDetail
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

data class PlaylistTrackIdsRequest(val playlistTrackIds: List<Int>)
data class TrackIdsRequest(val trackIds: List<Int>)
data class PlaylistTitleRequest(val title: String)

interface PlaylistApiService {
    // 전체 조회
    @GET("/playlists")
    fun getAllPlaylists(
        @Header("Authorization") authorization: String,
    ): Call<List<AllPlaylistItem>>

    // 플레이리스트 생성
    @POST("/playlists")
    fun postAllPlaylists(
        @Header("Authorization") authorization: String,
        @Body title: PlaylistTitleRequest
    ): Call<CreatePlaylistResponse>

    // 단일 플레이리스트 조회
    @GET("/playlists/{playlistId}")
    fun getPlaylistById(
        @Header("Authorization") authorization: String,
        @Path("playlistId") playlistId: Int
    ): Call<PlaylistDetail>

    // 플레이리스트 삭제
    @DELETE("/playlists/{playlistId}")
    fun deletePlaylistById(
        @Header("Authorization") authorization: String,
        @Path("playlistId") playlistId: Int
    ): Call<Void>

    // 플레이리스트에 곡 추가
    @PUT("/playlists/{playlistId}")
    fun putPlaylistById(
        @Header("Authorization") authorization: String,
        @Path("playlistId") playlistId: Int,
        @Body trackIds: TrackIdsRequest
    ): Call<Void>

    // 플레이리스트에서 곡 삭제
    @HTTP(method = "DELETE", path = "/playlists/{playlistId}/tracks", hasBody = true)
    fun deleteTracksInPlaylist(
        @Header("Authorization") authorization: String,
        @Path("playlistId") playlistId: Int,
        @Body playlistTrackIds: PlaylistTrackIdsRequest
    ): Call<Void>
}