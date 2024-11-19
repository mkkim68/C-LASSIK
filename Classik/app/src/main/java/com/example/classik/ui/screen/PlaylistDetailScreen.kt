package com.example.classik.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.classik.ui.component.PlaylistBar
import com.example.classik.ui.component.SearchNavbar
import com.example.classik.ui.theme.LeagueSpartan
import com.example.classik.ui.theme.TransParentWhite
import com.example.classik.ui.theme.White
import com.example.classik.ui.theme.pretendardBold
import com.example.classik.ui.theme.pretendardRegular
import com.example.classik.viewmodel.MusicViewModel
import com.example.classik.viewmodel.MyPageViewModel
import com.example.classik.viewmodel.PlaylistViewModel
import com.example.classik.viewmodel.SearchViewModel

@Composable
fun PlaylistDetailScreen(navController: NavController, playlistId: Int, playlistViewModel: PlaylistViewModel, musicViewModel: MusicViewModel, searchViewModel: SearchViewModel, myPageViewModel: MyPageViewModel) {
    val context = LocalContext.current
    val selectedPlaylist by playlistViewModel.selectedPlaylist.observeAsState()
    val fetchPlaylist by playlistViewModel.fetchPlaylist.observeAsState()
    val deletePlaylistSuccess by playlistViewModel.deletePlaylistSuccess.observeAsState()

    PlaylistBar(
        navController,
        content = {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFF292929))
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Left Column: Playlist details
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .padding(0.dp)
                    ) {
                        (0 until 3).forEach { index ->
                            if (index < (selectedPlaylist?.tracks?.size ?: 0)) {
                                AsyncImage(
                                    model = selectedPlaylist!!.tracks[index].thumbnailUrl,
                                    contentDescription = "Thumbnail $index",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .width(363.dp)
                                        .height(242.dp)
                                        .offset(x = (index * 10).dp, y = (-index * 10).dp)
                                        .zIndex((3 - index).toFloat()) // 가장 앞에 있는 이미지가 위로 오도록 설정
                                )

                            } else {
                                Box(
                                    modifier = Modifier
                                        .width(363.dp)
                                        .height(242.dp)
                                        .offset(x = (index * 10).dp, y = (-index * 10).dp)
                                        .background(if (index == 1) Color(0xFFD9D9D9) else Color(0xFFBEBEBE))
                                        .zIndex((3 - index).toFloat())
                                )
                            }
                        }
                    }

                    // Playlist Name
                    Text(
                        text = selectedPlaylist?.playlistTitle ?: "",
                        fontFamily = LeagueSpartan,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier.padding(vertical = 16.dp)
                    )

                    // Buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        TextButton(
                            onClick = {
                                /* TODO: Play All logic */
                            },
                            modifier = Modifier
                                .clip(RoundedCornerShape(100.dp))
                                .background(TransParentWhite)
                                .size(110.dp, 30.dp),
                            contentPadding = PaddingValues(0.dp)
                        ) {
                            Text("전체 재생", fontSize = 12.sp, fontFamily = pretendardRegular, color = White)
                        }
                        TextButton(
                            onClick = {
                                playlistViewModel.deletePlaylistById(playlistId, context)
                                if (deletePlaylistSuccess == true) {
                                    navController.navigate("myPageScreen")
                                }
                            },
                            modifier = Modifier
                                .clip(RoundedCornerShape(100.dp))
                                .background(TransParentWhite)
                                .size(110.dp, 30.dp),
                            contentPadding = PaddingValues(0.dp)
                        ) {
                            Text("플레이리스트 삭제", fontSize = 12.sp, fontFamily = pretendardRegular, color = White)
                        }
                    }
                }

                Spacer(modifier = Modifier.width(16.dp))

                if (selectedPlaylist!!.trackCount.toInt() > 0) {
                    LazyColumn(
                        modifier = Modifier
                            .weight(2f)
                            .fillMaxHeight()
                    ) {
                        items(selectedPlaylist!!.tracks) { item ->
                            TrackRow(
                                trackId = item.trackId,
                                thumbnailUrl = item.thumbnailUrl,
                                composer = item.composer,
                                trackTitle = item.title,
                                tags = item.tags,
                                description = item.description,
                                musicViewModel = musicViewModel,
                                navController = navController
                            )
                        }
                    }
                } else {
                    Column(
                        modifier = Modifier.fillMaxHeight().weight(2f),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text("저장된 곡이 없습니다.", fontFamily = pretendardRegular, fontSize = 20.sp, color = White)
                    }
                }

            }
        },
        topBarContent = {
            SearchNavbar(navController, searchViewModel, myPageViewModel)
        },
        musicViewModel,
        playlistViewModel
    )

}

@Composable
fun TrackRow(
    trackId: Int,
    thumbnailUrl: String,
    trackTitle: String,
    composer: String,
    tags: List<String>,
    description: String,
    musicViewModel: MusicViewModel,
    navController: NavController
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current

    val defaultPlaylist by musicViewModel.defaultPlaylist.observeAsState()
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(170.dp)
            .padding(vertical = 16.dp)
            .clickable(enabled = true) {
                musicViewModel.fetchMusicDetail(trackId) {
                    musicViewModel.musicDetail.observe(lifecycleOwner) { musicDetail ->
                        if (musicDetail != null && musicDetail.trackId == trackId) {
                            musicViewModel.saveLastPlayedTrack(
                                context = context,
                                track = musicDetail,
                                index = 0
                            )

                            // defaultPlaylist에서 선택된 트랙을 맨 위로 이동
                            val updatedPlaylist = listOf(musicDetail) + musicViewModel.defaultPlaylist.value.orEmpty()
                                .filterNot { it.trackId == musicDetail.trackId }
                            musicViewModel.updateDefaultPlaylist(updatedPlaylist)

                            musicViewModel.setNowPlayingList(
                                context,
                                "default",
                                -99,
                                defaultPlaylist!!
                            )

                            // "musicScreen"으로 네비게이션
                            navController.navigate("musicScreen")
                        }
                    }
                }

            },
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(16.dp) // 요소 간 16dp 간격
    ) {
        // Track 이미지
        AsyncImage(
            model = thumbnailUrl,
            contentDescription = "Track Image",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .width(200.dp)
                .height(150.dp)
        )

        // Track 정보 (타이틀, 작곡가, 태그)
        Column(
            modifier = Modifier.width(200.dp).fillMaxHeight(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = trackTitle,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color.White,
                    fontFamily = pretendardBold
                ),
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = composer,
                style = MaterialTheme.typography.bodyMedium.copy(color = Color.Gray)
            )

            // Tags Row
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(tags) {
                    item -> Box(
                    modifier = Modifier
                        .background(Color(0xFFB35956), RoundedCornerShape(12.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = item,
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontSize = 10.sp,
                                color = Color.White,
                                fontFamily = pretendardRegular
                            )
                        )
                    }
                }
            }
        }

        // Description
        Text(
            text = description,
            modifier = Modifier.width(283.dp),
            style = MaterialTheme.typography.bodySmall.copy(color = Color.White)
        )
    }
}

//// Preview 설정
//@Preview(showBackground = true, widthDp = 1280, heightDp = 800)
//@Composable
//fun PlaylistDetailScreenPreview() {
//    val playlistViewModel: PlaylistViewModel = viewModel()
//    val musicViewModel: MusicViewModel = viewModel()
//    val searchViewModel: SearchViewModel = viewModel()
//    val myPageViewModel: MyPageViewModel = viewModel()
//    PlaylistDetailScreen(navController = rememberNavController(), 6, playlistViewModel, musicViewModel, searchViewModel, myPageViewModel)
//}
