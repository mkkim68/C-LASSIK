package com.example.classik.ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.classik.ui.theme.White
import com.example.classik.ui.theme.pretendardRegular
import com.example.classik.viewmodel.PlaylistViewModel

@Composable
fun MyPlaylistComponent(navController: NavController, playlistViewModel: PlaylistViewModel) {
    val context = LocalContext.current
    val allPlaylistItems by playlistViewModel.playlists.observeAsState(emptyList())
    val itemCount by playlistViewModel.itemCount.observeAsState()

    LaunchedEffect(Unit) {
        playlistViewModel.fetchPlaylists(context)
    }

    Column(
        modifier = Modifier
            .padding(horizontal = 20.dp, vertical = 5.dp)
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        MyPageSectionTitle("내 플레이리스트", navController)
        Spacer(modifier = Modifier.height(20.dp))
        allPlaylistItems?.let {
            LazyRow {
                items(allPlaylistItems) { item ->
                    PlaylistCard(
                        playlistId = item.playlistId,
                        title = item.playlistTitle,
                        trackCount = item.trackCount,
                        thumbnails = item.thumbnailUrls,
                        navController,
                        playlistViewModel
                    )
                    Spacer(modifier = Modifier.width(15.dp))
                }
            }
        }

        if (itemCount == 0) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("플레이리스트가 없습니다.", color = White, fontSize = 16.sp, fontFamily = pretendardRegular)
            }
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun PreviewMyPlaylist() {
//    MyPlaylistComponent()
//}