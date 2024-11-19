package com.example.classik.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.classik.ui.component.MusicCard
import com.example.classik.ui.component.PlaylistBar
import com.example.classik.ui.component.SearchNavbar
import com.example.classik.viewmodel.MusicViewModel
import com.example.classik.viewmodel.MyPageViewModel
import com.example.classik.viewmodel.PlaylistViewModel
import com.example.classik.viewmodel.SearchViewModel

@Composable
fun SearchResultScreen(navController: NavHostController, searchQuery: String, searchViewModel: SearchViewModel, musicViewModel: MusicViewModel, myPageViewModel: MyPageViewModel, playlistViewModel: PlaylistViewModel) {
    val searchResult by searchViewModel.searchResult.observeAsState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        searchViewModel.fetchSearch(context, searchQuery) {
//            Log.d("search", "$searchResult")
        }
    }
    PlaylistBar(
        navController,
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFF292929)) // 배경색 설정
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // 검색 결과 제목
                Text(
                    text = """"$searchQuery"에 대한 검색 결과""",
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontSize = 28.sp, // 폰트 크기 조정
                        fontWeight = FontWeight.Bold
                    ),
                    color = Color.White, // 텍스트 색상 흰색으로 설정
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                // 반응형 그리드: 최소 너비가 264dp인 카드로 가능한 많은 열을 자동으로 조절
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(minSize = 264.dp),
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(searchResult!!) { item ->
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
        },
        topBarContent = { SearchNavbar(navController, searchViewModel, myPageViewModel) },
        musicViewModel,
        playlistViewModel
    )
}

// Preview에서 샘플 검색어로 미리보기 확인
//@Preview(showBackground = true, widthDp = 1280, heightDp = 800)
//@Composable
//fun SearchResultScreenPreview() {
//    SearchResultScreen(navController = rememberNavController(), searchQuery = "모차르트")
//}