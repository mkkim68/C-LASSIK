package com.example.classik.ui.view

import android.net.Uri
import android.view.ViewTreeObserver
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.classik.R
import com.example.classik.data.model.RecommendList
import com.example.classik.ui.component.PlaylistBar
import com.example.classik.ui.component.SearchDropdown
import com.example.classik.ui.theme.Gray
import com.example.classik.ui.theme.LeagueSpartan
import com.example.classik.ui.theme.LightGray
import com.example.classik.ui.theme.White
import com.example.classik.ui.theme.pretendardBold
import com.example.classik.ui.theme.pretendardRegular
import com.example.classik.viewmodel.HomeViewModel
import com.example.classik.viewmodel.MusicViewModel
import com.example.classik.viewmodel.MyPageViewModel
import com.example.classik.viewmodel.PlaylistViewModel
import com.example.classik.viewmodel.SearchViewModel
import com.google.gson.Gson

@Composable
fun HomeScreen(navController: NavController, viewModel: HomeViewModel, searchViewModel: SearchViewModel, myPageViewModel: MyPageViewModel, musicViewModel: MusicViewModel, playlistViewModel: PlaylistViewModel) {
    val context = LocalContext.current
    val textState = remember { mutableStateOf("") }

    val test = viewModel.test.observeAsState()
    val recommendLists by viewModel.recommendlists.observeAsState(emptyList())

    val searchSuccess by searchViewModel.searchSuccess.observeAsState()
    val searchResult by searchViewModel.searchResult.observeAsState(emptyList())

    var isTextFieldFocused by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }

    val view = LocalView.current
    var keyboardHeightDp by remember { mutableStateOf(0.dp) }
    val density = LocalDensity.current

    DisposableEffect(view) {
        val listener = ViewTreeObserver.OnGlobalLayoutListener {
            val rect = android.graphics.Rect()
            view.getWindowVisibleDisplayFrame(rect)
            val screenHeight = view.rootView.height
            val keypadHeight = screenHeight - rect.bottom
            val newHeightDp = with(density) { keypadHeight.toDp() }

            // 키보드가 올라왔을 때만 높이 값을 변경
            if (newHeightDp != keyboardHeightDp) {
                keyboardHeightDp = newHeightDp
            }
        }

        view.viewTreeObserver.addOnGlobalLayoutListener(listener)

        onDispose {
            view.viewTreeObserver.removeOnGlobalLayoutListener(listener)
        }
    }

    LaunchedEffect(test) {
        viewModel.fetchTest()
        viewModel.fetchRecommendLists(context)
    }
    PlaylistBar(
        navController,
        content = {
            if (isTextFieldFocused) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable {
                            focusManager.clearFocus()
                            isTextFieldFocused = false
                        }
                        .zIndex(1f)
                ) {
                    SearchDropdown(
                        navController,
                        modifier = Modifier
                            .offset(y = keyboardHeightDp + 150.dp)
                            .align(Alignment.TopCenter) // 원하는 위치에 맞게 설정
                            .fillMaxWidth(0.9f)
                            .zIndex(100f), // zIndex 설정
                        searchViewModel
                    )
                }
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Spacer(modifier = Modifier.height(40.dp))
                Text(
                    text = "C-LASSIK",
                    fontFamily = LeagueSpartan,
                    color = Color(0xFFb38756),
                    fontSize = 72.sp,
                    fontWeight = FontWeight.Normal
                )
                Spacer(modifier = Modifier.height(40.dp))
                TextField(
                    value = textState.value,
                    onValueChange = { textState.value = it },
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .height(54.dp)
                        .clip(RoundedCornerShape(28.dp))
                        .background(Color.White.copy(alpha = 0.1f))
                        .onFocusChanged { focusState ->
                            isTextFieldFocused = focusState.isFocused
                        },
                    textStyle = TextStyle(color = White, fontSize = 12.sp, fontFamily = pretendardRegular),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent,
                        errorContainerColor = Color.Transparent,

                        focusedIndicatorColor = White,
                        unfocusedIndicatorColor = LightGray,
                        disabledIndicatorColor = Gray,
                        errorIndicatorColor = Color.Red,
                    ),
                    placeholder = { Text(text = "검색어를 입력하세요.", color = Color.Gray) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Search
                    ),
                    keyboardActions = KeyboardActions(
                        onSearch = {
                            // 확인 버튼 눌렀을 때 API 호출 함수 실행
                            searchViewModel.fetchSearch(context, textState.value) {
                                navController.navigate("searchResultScreen/${textState.value}")
                            }
                            focusManager.clearFocus()
                        }
                    )
                )
                Spacer(modifier = Modifier.height(80.dp))
                // Horizontal scrolling row for playlists
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp, start = 16.dp, end=16.dp)
                ) {
                    items(recommendLists) { recommendList  ->
                        PlaylistCard(
                            recommendList,
                            modifier = Modifier
                                .size(200.dp)
                                .clip(RoundedCornerShape(12.dp)),
                            navController
                        )
                    }
                }
            }

        },
        topBarContent = {
            HomeScreenNavbar(navController, myPageViewModel)
        },
        musicViewModel,
        playlistViewModel = playlistViewModel
    )
}

