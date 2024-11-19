// MusicViewModel.kt
package com.example.classik.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.classik.data.model.LastPlayedTrack
import com.example.classik.data.model.MusicDetail
import com.example.classik.data.model.NowPlayingList
import com.example.classik.network.ApiService
import com.example.classik.ui.component.WebViewManager
import com.example.classik.utils.LocalStorageManager
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class MusicViewModel @Inject constructor(@ApplicationContext private val context: Context) : ViewModel() {
    private val webViewManager = WebViewManager

    val isPlaying: LiveData<Boolean> = webViewManager.isPlaying
    val currentTime: LiveData<Float> = webViewManager.currentTime
    val duration: LiveData<Float> = webViewManager.duration
    val hapticEnabled: LiveData<Boolean> = webViewManager.hapticEnabled
    val isMuted: LiveData<Boolean> = webViewManager.isMuted

    private var isInitialLoad = true

    private val _defaultPlaylist = MutableLiveData<List<MusicDetail>>()
    val defaultPlaylist: LiveData<List<MusicDetail>> get() = _defaultPlaylist
    val defaultItemCount = MediatorLiveData<Int>().apply {
        addSource(defaultPlaylist) { playlist ->
            value = playlist.size
        }
    }

    private val _nowPlayingList = MutableLiveData<NowPlayingList>()
    val nowPlayingList: LiveData<NowPlayingList> = _nowPlayingList

    private val _lastPlayedTrack = MutableLiveData<LastPlayedTrack>()
    val lastPlayedTrack: LiveData<LastPlayedTrack> = _lastPlayedTrack

    private val _lastPlayedVideoId = MutableLiveData<String>()
    val lastPlayedVideoId: LiveData<String> get() = _lastPlayedVideoId

    private val _musicDetail = MutableLiveData<MusicDetail>()
    val musicDetail: LiveData<MusicDetail> get() = _musicDetail

    private fun observeIsEnded() {
        WebViewManager.isEnded.observeForever { isEnded ->
            if (isEnded) {
                playNext(context)
            }
        }
    }

    init {
        loadLastPlayedTrack(context)
        loadLastPlayedVideoId(context)
        loadNowPlayingList(context)
        loadDefaultPlaylist()
        observeIsEnded()
    }

    // 기본 재생목록 로드
    fun loadDefaultPlaylist() {
        _defaultPlaylist.value = LocalStorageManager.loadPlaylist(context)
    }

    // 기본 재생목록에서 곡 제거
    fun removeTracksByIndex(context: Context, indicesToRemove: List<Int>) {
        // 현재 default playlist 로드
        val currentPlaylist = LocalStorageManager.loadPlaylist(context)

        // 주어진 인덱스 목록에 없는 트랙만 필터링하여 새로운 리스트 생성
        val updatedPlaylist = currentPlaylist.filterIndexed { index, _ -> index !in indicesToRemove }

        // 업데이트된 리스트를 저장
        LocalStorageManager.savePlaylist(context, updatedPlaylist)

        // 변경된 리스트를 LiveData에 반영하여 UI 갱신
        _defaultPlaylist.value = updatedPlaylist
    }


    fun updateDefaultPlaylist(newPlaylist: List<MusicDetail>) {
        LocalStorageManager.savePlaylist(context, newPlaylist)
        loadDefaultPlaylist()  // 업데이트 후 다시 로드
    }

    // 추가 기능: 특정 트랙을 선택하고 재생목록을 업데이트
    fun selectTrackAndMoveToTop(selectedTrack: MusicDetail) {
        LocalStorageManager.saveSelectedTrackToTop(context, selectedTrack)
        loadDefaultPlaylist() // 재생목록이 업데이트되므로 다시 로드
    }

    fun toggleHapticEnabled() {
        webViewManager.toggleHapticEnabled()
    }

    fun toggleMute() {
        if (isMuted.value == true) {
            webViewManager.unmute()
        } else {
            webViewManager.mute()
        }
    }

    // 마지막 재생 곡
    fun saveLastPlayedTrack(context: Context, track: MusicDetail, index: Int) {
        val newLast = LastPlayedTrack(
            index = index,
            trackId = track.trackId,
            title = track.title,
            composer = track.composer,
            description = track.description,
            tags = track.tags,
            videoId = track.videoId,
            imageUrl = track.imageUrl,
            vrImageUrl = track.vrImageUrl,
            thumbnailUrl = track.thumbnailUrl,
            hapticTime = track.hapticTime,
            hapticIntensity = track.hapticIntensity
        )
        _lastPlayedTrack.value = newLast
        LocalStorageManager.saveLastPlayedTrack(context, track, index)
    }

    fun saveLastPlayedTrackInPlaylist(context: Context, track: LastPlayedTrack) {
        _lastPlayedTrack.value = track
        LocalStorageManager.saveLastPlayedTrackInPlaylist(context, track)
    }

    private fun loadLastPlayedVideoId(context: Context) {
        val lastPlayedTrack = LocalStorageManager.getLastPlayedTrack(context)
        _lastPlayedVideoId.value = lastPlayedTrack?.videoId ?: "gvXsmI3Gdq8"
    }

    fun loadLastPlayedTrack(context: Context) {
        _lastPlayedTrack.value = LocalStorageManager.getLastPlayedTrack(context)
    }

    // 재생 관련
    fun play() {
        _lastPlayedTrack.value?.let { track ->
            // 진동 시간과 세기 설정
            webViewManager.setHapticFeedbackInfo(
                hapticTime = track.hapticTime.mapNotNull { it.toFloatOrNull() },
                hapticIntensity = track.hapticIntensity.mapNotNull { it.toIntOrNull() }
            )
            webViewManager.playVideo()
        }
    }

    fun pause() {
        WebViewManager.pauseVideo()
    }

    fun seekTo(time: Float) = webViewManager.seekTo(time)

    fun playNext(context: Context) {
        val (nextTrack, nextIndex) = LocalStorageManager.loadNextTrack(context)
        nextTrack?.let {
            fetchMusicDetail(it.trackId) {
                webViewManager.setVideoId(context, it.videoId)
                saveLastPlayedTrack(context, it, nextIndex ?: 0)
            }
        }
    }

    fun playPrevious(context: Context) {
        val (previousTrack, previousIndex) = LocalStorageManager.loadPreviousTrack(context)
        previousTrack?.let {
            fetchMusicDetail(it.trackId) {
                webViewManager.setVideoId(context, it.videoId)
                saveLastPlayedTrack(context, it, previousIndex ?: 0)
            }
        }
    }

    // 재생중인 목록
    fun loadNowPlayingList(context: Context) {
        _nowPlayingList.value = LocalStorageManager.getNowPlayingList(context)
    }

    fun setNowPlayingList(context: Context, playlistType: String, playlistId: Int, playlist: List<MusicDetail>) {
        val newNowPlayingList = NowPlayingList(
            playlistType = playlistType,
            playlistId = playlistId,
            playlist = playlist
        )
        LocalStorageManager.saveNowPlayingList(context, newNowPlayingList)
        _nowPlayingList.value = newNowPlayingList
    }

    // fetch
    fun fetchMusicDetail(trackId: Int, onSuccess: () -> Unit) {
        val authorization = LocalStorageManager.getAccessToken(context)
        ApiService.musicPlaybackApi.getMusicDetail("Bearer $authorization",trackId).enqueue(object : Callback<MusicDetail> {
            override fun onResponse(call: Call<MusicDetail>, response: Response<MusicDetail>) {
                if (response.isSuccessful) {
                    _musicDetail.value = response.body()
                    Log.d("fetchMusicDetail 성공", "${response.body()}")
                    onSuccess()
                } else {
                    // Handle error response
                    Log.d("fetchMusicDetail 실패", response.toString())
                }
            }

            override fun onFailure(call: Call<MusicDetail>, t: Throwable) {
                // Handle failure (e.g., network error)
                Log.d("fetchMusicDetail network 에러", t.toString())
            }
        })
    }

    fun updateMusicDetail(newMusic: MusicDetail) {
        _musicDetail.value = newMusic
    }

    override fun onCleared() {
        WebViewManager.isEnded.removeObserver { /* Observer 해제 */ }
        super.onCleared()
    }
}
