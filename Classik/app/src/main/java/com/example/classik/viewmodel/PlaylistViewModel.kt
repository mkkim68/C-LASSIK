package com.example.classik.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.classik.data.model.AllPlaylistItem
import com.example.classik.data.model.CreatePlaylistResponse
import com.example.classik.data.model.PlaylistDetail
import com.example.classik.network.ApiService
import com.example.classik.network.PlaylistTitleRequest
import com.example.classik.network.PlaylistTrackIdsRequest
import com.example.classik.network.TrackIdsRequest
import com.example.classik.utils.LocalStorageManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PlaylistViewModel : ViewModel() {

    private val _playlists = MutableLiveData<List<AllPlaylistItem>>(emptyList())
    val playlists: LiveData<List<AllPlaylistItem>> get() = _playlists

    private val _selectedPlaylist = MutableLiveData<PlaylistDetail>()
    val selectedPlaylist: LiveData<PlaylistDetail> get() = _selectedPlaylist

    val itemCount = MediatorLiveData<Int>().apply {
        addSource(_playlists) { list ->
            value = list?.size ?: 0
        }
    }

//    init {
//        _playlists.value = listOf(
//            AllPlaylistItem(
//                1,
//                "출근길 플리",
//                23,
//                listOf(
//                    Thumbnails("https://classik-bucket.s3.ap-northeast-2.amazonaws.com/_hyAOYMUVDs"),
//                    Thumbnails("https://classik-bucket.s3.ap-northeast-2.amazonaws.com/9PFGpPzu_7s"),
//                    Thumbnails("https://classik-bucket.s3.ap-northeast-2.amazonaws.com/k5cCjVI7LmY"),
//                )
//            ),
//            AllPlaylistItem(
//                2,
//                "유태리듬",
//                23,
//                listOf(
//                    Thumbnails("https://classik-bucket.s3.ap-northeast-2.amazonaws.com/_hyAOYMUVDs"),
//                    Thumbnails("https://classik-bucket.s3.ap-northeast-2.amazonaws.com/9PFGpPzu_7s"),
//                    Thumbnails("https://classik-bucket.s3.ap-northeast-2.amazonaws.com/9PFGpPzu_7s"),
//                )
//            ),
//            AllPlaylistItem(
//                3,
//                "유성주주클럽",
//                23,
//                listOf(
//                    Thumbnails("https://classik-bucket.s3.ap-northeast-2.amazonaws.com/k5cCjVI7LmY"),
//                    Thumbnails("https://classik-bucket.s3.ap-northeast-2.amazonaws.com/9PFGpPzu_7s"),
//                    Thumbnails("https://classik-bucket.s3.ap-northeast-2.amazonaws.com/_hyAOYMUVDs"),
//                )
//            ),
//            AllPlaylistItem(
//                4,
//                "두 곡 저장된 플리",
//                2,
//                listOf(
//                    Thumbnails("https://classik-bucket.s3.ap-northeast-2.amazonaws.com/_hyAOYMUVDs"),
//                    Thumbnails("https://classik-bucket.s3.ap-northeast-2.amazonaws.com/k5cCjVI7LmY"),
//                )
//            ),
//            AllPlaylistItem(
//                5,
//                "한 곡 저장된 플리",
//                1,
//                listOf(
//                    Thumbnails("https://classik-bucket.s3.ap-northeast-2.amazonaws.com/_hyAOYMUVDs"),
//                )
//            ),
//        )
//    }


    // 모든 플레이리스트 조회
    fun fetchPlaylists(context: Context) {
        val authorization = LocalStorageManager.getAccessToken(context)
        ApiService.playlistApi.getAllPlaylists("Bearer $authorization").enqueue(object : Callback<List<AllPlaylistItem>> {
            override fun onResponse(call: Call<List<AllPlaylistItem>>, response: Response<List<AllPlaylistItem>>) {
                if (response.isSuccessful) {
                    _playlists.value = response.body()
                    Log.d("Playlist", "성공: ${response.body()}")
                } else {
                    // 에러 처리
                    Log.d("Playlist", "실패: $response")
                }
            }

            override fun onFailure(call: Call<List<AllPlaylistItem>>, t: Throwable) {
                // 네트워크 또는 기타 오류 처리
                Log.d("Playlist", "오류: $t")
            }
        })
    }

    // 새로운 플레이리스트 생성
    fun createPlaylist(title: String, context: Context) {
        val authorization = LocalStorageManager.getAccessToken(context)
        val titleRequest = PlaylistTitleRequest(title = title)
        ApiService.playlistApi.postAllPlaylists( "Bearer $authorization",titleRequest).enqueue(object : Callback<CreatePlaylistResponse> {
            override fun onResponse(call: Call<CreatePlaylistResponse>, response: Response<CreatePlaylistResponse>) {
                if (response.isSuccessful) {
                    fetchPlaylists(context)
                    Log.d("Playlist Create", "성공: ${response.body()}")
                } else {
                    // 에러 처리
                    Log.d("Playlist Create", "실패: $response")
                }
            }

            override fun onFailure(call: Call<CreatePlaylistResponse>, t: Throwable) {
                // 네트워크 또는 기타 오류 처리
                Log.d("Playlist Create", "실패: $t")
            }
        })
    }


    private val _fetchPlaylist = MutableLiveData<Boolean>()
    val fetchPlaylist: LiveData<Boolean> get() = _fetchPlaylist

    // 단일 플레이리스트 조회
    fun fetchPlaylistById(playlistId: Int, context: Context, onSuccess: () -> Unit) {
        val authorization = LocalStorageManager.getAccessToken(context)
        ApiService.playlistApi.getPlaylistById("Bearer $authorization", playlistId).enqueue(object : Callback<PlaylistDetail> {
            override fun onResponse(call: Call<PlaylistDetail>, response: Response<PlaylistDetail>) {
                if (response.isSuccessful) {
                    _selectedPlaylist.value = response.body()
                    _fetchPlaylist.value = true
                    Log.d("Playlist Detail", "성공: ${response.body()}")
                    onSuccess()
                } else {
                    _fetchPlaylist.value = false
                    Log.d("Playlist Detail", "실패: $response")
                }
            }

            override fun onFailure(call: Call<PlaylistDetail>, t: Throwable) {
                Log.d("Playlist Detail", "오류: $t")
                _fetchPlaylist.value = false
            }
        })
    }

    private val _deletePlaylistSuccess = MutableLiveData<Boolean>()
    val deletePlaylistSuccess: LiveData<Boolean> get() = _deletePlaylistSuccess

    // 플레이리스트 삭제
    fun deletePlaylistById(playlistId: Int, context: Context) {
        val authorization = LocalStorageManager.getAccessToken(context)
        ApiService.playlistApi.deletePlaylistById("Bearer $authorization", playlistId).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    fetchPlaylists(context)
                    _deletePlaylistSuccess.value = true
                    Log.d("Playlist Delete", "성공: ${response.body()}")
                } else {
                    Log.d("Playlist Delete", "실패: $response")
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.d("Playlist Delete", "오류: $t")
            }
        })
    }

    // 플레이리스트에 곡 추가
    fun addTracksToPlaylist(playlistId: Int, trackIds: List<Int>, context: Context) {
        val authorization = LocalStorageManager.getAccessToken(context)
        ApiService.playlistApi.putPlaylistById("Bearer $authorization", playlistId, TrackIdsRequest(trackIds)).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    fetchPlaylistById(playlistId, context) {}
                    fetchPlaylists(context)
                    Log.d("Playlist Add", "성공: ${response.body()}")
                } else {
                    Log.d("Playlist Add", "실패: $response")
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.d("Playlist Add", "오류: $t")
            }
        })
    }

    // 플레이리스트에서 곡 삭제
    fun deleteTracksInPlaylist(playlistId: Int, trackIds: List<Int>, context: Context) {
        val request = PlaylistTrackIdsRequest(playlistTrackIds = trackIds)
        val authorization = LocalStorageManager.getAccessToken(context)

        ApiService.playlistApi.deleteTracksInPlaylist("Bearer $authorization", playlistId, request).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    fetchPlaylistById(playlistId, context) {}
                    fetchPlaylists(context)
                    Log.d("Playlist Delete Track", "성공: ${response.body()}")
                } else {
                    Log.d("Playlist Delete Track", "실패: $response")
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.d("Playlist Delete Track", "오류: $t")
            }
        })
    }
}
