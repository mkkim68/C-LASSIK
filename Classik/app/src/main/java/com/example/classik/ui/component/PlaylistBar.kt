package com.example.classik.ui.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.classik.R
import com.example.classik.ui.theme.DarkGray
import com.example.classik.ui.theme.MainColor
import com.example.classik.ui.theme.PlayBar
import com.example.classik.viewmodel.MusicViewModel
import com.example.classik.viewmodel.PlaylistViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaylistBar(
    navController: NavController,
    content: @Composable () -> Unit,
    topBarContent: @Composable () -> Unit,
    viewModel: MusicViewModel,
    playlistViewModel: PlaylistViewModel
) {
    val context = LocalContext.current
    val isPlaying by viewModel.isPlaying.observeAsState(false)
    var isPlaylistVisible by remember { mutableStateOf(false) }
    val hapticEnabled by viewModel.hapticEnabled.observeAsState(true)
    val isMute by viewModel.isMuted.observeAsState(false)
    val lastPlayedTrack by viewModel.lastPlayedTrack.observeAsState()

    val playBarWidth by animateFloatAsState(
        targetValue = if (isPlaylistVisible) 0.7f else 1f,
        animationSpec = tween(durationMillis = 300)
    )

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            containerColor = DarkGray ,
            topBar = {
                Box(modifier = Modifier.fillMaxWidth(playBarWidth)) {
                    topBarContent()
                }
            },
            bottomBar = {
                BottomAppBar(
                    modifier = Modifier
                        .height(70.dp)
                        .fillMaxWidth(playBarWidth)
                        .padding(0.dp),
                    containerColor = PlayBar
                ) {
                    Column {
                        Row(
                            Modifier.fillMaxSize().weight(1.2f),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                                AsyncImage(
                                    model = lastPlayedTrack?.thumbnailUrl ?: R.drawable.mozart_main,
                                    contentDescription = "Go to Music Screen",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .width(60.dp)
                                        .height(45.dp)
                                        .clickable {
                                            navController.navigate("musicScreen")
                                        }
                                )

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
                                        contentDescription = "Haptic toggle",
//                                        modifier = Modifier.fillMaxSize(),
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
                                    WebViewManager.toggleHapticEnabled()
                                    },
                                    modifier = Modifier.size(30.dp)
                                    ) {
                                    Image(
                                        painter = if (hapticEnabled) painterResource(id = R.drawable.haptic_on_icon) else painterResource(id = R.drawable.haptic_off_icon),
                                        contentDescription = "Haptic toggle",
//                                        modifier = Modifier.fillMaxSize(),
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
                content()
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
            PlaylistComponent(viewModel, playlistViewModel = playlistViewModel)
        }
    }
}