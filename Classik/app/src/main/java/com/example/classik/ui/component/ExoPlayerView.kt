package com.example.classik.ui.component

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.PlayerView

@Composable
fun ExoPlayerView(videoUri: Uri, modifier: Modifier = Modifier) {
    val context = LocalContext.current

    // ExoPlayer 초기화
    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            setMediaItem(MediaItem.fromUri(videoUri))
            repeatMode = Player.REPEAT_MODE_ONE // 무한 루프
            playWhenReady = true // 자동 재생
            prepare()
        }
    }

    // PlayerView를 AndroidView로 설정
    AndroidView(
        factory = {
            PlayerView(context).apply {
                player = exoPlayer
                useController = false // 플레이어 컨트롤러 사용 여부
            }
        },
        modifier = modifier
    )

    // 화면에서 벗어날 때 플레이어 해제
    DisposableEffect(Unit) {
        onDispose {
            exoPlayer.release()
        }
    }
}