@Composable
fun HomeScreenNavbar(navController: NavController, myPageViewModel: MyPageViewModel) {
    val memberInfo by myPageViewModel.memberInfo.observeAsState()
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
        horizontalArrangement = Arrangement.End,
    ) {

        AsyncImage(
            model = memberInfo?.profileUrl ?: R.drawable.profile_icon,
            contentDescription = "User Image",
            modifier = Modifier
                .size(50.dp)
                .padding(end = 8.dp)
                .clickable(enabled = true) {
                    navController.navigate("myPageScreen")
                }
                .clip(CircleShape)
        )
    }
}

@Composable
fun PlaylistCard(recommendPlaylist: RecommendList, modifier: Modifier = Modifier, navController: NavController) {
    val gson = Gson()
    val itemsJson = gson.toJson(recommendPlaylist)

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(150.dp) // 카드 크기 설정
            .clickable(enabled = true) {
                navController.navigate("recommendScreen/${Uri.encode(itemsJson)}")
            }
    ) {
        // 플레이리스트 이미지 - 뒤쪽에 배치
        AsyncImage(
            model = recommendPlaylist.recommendImageUrl,
            contentDescription = "Playlist Image",
            modifier = Modifier
                .size(0.8f * 150.dp)
                .align(Alignment.Center)
                .clip(RoundedCornerShape(12.dp))
        )

        // 프레임 이미지 (classik_frame) - 앞쪽에 배치
        Image(
            painter = painterResource(id = R.drawable.classik_frame),
            contentDescription = "Frame",
            modifier = Modifier
                .fillMaxSize() // 부모 Box 크기에 맞추기
                .drawWithContent {
                    drawContent() // 이미지 콘텐츠 먼저 그리기
                    drawRect(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color.Black.copy(alpha = 0.5f), // 그림자의 시작 색상
                                Color.Transparent // 그림자가 서서히 사라짐
                            ),
                            startY = size.height, // 시작 위치 조정
                            endY = size.height - 50.dp.toPx() // 끝나는 위치 조정
                        ),
                        size = size.copy(width = 150.dp.toPx()),
                        topLeft = Offset(25.dp.toPx(), size.height - 40.dp.toPx())
                    )
                }
        )

        // Playlist 이름 - 프레임 아래쪽에 위치
        Text(
            text = recommendPlaylist.recommendTitle,
            color = Color.White,
            fontSize = 18.sp,
            fontFamily = pretendardBold,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 8.dp)
                .fillMaxWidth(0.6f)
                .clip(RoundedCornerShape(12.dp))
        )
    }
}

//@Preview(showBackground = true, widthDp = 1280, heightDp = 800)
//@Composable
//fun HomeScreenPreview() {
//    HomeScreen()
//}
