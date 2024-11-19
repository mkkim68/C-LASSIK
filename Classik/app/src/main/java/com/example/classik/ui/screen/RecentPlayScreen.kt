package com.example.classik.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
fun RecentPlayScreen(navController: NavHostController, viewModel: MyPageViewModel, musicViewModel: MusicViewModel, searchViewModel: SearchViewModel, playlistViewModel: PlaylistViewModel) {
    val context = LocalContext.current
    val history by viewModel.history.observeAsState(emptyList())

    LaunchedEffect(Unit) {
        viewModel.fetchHistory(context)
    }

    PlaylistBar(
        navController,
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFF292929))
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // 최근 재생 기록 제목
                Text(
                    text = "최근 재생 기록",
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    color = Color.White,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                // 반응형 그리드
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(minSize = 264.dp),
                    modifier = Modifier.fillMaxSize(),
//                    horizontalArrangement = Arrangement.Center,
//                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                     items(history) { item ->
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
        topBarContent = { SearchNavbar(navController, searchViewModel, viewModel) },
        musicViewModel,
        playlistViewModel
    )



}



//@Preview(showBackground = true, widthDp = 1280, heightDp = 800)
//@Composable
//fun RecentPlayScreenPreview() {
//    RecentPlayScreen(navController = rememberNavController())
//}

