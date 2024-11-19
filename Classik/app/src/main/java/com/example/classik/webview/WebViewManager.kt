// WebViewManager.kt
package com.example.classik.ui.component

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.classik.utils.HapticFeedbackUtil

object WebViewManager {
    private var webView: WebView? = null
    private var appContext: Context? = null

    private val _isPlaying = MutableLiveData(false)
    val isPlaying: LiveData<Boolean> = _isPlaying
    private val _currentTime = MutableLiveData(0f)
    val currentTime: LiveData<Float> = _currentTime
    private val _duration = MutableLiveData(1f)
    val duration: LiveData<Float> = _duration
    private val _isMuted = MutableLiveData(false)
    val isMuted: LiveData<Boolean> = _isMuted
    private val _isEnded = MutableLiveData(false)
    val isEnded: LiveData<Boolean> = _isEnded

    private val _videoId = MutableLiveData<String>()
    val videoId: LiveData<String> get() = _videoId

    private var isInitialized = false

    // Haptic 피드백을 위한 정보
    private var hapticTimeList: List<Float> = emptyList() // 서버에서 받은 시간 리스트
    private var hapticIntensityList: List<Int> = emptyList() // 서버에서 받은 세기 리스트

    private val _hapticEnabled = MutableLiveData(true) // 햅틱 기능 활성화 여부
    val hapticEnabled: LiveData<Boolean> = _hapticEnabled

    fun mute() {
        webView?.evaluateJavascript("javascript:player.mute();", null)
        _isMuted.value = true
    }

    fun unmute() {
        webView?.evaluateJavascript("javascript:player.unMute();", null)
        _isMuted.value = false
    }

    fun setHapticFeedbackInfo(hapticTime: List<Float>, hapticIntensity: List<Int>) {
        hapticTimeList = hapticTime
        hapticIntensityList = hapticIntensity
    }

    fun toggleHapticEnabled() {
        _hapticEnabled.value = !_hapticEnabled.value!!
    }


    fun setVideoId(context: Context, newVideoId: String) {
        if (webView == null || !isInitialized) {
            initialize(context, newVideoId)
        } else {
            loadNewVideo(newVideoId)

            // Force a duration update after a small delay
            Handler(Looper.getMainLooper()).postDelayed({
                webView?.evaluateJavascript("javascript:checkAndSetDuration();", null)
            }, 500)
        }
    }


    private class AndroidInterface {
        @JavascriptInterface
        fun updateTime(time: Float) {
            _currentTime.postValue(time)
            checkAndTriggerHapticFeedback(time)
        }

        @JavascriptInterface
        fun setDuration(duration: Float) {
            _duration.postValue(duration)
        }

        @JavascriptInterface
        fun updateIsPlaying(isPlaying: Boolean) {
            if (isPlaying) {
                _isPlaying.postValue(true)
            } else {
                _isPlaying.postValue(false)
            }
        }

        @JavascriptInterface
        fun setIsEnded(isEnded: Boolean) {
            // ENDED 상태 여부를 업데이트
            _isEnded.postValue(isEnded)
        }
    }


    fun initialize(context: Context, videoId: String) {
            appContext = context.applicationContext
            val htmlData = getHTMLData(videoId)
            webView = WebView(context).apply {
                settings.javaScriptEnabled = true
                settings.domStorageEnabled = true
                settings.loadWithOverviewMode = true
                settings.useWideViewPort = true
                settings.mediaPlaybackRequiresUserGesture = false
                webViewClient = WebViewClient()
                addJavascriptInterface(AndroidInterface(), "AndroidInterface")

                loadDataWithBaseURL(
                    "https://www.youtube.com",
                    htmlData,
                    "text/html",
                    "utf-8",
                    null
                )
            }
        isInitialized = true
    }

    @Composable
    fun WebViewContainer(videoId: String) {
        val context = LocalContext.current
        LaunchedEffect(Unit) {
            initialize(context, videoId)
        }

        AndroidView(factory = { webView ?: WebView(context) })
    }


