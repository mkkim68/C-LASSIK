package com.example.classik.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.classik.R
import com.example.classik.data.model.RecommendList
import com.example.classik.ui.component.MusicCard
import com.example.classik.ui.component.PlaylistBar
import com.example.classik.ui.component.SearchNavbar
import com.example.classik.ui.theme.LeagueSpartan
import com.example.classik.viewmodel.MusicViewModel
import com.example.classik.viewmodel.MyPageViewModel
import com.example.classik.viewmodel.PlaylistViewModel
import com.example.classik.viewmodel.SearchViewModel


@Composable
fun RecommendScreen(navController: NavHostController, recommendPlaylist: RecommendList, musicViewModel: MusicViewModel, searchViewModel: SearchViewModel, myPageViewModel: MyPageViewModel, playlistViewModel: PlaylistViewModel) {
    PlaylistBar(
        navController,
        content = {
            Column(
                modifier = Modifier
                    .background(Color(0xFF292929))
                    .fillMaxSize()
                    .padding(horizontal = 32.dp, vertical = 16.dp)
            ) {
                // 상단의 재생목록 이름과 전체 재생 버튼
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = "${recommendPlaylist.recommendTitle} Mix",
                        style = TextStyle(
                            fontFamily = LeagueSpartan,
                            fontSize = 48.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFb38756)
                        ),
                        modifier = Modifier.weight(1f),
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                    Button(
                        onClick = { /* 전체 재생 로직 추가 */ },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White.copy(alpha = 0.1f)),
                        modifier = Modifier
                            .weight(1f)
                            .wrapContentWidth(Alignment.End)
                    ) {
                        Text(text = "전체 재생하기", color = Color.White)
                    }
                }

                // 메인 콘텐츠: 썸네일과 반응형 그리드
                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.spacedBy(30.dp)
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(500.dp)
                    ) {
                        AsyncImage(
                            model = recommendPlaylist.recommendImageUrl,
                            contentDescription = "Playlist Thumbnail",
                            modifier = Modifier
                                .fillMaxSize(0.7f)
                                .align(Alignment.Center)
                        )

                        Image(
                            painter = painterResource(id = R.drawable.classik_frame),
                            contentDescription = "Frame",
                            modifier = Modifier
                                .fillMaxSize()
                        )
                    }

                    LazyVerticalGrid(
                        columns = GridCells.Adaptive(minSize = 264.dp),
                        modifier = Modifier
                            .weight(0.5f)
                            .fillMaxHeight(),
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        items(recommendPlaylist.recommendTracks) { item ->
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                MusicCard(
                                    navController,
                                    item.trackId,
                                    thumbnailResId = item.thumbnailUrl,
                                    title = item.title,
                                    composer = item.composer,
                                    tags = item.tags,
                                    musicViewModel = musicViewModel
                                )
                            }

                        }
                    }
                }
            }
        },
        topBarContent = { SearchNavbar(navController, searchViewModel, myPageViewModel) },
        musicViewModel,
        playlistViewModel
    )
}



// Preview 설정
//@Preview(showBackground = true, widthDp = 1280, heightDp = 800)
//@Composable
//fun RecommendScreenPreview() {
//    val navController = rememberNavController()
//    RecommendScreen(navController = navController, playListName = "Beethoven")
//}
