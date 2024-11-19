package com.example.classik.ui.screen

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.palette.graphics.Palette
import coil.compose.AsyncImage
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.classik.R
import com.example.classik.ui.component.PlaylistComponent
import com.example.classik.ui.theme.DarkGray
import com.example.classik.ui.theme.LightGray
import com.example.classik.ui.theme.MainColor
import com.example.classik.ui.theme.PlayBar
import com.example.classik.ui.theme.SuperLightGray
import com.example.classik.ui.theme.White
import com.example.classik.ui.theme.pretendardBold
import com.example.classik.ui.theme.pretendardRegular
import com.example.classik.viewmodel.MusicViewModel
import com.example.classik.viewmodel.PlaylistViewModel
import android.widget.Toast
import android.view.InputDevice
import com.unity3d.player.UnityPlayerActivity
import com.unity3d.player.UnityPlayer

@OptIn(ExperimentalMaterial3Api::class)
@Composable

fun MusicScreen(navController: NavController, viewModel: MusicViewModel, playlistViewModel: PlaylistViewModel) {
    val context = LocalContext.current as ComponentActivity
    val musicDetail by viewModel.lastPlayedTrack.observeAsState()

//    val videoUri = Uri.parse(musicDetail!!.imageUrl)

    val isPlaying by viewModel.isPlaying.observeAsState(false)
    val currentTime by viewModel.currentTime.observeAsState(0f)
    val duration by viewModel.duration.observeAsState(1f)

    var isPlaylistVisible by remember { mutableStateOf(false) }
    val hapticEnabled by viewModel.hapticEnabled.observeAsState(true)
    val isMute by viewModel.isMuted.observeAsState(false)

    var colors by remember { mutableStateOf<List<Color>>(emptyList()) }

    LaunchedEffect(musicDetail!!.vrImageUrl) {
        extractDominantColorsFromImageUrl(context, musicDetail!!.vrImageUrl) { extractedColors ->
            colors = extractedColors
        }
    }

    val playBarWidth by animateFloatAsState(
        targetValue = if (isPlaylistVisible) 0.7f else 1f,
        animationSpec = tween(durationMillis = 300)
    )
    var nowTime by remember {
        mutableStateOf(0f)
    }

    val animatedCurrentTime by animateFloatAsState(targetValue = nowTime)
    LaunchedEffect(currentTime, duration) {
        if (duration > 1f && 0f < currentTime && currentTime <= duration) {
            // 슬라이더 상태 업데이트 로직
            nowTime = currentTime
            // 필요하면 현재 슬라이더 값을 서버나 ViewModel에 저장하는 추가 작업도 가능
            println("Slider updated with currentTime: $currentTime and duration: $duration")
        } else {
            println("Waiting for valid currentTime and duration values")
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        AnimatedGradientBackground(colors)
        Scaffold(
            containerColor = Color.Transparent,
            bottomBar = {
                BottomAppBar(
                    modifier = Modifier
                        .height(90.dp)
                        .fillMaxWidth(playBarWidth)
                        .padding(horizontal = 30.dp),
                    containerColor = Color.Transparent
                ) {
                    Column {
                        Canvas(modifier = Modifier
                            .fillMaxSize()
                            .weight(0.5f)) {
                            val sliderWidth = size.width
                            val sliderHeight = 2.dp.toPx()

                            // 트랙
                            drawRect(
                                color = LightGray,
                                topLeft = Offset(0f, center.y - sliderHeight / 2),
                                size = Size(sliderWidth, sliderHeight),
                                style = Stroke(width = sliderHeight)
                            )

                            // 활성화된 트랙 부분
                            drawRect(
                                color = White,
                                topLeft = Offset(0f, center.y - sliderHeight / 2),
                                size = Size(sliderWidth * animatedCurrentTime / duration, sliderHeight)
                            )

                            // 썸을 원형으로 그리기
                            val thumbRadius = 5.dp.toPx()
                            drawCircle(
                                color = White,
                                radius = thumbRadius,
                                center = Offset(sliderWidth * animatedCurrentTime / duration, center.y)
                            )
                        }

                        Slider(
                            value = animatedCurrentTime,
                            valueRange = 0f..duration,
                            onValueChange = { newValue ->
                                viewModel.seekTo(newValue)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(2.dp)
                                .weight(0.1f),
                            colors = SliderDefaults.colors(
                                thumbColor = Color.Transparent, // 썸의 색상
                                activeTrackColor = Color.Transparent, // 활성 상태의 트랙 색상
                                inactiveTrackColor = Color.Transparent, // 비활성 상태의 트랙 색상
                                activeTickColor = Color.Transparent,
                                inactiveTickColor = Color.Transparent
                            )
                        )


                        Row(
                            Modifier
                                .fillMaxSize()
                                .weight(1.2f),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            val context = LocalContext.current // @Composable 함수 내에서 context 가져오기

                            IconButton(onClick = {
                                val vrImageUrl = musicDetail?.vrImageUrl

                                Log.d("VRImageUrl", "vrImageUrl: $vrImageUrl")

                                if (!vrImageUrl.isNullOrEmpty()) { // URL 유효성 검사
                                    try {
                                        // UnityPlayerActivity에 전달할 Intent 생성
                                        val intent = Intent(context, UnityPlayerActivity::class.java).apply {
                                            putExtra("vrImageUrl", vrImageUrl) // VR 이미지 URL 전달
                                        }
                                        context.startActivity(intent) // UnityPlayerActivity 실행
                                    } catch (e: Exception) {
                                        // UnityPlayerActivity 실행 실패 시 로그 출력 및 사용자 알림
                                        Log.e("MusicScreen", "Failed to start UnityPlayerActivity: ${e.message}")
                                        Toast.makeText(context, "VR 화면을 시작할 수 없습니다.", Toast.LENGTH_SHORT).show()
                                    }
                                } else {
                                    Toast.makeText(context, "유효하지 않은 VR 이미지 URL입니다.", Toast.LENGTH_SHORT).show()
                                }
                            }) {
                                Image(
                                    painter = painterResource(id = R.drawable.vr_icon),
                                    contentDescription = "Start VR",
                                    modifier = Modifier.size(32.dp),
                                )
                            }

                            Row(
                                Modifier.width(300.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                IconButton(onClick = {
                                    viewModel.toggleMute()
                                },
                                    modifier = Modifier.size(30.dp)
                                ) {
                                    Image(
                                        painter = if (isMute) painterResource(id = R.drawable.volume_off_icon) else painterResource(id = R.drawable.volume_on_icon),
                                        contentDescription = "Volume toggle",
                                        colorFilter = ColorFilter.tint(MainColor)
                                    )
                                }
                                IconButton(onClick = {
                                    viewModel.playPrevious(context)
                                }) {
                                    Image(
                                        painter = painterResource(id = R.drawable.previous_video_icon),
                                        contentDescription = "Previous Video",
                                        modifier = Modifier.size(55.dp),
                                        colorFilter = ColorFilter.tint(MainColor)
                                    )
                                }

                                if (isPlaying) {
                                    IconButton(onClick = {
                                        viewModel.pause()
                                    }) {
                                        Image(
                                            painter = painterResource(id = R.drawable.pause_icon),
                                            contentDescription = "Pause Video",
                                            modifier = Modifier.size(55.dp),
                                            colorFilter = ColorFilter.tint(MainColor)
                                        )
                                    }
                                } else {
                                    IconButton(onClick = {
                                        viewModel.play()
                                    }) {
                                        Icon(
                                            imageVector = Icons.Filled.PlayArrow,
                                            contentDescription = "Play Video",
                                            tint = MainColor,
                                            modifier = Modifier.size(55.dp)
                                        )
                                    }
                                }


                                IconButton(onClick = {
                                    viewModel.playNext(context)
                                }) {
                                    Image(
                                        painter = painterResource(id = R.drawable.next_video_icon),
                                        contentDescription = "Next Video",
                                        modifier = Modifier.size(55.dp),
                                        colorFilter = ColorFilter.tint(MainColor)
                                    )
                                }

                                IconButton(onClick = {
                                    viewModel.toggleHapticEnabled()
                                },
                                    modifier = Modifier.size(30.dp)
                                ) {
                                    Image(
                                        painter = if (hapticEnabled) painterResource(id = R.drawable.haptic_on_icon) else painterResource(id = R.drawable.haptic_off_icon),
                                        contentDescription = "Haptic toggle",
                                        colorFilter = ColorFilter.tint(MainColor)
                                    )
                                }
                            }
                            IconButton(onClick = {
                                isPlaylistVisible = !isPlaylistVisible // 버튼 클릭 시 너비 변경

                            }) {
                                Icon(
                                    imageVector = Icons.Filled.Menu,
                                    contentDescription = "Open Playlist",
                                    tint = MainColor,
                                    modifier = Modifier.size(55.dp)
                                )
                            }
                        }
                    }

                }

            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxWidth(playBarWidth)
                    .padding(innerPadding),
            ) {
                Column {
                    Row(
                        verticalAlignment = Alignment.Top,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(30.dp)
                    ) {
                        Column(
                            modifier = Modifier.background(Color.Transparent)
                        ) {
                            Text(
//                                text = "$currentTime / $duration",
                                text = "${musicDetail!!.title}",
                                color = White,
                                fontSize = 42.sp,
                                fontFamily = pretendardBold,
                                modifier = Modifier.fillMaxWidth(0.9f)
                            )
                            Text(
                                musicDetail!!.composer,
                                color = SuperLightGray,
                                fontSize = 26.sp,
                                fontFamily = pretendardBold
                            )
                        }
                        IconButton(onClick = {
                            navController.popBackStack() // 전 화면으로 이동
                        }) {
                            Icon(
                                imageVector = Icons.Filled.KeyboardArrowDown,
                                contentDescription = "Close play page",
                                tint = White,
                                modifier = Modifier.size(100.dp)
                            )
                        }
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 30.dp, start = 30.dp, end = 30.dp)
                    ) {
                        val videoWidth by animateDpAsState(
                            targetValue = if (isPlaylistVisible) 750.dp else 600.dp,
                            animationSpec = tween(durationMillis = 300)
                        )
                        val videoHeight by animateDpAsState(
                            targetValue = if (isPlaylistVisible) 500.dp else 400.dp,
                            animationSpec = tween(durationMillis = 300)
                        )

                        AsyncImage(
                            model = musicDetail!!.vrImageUrl,
                            contentDescription = "Playlist Image",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .width(videoWidth)
                                .height(videoHeight)
                        )

                        if (!isPlaylistVisible) {
                            Text(
                                """
                                ${musicDetail!!.description}
                """.trimIndent(),
                                color = Color.White,
                                modifier = Modifier
                                    .height(400.dp)
                                    .width(600.dp)
                                    .verticalScroll(rememberScrollState()),
                                fontFamily = pretendardRegular,
                                fontSize = 22.sp
                            )
                        }
                    }
                }
            }
        }
        AnimatedVisibility(
            visible = isPlaylistVisible,
            enter = slideInHorizontally(animationSpec = tween(300)) { it },
            exit = slideOutHorizontally(animationSpec = tween(300)) { it },
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .fillMaxHeight()
                .fillMaxWidth(0.3f)
                .background(Color.Transparent)

        ) {
            PlaylistComponent(viewModel, backgroundColor = Color(0x1AFFFFFF), playlistViewModel = playlistViewModel )
        }
    }
}

@Composable
fun AnimatedGradientBackground(colors: List<Color>) {
    val infiniteTransition = rememberInfiniteTransition()

    val color1 by infiniteTransition.animateColor(
        initialValue = colors.getOrElse(4) { PlayBar},
        targetValue = colors.getOrElse(0) { DarkGray},
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    val color2 by infiniteTransition.animateColor(
        initialValue = colors.getOrElse(0) { DarkGray },
        targetValue = colors.getOrElse(1) { PlayBar },
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    val color3 by infiniteTransition.animateColor(
        initialValue = colors.getOrElse(1) { PlayBar },
        targetValue = colors.getOrElse(2) { DarkGray },
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        drawRect(
            brush = Brush.linearGradient(
                colors = listOf(color1, color2, color3),
                start = Offset.Zero,
                end = Offset(size.width, size.height),
                tileMode = TileMode.Mirror
            )
        )
    }
}

fun extractDominantColorsFromImageUrl(context: Context, imageUrl: String, onColorsExtracted: (List<Color>) -> Unit) {
    Glide.with(context)
        .asBitmap()
        .load(imageUrl)
        .into(object : CustomTarget<Bitmap>() {
            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                val palette = Palette.from(resource).generate()
                val dominantColors = listOf(
                    palette.getDominantColor(0xFF000000.toInt()),         // Dominant color
                    palette.getDarkVibrantColor(0xFF000000.toInt()),      // Dark Vibrant color
                    palette.getVibrantColor(0xFF888888.toInt()),
                    palette.getDarkMutedColor(0xFFffffff.toInt()),
                    palette.getMutedColor(0xFFffffff.toInt())
                ).map { Color(it) }
                onColorsExtracted(dominantColors)
            }

            override fun onLoadCleared(placeholder: Drawable?) {}
        })
}

fun isRunningOnVRDevice(): Boolean {
    val manufacturer = android.os.Build.MANUFACTURER
    val model = android.os.Build.MODEL
    return manufacturer.equals("Oculus", ignoreCase = true) ||
            model.contains("Quest", ignoreCase = true)
}

