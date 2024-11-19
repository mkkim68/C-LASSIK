package com.example.classik.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.classik.viewmodel.MusicViewModel
import com.example.classik.viewmodel.MyPageViewModel

@Composable
fun HistoryComponent(navController: NavController, myPageViewModel: MyPageViewModel, musicViewModel: MusicViewModel) {
    val musics by myPageViewModel.history.observeAsState(emptyList())
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        myPageViewModel.fetchHistory(context)
    }

    Column(
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .fillMaxWidth()
            .fillMaxHeight(0.5f)
    ) {
        MyPageSectionTitle("최근 재생 기록", navController)
        Spacer(modifier = Modifier.height(5.dp))
        LazyRow {
            items(musics.take(10)) { item ->
                    MusicCard(
                        navController,
                        trackId = item.trackId,
                        title = item.title,
                        composer = item.composer,
                        thumbnailResId = item.thumbnailUrl,
                        tags = item.tags,
                        musicViewModel = musicViewModel
                    )

                Spacer(modifier = Modifier.width(20.dp))
            }
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun PreviewHistory() {
//    HistoryComponent()
//}