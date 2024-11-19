package com.example.classik.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.classik.ui.theme.TagColor1
import com.example.classik.viewmodel.MusicViewModel

@Composable
fun MusicCard(
    navController: NavController,
    trackId: Int,
    thumbnailResId: String,
    title: String,
    composer: String,
    tags: List<String>,
    musicViewModel: MusicViewModel
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current

    val defaultPlaylist by musicViewModel.defaultPlaylist.observeAsState()
    Card(
        modifier = Modifier
            .size(width = 264.dp, height = 260.dp)
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
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF292929)) // 카드 배경 색상
    ) {
        Column(
            modifier = Modifier.padding(vertical = 8.dp), // 카드 내부 간격 최소화
            horizontalAlignment = Alignment.Start // 왼쪽 정렬
        ) {

            AsyncImage(
                model = thumbnailResId,
                contentDescription = "Playlist Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .width(264.dp)
                    .height(150.dp)
            )

            Spacer(modifier = Modifier.height(4.dp))

            // 제목
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = 16.sp, // 폰트 크기 줄이기
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                ),
                maxLines = 1
            )

            Spacer(modifier = Modifier.height(2.dp)) // 간격 최소화

            // 작곡가
            Text(
                text = "By $composer",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = 14.sp,
                    color = Color.Gray
                ),
                maxLines = 1
            )

            Spacer(modifier = Modifier.height(8.dp)) // 간격 최소화

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp), // 태그 간 간격
                verticalAlignment = Alignment.CenterVertically
            ) {
                items(tags.size) { index ->
                    Box(
                    modifier = Modifier
                        .background(TagColor1, RoundedCornerShape(16.dp)) // 배경색 및 모서리 둥글게
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                        .height(15.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = tags[index],
                            color = Color.White,
                            fontSize = 9.sp,
                            modifier = Modifier
                                .padding(horizontal = 4.dp),  // 태그 간 간격 추가
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun MusicCardPreview() {
//    val navController = rememberNavController()
//    MusicCard(
//        navController,
//        1,
//        thumbnailResId = "",
//        title = "Symphony No.5",
//        composer = "Beethoven",
//        listOf( "Classical", "Symphony"), modifier = Modifier.align(Alignment.CenterHorizontally)
//    )
//}
