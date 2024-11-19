package com.example.classik.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.classik.ui.component.HistoryComponent
import com.example.classik.ui.component.MyPlaylistComponent
import com.example.classik.ui.component.PlaylistBar
import com.example.classik.ui.component.SearchNavbar
import com.example.classik.ui.component.UserInfoComponent
import com.example.classik.viewmodel.MusicViewModel
import com.example.classik.viewmodel.MyPageViewModel
import com.example.classik.viewmodel.PlaylistViewModel
import com.example.classik.viewmodel.SearchViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyPageScreen(navController: NavController, playlistViewModel: PlaylistViewModel, musicViewModel: MusicViewModel, myPageViewModel: MyPageViewModel, searchViewModel: SearchViewModel) {
    PlaylistBar(
        navController,
        content = {
            Row(modifier = Modifier.fillMaxWidth()) {
                UserInfoComponent(navController, myPageViewModel)
                Column(
                    modifier = Modifier.weight(1f).fillMaxHeight()
                ) {
                    HistoryComponent(navController, myPageViewModel, musicViewModel)
                    MyPlaylistComponent(navController, playlistViewModel)
                }
            }
        },
        topBarContent = { SearchNavbar(navController, searchViewModel, myPageViewModel) },
        musicViewModel,
        playlistViewModel
    )
}
