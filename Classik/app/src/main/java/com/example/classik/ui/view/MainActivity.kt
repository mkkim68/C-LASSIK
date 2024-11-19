package com.example.classik.ui.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.classik.data.model.RecommendList
import com.example.classik.network.ApiService
import com.example.classik.ui.component.WebViewManager
import com.example.classik.ui.screen.LoginScreen
import com.example.classik.ui.screen.MusicScreen
import com.example.classik.ui.screen.MyPageScreen
import com.example.classik.ui.screen.RecentPlayScreen
import com.example.classik.ui.screen.RecommendScreen
import com.example.classik.ui.screen.SearchResultScreen
import com.example.classik.ui.screen.SignupScreen
import com.example.classik.ui.screen.SplashScreen
import com.example.classik.utils.LocalStorageManager
import com.example.classik.viewmodel.HomeViewModel
import com.example.classik.viewmodel.MusicViewModel
import com.example.classik.viewmodel.MusicViewModelFactory
import com.example.classik.viewmodel.MyPageViewModel
import com.example.classik.viewmodel.PlaylistViewModel
import com.example.classik.viewmodel.SearchViewModel
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val gson = Gson()
            val navController = rememberAnimatedNavController()
            val context = LocalContext.current

            // ViewModel을 통해 lastPlayedTrack을 observe
//            val musicViewModel: MusicViewModel = hiltViewModel()
            val musicViewModel: MusicViewModel = viewModel(factory = MusicViewModelFactory(context))
            val lastPlayedTrack by musicViewModel.lastPlayedTrack.observeAsState()

            val homeViewModel: HomeViewModel = viewModel()
            val playlistViewModel: PlaylistViewModel = viewModel()
            val myPageViewModel: MyPageViewModel = viewModel()
            val searchViewModel: SearchViewModel = viewModel()

            LocalStorageManager.initializeIfNeeded(this)
            ApiService.init(this)

            // WebView 초기화
            WebViewManager.WebViewContainer(
                videoId = lastPlayedTrack?.videoId ?: ""
            ) // 기본 비디오 ID

            // lastPlayedTrack 변경 감지 시 WebView 업데이트
            LaunchedEffect(lastPlayedTrack) {
                lastPlayedTrack?.let { track ->
                    WebViewManager.setVideoId(context, track.videoId)
                }
            }

            // 네비게이션 설정
            AnimatedNavHost(
                navController = navController,
//                startDestination = "homeScreen"
                startDestination = "splashScreen"
            ) {
                composable("splashScreen") { SplashScreen(navController) }
                composable("homeScreen") { HomeScreen(navController, homeViewModel, searchViewModel, myPageViewModel, musicViewModel, playlistViewModel) }
                composable(
                    "musicScreen",
                    enterTransition = { slideInVertically(initialOffsetY = { it }) + fadeIn() },
                    exitTransition = { slideOutVertically(targetOffsetY = { it }) + fadeOut() }
                ) {
                    MusicScreen(navController, musicViewModel, playlistViewModel)
                }
                composable("myPageScreen") { MyPageScreen(navController, playlistViewModel, musicViewModel, myPageViewModel, searchViewModel) }
                composable("recentPlayScreen") { RecentPlayScreen(navController, myPageViewModel, musicViewModel, searchViewModel, playlistViewModel) }
                composable(
                    "loginScreen",
                    enterTransition = { slideInHorizontally(initialOffsetX = { -it }) },
                    exitTransition = { slideOutHorizontally(targetOffsetX = { -it }) }) {
                    LoginScreen(navController, myPageViewModel = myPageViewModel)
                }
                composable(
                    "signUpScreen",
                    enterTransition = { slideInHorizontally(initialOffsetX = { it }) },
                    exitTransition = { slideOutHorizontally(targetOffsetX = { it }) }) {
                    SignupScreen(navController)
                }

                composable("recentPlayScreen") { RecentPlayScreen(navController, myPageViewModel, musicViewModel, searchViewModel, playlistViewModel) }

                composable("searchResultScreen/{searchQuery}") { backStackEntry ->
                    val searchQuery = backStackEntry.arguments?.getString("searchQuery") ?: ""

                    SearchResultScreen(navController, searchQuery, searchViewModel, musicViewModel, myPageViewModel, playlistViewModel)
                }

                composable("recommendScreen/{recommendsJson}") { backStackEntry ->
                    val recommendsJson = backStackEntry.arguments?.getString("recommendsJson")

                    val recommendPlaylist: RecommendList = gson.fromJson(
                        recommendsJson,
                        object : TypeToken<RecommendList>() {}.type
                    )

                    RecommendScreen(navController = navController, recommendPlaylist, musicViewModel, searchViewModel, myPageViewModel, playlistViewModel)
                }

                composable("playlistDetailScreen/{playlistId}") { backStackEntry ->
                    val playlistId = backStackEntry.arguments?.getString("playlistId")?.toInt() ?: 0

                    PlaylistDetailScreen(navController = navController, playlistId = playlistId, playlistViewModel, musicViewModel, searchViewModel, myPageViewModel)
                }
            }
        }
    }
}