    fun playVideo() {
        webView?.evaluateJavascript("javascript:playVideo();", null)
        _isPlaying.value = true
    }

    fun pauseVideo() {
        webView?.evaluateJavascript("javascript:pauseVideo();", null)
        _isPlaying.value = false
    }

    private fun loadNewVideo(videoId: String) {
        webView?.evaluateJavascript("javascript:player.loadVideoById('$videoId');", null)
    }

    fun seekTo(time: Float) {
        webView?.evaluateJavascript("javascript:seekTo($time);", null)
        _currentTime.value = time
    }

    // You can add a method to get the video duration
    private fun getHTMLData(videoId: String): String {
        return """
        <html>
            <body style="margin:0px;padding:0px;">
               <div id="player"></div>
                <script>
                    var player;
                    function onYouTubeIframeAPIReady() {
                        player = new YT.Player('player', {
                            height: '0',
                            width: '0',
                            videoId: '$videoId',
                            playerVars: {
                                'playsinline': 1
                            },
                            events: {
                                'onReady': onPlayerReady,
                                'onStateChange': onPlayerStateChange
                            }
                        });
                    }
                    
                    function onPlayerStateChange(event) {
                        if (event.data == YT.PlayerState.PLAYING) {
                            AndroidInterface.setIsEnded(false);
                            AndroidInterface.updateIsPlaying(true); // 재생 중
                        } else if (event.data == YT.PlayerState.PAUSED) {
                            AndroidInterface.setIsEnded(false);
                            AndroidInterface.updateIsPlaying(false); // 일시 정지 또는 멈춤 상태
                        } else if (event.data == YT.PlayerState.ENDED) {
                            AndroidInterface.setIsEnded(true);
                            AndroidInterface.updateIsPlaying(false);
                        }
                    }
                    
                    function onPlayerReady(event) {
                        event.target.setVolume(100);
                        var videoDuration = player.getDuration();
                        AndroidInterface.setDuration(videoDuration);
                        updateProgress();
                    }
                    function seekTo(time) {
                        player.seekTo(time, true);
                    }
                      function playVideo() {
                        player.playVideo();
                    }
                    function pauseVideo() {
                        player.pauseVideo();
                    }
                    function nextVideo() {
                        player.nextVideo();
                    }
                    function previousVideo() {
                        player.previousVideo();
                    }
                    function loadVideoById(videoId) {
                        player.loadVideoById(videoId);
                        checkAndSetDuration();
                        updateProgress();
                    }
                    
                    function checkAndSetDuration() {
                        var videoDuration = player.getDuration();
                        if (videoDuration > 0) { // Duration이 0보다 클 때만 설정
                            AndroidInterface.setDuration(videoDuration);
                        } else {
                            // Duration이 아직 0인 경우 500ms 후에 다시 시도
                            setTimeout(checkAndSetDuration, 500);
                        }
                    }

                    function updateProgress() {
                        setInterval(function() {
                            var currentTime = player.getCurrentTime();
                            AndroidInterface.updateTime(currentTime);
                            
                            checkAndTriggerHapticFeedback(currentTime);
                        }, 100);
                    }
                </script>
                <script src="https://www.youtube.com/iframe_api"></script>
            </body>
        </html>
    """.trimIndent()
    }

    // 햅틱 관련
    private var lastTriggeredIndex = -1

    private fun checkAndTriggerHapticFeedback(currentTime: Float) {
        if (_hapticEnabled.value == false) return
        // hapticTime 리스트에서 현재 재생 시간과 일치하는 인덱스 찾기
        hapticTimeList.forEachIndexed { index, hapticTime ->
            if (hapticTime <= currentTime && index > lastTriggeredIndex) {
                lastTriggeredIndex = index // 현재 인덱스를 마지막 발생 인덱스로 업데이트

                val intensity = hapticIntensityList.getOrElse(index) { 0 } // Intensity 값 얻기
                appContext?.let { context ->
                    HapticFeedbackUtil.triggerHapticFeedback(context, intensity) // Haptic Feedback 실행
                }
            }
        }
    }


}