package com.example.classik.ui.component

import android.content.Context
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.example.classik.R
import com.example.classik.data.model.LastPlayedTrack
import com.example.classik.data.model.MusicDetail
import com.example.classik.ui.theme.Gray
import com.example.classik.ui.theme.LightGray
import com.example.classik.ui.theme.MainColor
import com.example.classik.ui.theme.PlayBar
import com.example.classik.ui.theme.SuperLightGray
import com.example.classik.ui.theme.White
import com.example.classik.ui.theme.pretendardRegular
import com.example.classik.viewmodel.MusicViewModel
import com.example.classik.viewmodel.PlaylistViewModel

@Composable
fun PlaylistComponent(
    viewModel: MusicViewModel,
    backgroundColor: Color = PlayBar,
    playlistViewModel: PlaylistViewModel
) {
    val context = LocalContext.current
    val defaultPlaylist = viewModel.defaultPlaylist.observeAsState(emptyList())
    val defaultItemCount = viewModel.defaultItemCount.observeAsState(0)
    val nowPlayingList by viewModel.nowPlayingList.observeAsState()
    var nowState by remember {
        mutableStateOf(
            nowPlayingList?.playlistType
        )
    } // default, allPlaylists, playlist

    val lastPlayedTrack by viewModel.lastPlayedTrack.observeAsState()

    val playlists by playlistViewModel.playlists.observeAsState(emptyList())
    var allSelected by remember { mutableStateOf(false) }
    var selectedItems by remember { mutableStateOf(mutableSetOf<Int>()) }
    var selectedByTrackIds by remember { mutableStateOf(mutableSetOf<Int>()) }
    val fetchPlaylistSuccess by playlistViewModel.fetchPlaylist.observeAsState()

    var showCreateModal by remember { mutableStateOf(false) }
    var showAddModal by remember { mutableStateOf(false) }
    var text by remember { mutableStateOf(TextFieldValue("")) }

    var playlistId by remember { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        playlistViewModel.fetchPlaylists(context)
    }

    LaunchedEffect(nowState) {
        allSelected = false
        selectedItems = mutableSetOf()
        selectedByTrackIds = mutableSetOf()
    }

    LaunchedEffect(selectedByTrackIds) {
        Log.d("playlist delete track", "$selectedByTrackIds")
    }

    Box(
        modifier = Modifier
            .fillMaxHeight()
            .width(366.dp)
            .background(backgroundColor)
            .padding(horizontal = 16.dp, vertical = 24.dp)
    ) {
        Column {
            // 상단 재생목록과 플레이리스트 텍스트
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    "재생목록",
                    color = if (nowState == "default") White else LightGray,
                    fontSize = 24.sp,
                    fontFamily = FontFamily(Font(R.font.pretendard_regular, FontWeight.Normal)),
                    modifier = Modifier.clickable { nowState = "default" }
                )
                Spacer(modifier = Modifier.width(20.dp))
                Text(
                    "플레이리스트",
                    color = if (nowState == "default") LightGray else White,
                    fontSize = 24.sp,
                    fontFamily = FontFamily(Font(R.font.pretendard_regular, FontWeight.Normal)),
                    modifier = Modifier.clickable { nowState = "allPlaylists" }
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            if (nowState == "default") {
                // 전체 선택 체크박스 및 곡 수
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RoundCheckbox(
                        checked = allSelected,
                        onCheckedChange = { isChecked ->
                            allSelected = isChecked
                            selectedItems =
                                if (isChecked) {
                                    defaultPlaylist.value.indices.toMutableSet() // 모든 인덱스를 선택된 상태로 설정
                                } else {
                                    mutableSetOf() // 선택된 항목 초기화
                                }
                            selectedByTrackIds = if (isChecked) {
                                defaultPlaylist.value.map { it.trackId }.toMutableSet() // 모든 trackId를 선택된 상태로 설정
                            } else {
                                mutableSetOf() // 선택된 trackId 초기화
                            }
                        }
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "${defaultItemCount.value}곡",
                        color = White,
                        fontSize = 14.sp,
                        fontFamily = FontFamily(Font(R.font.pretendard_regular, FontWeight.Normal)),
                        modifier = Modifier.padding(6.dp)
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))

                // 재생목록 아이템 목록
                LazyColumn {
                    itemsIndexed(defaultPlaylist.value) { index, item ->
                        val isLastPlayedTrack =
                            lastPlayedTrack?.let { it.index == index && it.trackId == item.trackId }
                                ?: false

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp)
                                .clickable {
                                    viewModel.fetchMusicDetail(item.trackId) {
                                        val newLastPlayedTrack = MusicDetail(
                                            trackId = item.trackId,
                                            title = item.title,
                                            composer = item.composer,
                                            description = item.description,
                                            tags = item.tags,
                                            videoId = item.videoId,
                                            imageUrl = item.imageUrl,
                                            vrImageUrl = item.vrImageUrl,
                                            thumbnailUrl = item.thumbnailUrl,
                                            hapticTime = item.hapticTime,
                                            hapticIntensity = item.hapticIntensity
                                        )
                                        viewModel.saveLastPlayedTrack(
                                            context,
                                            newLastPlayedTrack,
                                            index
                                        )
                                        WebViewManager.setVideoId(context, newVideoId = item.videoId)
                                        viewModel.updateMusicDetail(newLastPlayedTrack)

                                        // NowPlayingList에 default 저장
                                        viewModel.setNowPlayingList(
                                            context,
                                            "default",
                                            -99,
                                            defaultPlaylist.value
                                        )
                                    }
                                }
                        ) {
                            RoundCheckbox(
                                checked = selectedItems.contains(index),
                                onCheckedChange = { isChecked ->
                                    selectedItems = if (isChecked) {
                                        selectedItems.toMutableSet().apply { add(index) }
                                    } else {
                                        selectedItems.toMutableSet().apply { remove(index) }
                                    }
                                    allSelected = selectedItems.size == defaultItemCount.value
                                    selectedByTrackIds = if (isChecked) {
                                        selectedByTrackIds.toMutableSet().apply { add(item.trackId) }
                                    } else {
                                        selectedByTrackIds.toMutableSet().apply { remove(item.trackId) }
                                    }
                                }
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            AsyncImage(
                                model = item.thumbnailUrl,
                                contentDescription = "Music Thumbnail",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .width(46.dp)
                                    .height(33.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Column {
                                Text(
                                    item.title,
                                    color = if (isLastPlayedTrack) MainColor else White,
                                    fontSize = 16.sp,
                                    maxLines = 1, // 한 줄로 제한
                                    overflow = TextOverflow.Ellipsis // 말줄임표 표시
                                )
                                Text(item.composer, color = SuperLightGray, fontSize = 12.sp)
                            }
                        }
                    }
                }
            }
            else if (nowState == "allPlaylists") { // 플레이리스트
                Text(
                    text = "+ 새 플레이리스트",
                    color = White,
                    fontSize = 14.sp,
                    fontFamily = FontFamily(Font(R.font.pretendard_regular, FontWeight.Normal)),
                    modifier = Modifier.padding(6.dp).clickable { showCreateModal = true }
                )
                Spacer(modifier = Modifier.height(6.dp))

                // 재생목록 아이템 목록
                LazyColumn {
                    items(playlists) { item ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier
                                .padding(vertical = 6.dp)
                                .fillMaxWidth()
                                .clickable {
                                    // 특정 플레이리스트로 이동
                                    playlistViewModel.fetchPlaylistById(item.playlistId, context) {
                                        playlistId = item.playlistId
                                        nowState = "playlist"
                                    }
                                }
                        ) {
                            Row {
                                if (item.thumbnailUrls.size > 0) {
                                    AsyncImage(
                                        model = item.thumbnailUrls[0],
                                        contentDescription = "Music Thumbnail",
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier
                                            .width(46.dp)
                                            .height(33.dp)
                                    )
                                } else {
                                    Box(modifier = Modifier.size(46.dp, 33.dp).background(Gray))
                                }
                                Spacer(modifier = Modifier.width(6.dp))
                                Column {
                                    Text(item.playlistTitle, color = White, fontSize = 16.sp)
                                    Text("${item.trackCount}곡", color = LightGray, fontSize = 12.sp)
                                }
                            }

                            IconButton(onClick = {
                                // 이 플레이리스트 목록을 현재 재생에 넣고 처음 곡부터 play
                            }) {
                                Icon(
                                    imageVector = Icons.Filled.PlayArrow,
                                    contentDescription = "Play this playlist",
                                    tint = White,
                                    modifier = Modifier.size(20.dp)
                                )
                            }

                        }
                    }
                }
            }
            else if (nowState == "playlist") {
                SinglePlaylist(
                    playlistViewModel,
                    context,
                    playlistId,
                    viewModel,
                    selectedItems,
                    selectedByTrackIds,
                    onSelectedItemsChange = { selectedItems = it },
                    onSelectedByTrackIdsChange = { selectedByTrackIds = it }
                )
            }
        }
        if (nowState == "default") {
            AnimatedVisibility(
                visible = selectedItems.isNotEmpty(),
                enter = slideInVertically(initialOffsetY = { it }),
                exit = slideOutVertically(targetOffsetY = { it }),
                modifier = Modifier.align(Alignment.BottomCenter)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Button(
                        onClick = {
                            viewModel.removeTracksByIndex(context, selectedItems.toList())
                            selectedItems.clear()
                            allSelected = false
                        },
                        modifier = Modifier.wrapContentSize(),
                        colors = ButtonDefaults.buttonColors(Color.Transparent)
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Image(
                                painter = painterResource(id = R.drawable.bin_icon),
                                contentDescription = "Delete tracks",
                                modifier = Modifier.size(22.dp),
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text("재생목록에서 삭제", color = White, fontSize = 10.sp)
                        }
                    }
                    Button(
                        onClick = {
                            showAddModal = true
                        },
                        modifier = Modifier.wrapContentSize(),
                        colors = ButtonDefaults.buttonColors(Color.Transparent)
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Image(
                                painter = painterResource(id = R.drawable.plus_icon),
                                contentDescription = "Add tracks to a playlist",
                                modifier = Modifier.size(22.dp),
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text("내 플레이리스트에 추가", color = White, fontSize = 10.sp)
                        }
                    }
                }
            }
        }
        else if (nowState == "playlist") {
            AnimatedVisibility(
                visible = selectedItems.isNotEmpty(),
                enter = slideInVertically(initialOffsetY = { it }),
                exit = slideOutVertically(targetOffsetY = { it }),
                modifier = Modifier.align(Alignment.BottomCenter)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Button(
                        onClick = {
                            playlistViewModel.deleteTracksInPlaylist(playlistId, selectedItems.toList(), context)
                        },
                        modifier = Modifier.wrapContentSize(),
                        colors = ButtonDefaults.buttonColors(Color.Transparent)
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Image(
                                painter = painterResource(id = R.drawable.bin_icon),
                                contentDescription = "Delete tracks",
                                modifier = Modifier.size(22.dp),
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text("재생목록에서 삭제", color = White, fontSize = 10.sp)
                        }
                    }
                    Button(
                        onClick = {
                            showAddModal = true
                        },
                        modifier = Modifier.wrapContentSize(),
                        colors = ButtonDefaults.buttonColors(Color.Transparent)
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Image(
                                painter = painterResource(id = R.drawable.plus_icon),
                                contentDescription = "Add tracks to a playlist",
                                modifier = Modifier.size(22.dp),
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text("내 플레이리스트에 추가", color = White, fontSize = 10.sp)
                        }
                    }
                }
            }
        }
    }
    if (showCreateModal) {
        Dialog(onDismissRequest = { showCreateModal = false }) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .clickable(enabled = true) {
                        showCreateModal = false
                    },
                contentAlignment = Alignment.Center
            ) {
                Box(
                    Modifier
                        .background(PlayBar, shape = RoundedCornerShape(8.dp))
                        .padding(16.dp)
                        .width(210.dp)
                        .height(120.dp)
                        .clickable(enabled = false) {}, // 모달 안쪽 클릭은 무시
                ) {
                    Column(
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Text("새 플레이리스트 생성", fontSize = 18.sp, color = White, fontFamily = pretendardRegular)
                        BasicTextField(
                            value = text,
                            onValueChange = { text = it },
                            singleLine = true,
                            textStyle = TextStyle(
                                color = Color.White,
                                fontSize = 14.sp,
                                fontFamily = pretendardRegular // 본인의 폰트를 설정하세요.
                            ),
                            cursorBrush = SolidColor(Color.Gray),
                            modifier = Modifier
                                .wrapContentHeight(),
                            decorationBox = { innerTextField ->
                                Column(
                                    Modifier
                                        .fillMaxWidth()
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                    ) {
                                        innerTextField() // 텍스트 필드 내용 표시
                                    }
                                    // Bottom Border 추가
                                    Spacer(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(1.dp)
                                            .background(Color.White)
                                    )
                                }
                            }
                        )
                        Row(
                            horizontalArrangement = Arrangement.End,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("취소", fontSize = 16.sp, fontFamily = pretendardRegular, color = White, modifier = Modifier.clickable(enabled = true) {showCreateModal = false})
                            Spacer(modifier = Modifier.width(16.dp))
                            Text("생성", fontSize = 16.sp, fontFamily = pretendardRegular, color = White,
                                modifier = Modifier.clickable(enabled = true) {
                                    playlistViewModel.createPlaylist(title = text.text, context)
                                    showCreateModal = false
                                })
                        }
                    }
                }
            }

        }
    }
    if (showAddModal){
        Dialog(onDismissRequest = { showAddModal = false }) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .clickable(enabled = true) {
                        showAddModal = false
                    },
                contentAlignment = Alignment.Center
            ) {
                Box(
                    Modifier
                        .background(PlayBar, shape = RoundedCornerShape(8.dp))
                        .padding(16.dp)
                        .width(210.dp)
                        .heightIn(max = 350.dp)
                        .clickable(enabled = false) {}, // 모달 안쪽 클릭은 무시
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Top,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("플레이리스트에 담기", fontSize = 18.sp, color = White, fontFamily = pretendardRegular)
                            Text("Χ", fontSize = 18.sp, color = White, fontFamily = pretendardRegular, modifier = Modifier.clickable(enabled = true) {
                                showAddModal = false
                            })
                        }
                        Spacer(modifier = Modifier.fillMaxWidth().height(14.dp))
                        LazyColumn {
                            itemsIndexed(playlists) { index, item ->
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 10.dp)
                                        .clickable {
                                            playlistViewModel.addTracksToPlaylist(item.playlistId, selectedByTrackIds.toList(), context)
                                            showAddModal = false
                                        }
                                ) {
                                    Text(item.playlistTitle, color = White, fontSize = 13.sp)
                                }
                                Spacer(modifier = Modifier.fillMaxWidth().height(1.dp).background(color = Gray))
                            }
                        }
                    }
                }
            }

        }
    }
}

@Composable
fun SinglePlaylist(
    viewModel: PlaylistViewModel,
    context: Context,
    playlistId: Int,
    musicViewModel: MusicViewModel,
    selectedItems: MutableSet<Int>,
    selectedByTrackIds: MutableSet<Int>,
    onSelectedItemsChange: (MutableSet<Int>) -> Unit,
    onSelectedByTrackIdsChange: (MutableSet<Int>) -> Unit
) {
    var allSelected by remember { mutableStateOf(false) }

    val playlistDetail by viewModel.selectedPlaylist.observeAsState()
    val fetchedMusicDetail by musicViewModel.musicDetail.observeAsState()
    val lastPlayedTrack by musicViewModel.lastPlayedTrack.observeAsState()

    LaunchedEffect(playlistId) {
        viewModel.fetchPlaylistById(playlistId, context) {}
    }
    Text(
        text = playlistDetail!!.playlistTitle,
        color = White,
        fontSize = 16.sp,
        fontFamily = FontFamily(Font(R.font.pretendard_regular, FontWeight.Normal)),
        modifier = Modifier.padding(vertical = 6.dp)
    )
    Row(verticalAlignment = Alignment.CenterVertically) {
        RoundCheckbox(
            checked = allSelected,
            onCheckedChange = { isChecked ->
                allSelected = isChecked
                onSelectedItemsChange(
                    if (isChecked) playlistDetail!!.tracks.map { it.playlistTrackId }.toMutableSet() else mutableSetOf()
                )
                onSelectedByTrackIdsChange(
                    if (isChecked) playlistDetail!!.tracks.map { it.trackId }.toMutableSet() else mutableSetOf()
                )
            }
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = "${playlistDetail!!.trackCount}곡",
            color = White,
            fontSize = 14.sp,
            fontFamily = FontFamily(Font(R.font.pretendard_regular, FontWeight.Normal)),
            modifier = Modifier.padding(6.dp)
        )
    }
    Spacer(modifier = Modifier.height(6.dp))
    LazyColumn {
        itemsIndexed(playlistDetail!!.tracks) { index, item ->
            val isLastPlayedTrack =
                lastPlayedTrack?.let { it.index == item.playlistTrackId}
                    ?: false
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)
                    .clickable {
                        musicViewModel.fetchMusicDetail(item.trackId) {
                            fetchedMusicDetail?.let { fetchedDetail ->
                                val newLastPlayedTrack = LastPlayedTrack(
                                    index = item.playlistTrackId,
                                    trackId = fetchedDetail.trackId,
                                    title = fetchedDetail.title,
                                    composer = fetchedDetail.composer,
                                    description = fetchedDetail.description,
                                    tags = fetchedDetail.tags,
                                    videoId = fetchedDetail.videoId,
                                    imageUrl = fetchedDetail.imageUrl,
                                    vrImageUrl = fetchedDetail.vrImageUrl,
                                    thumbnailUrl = fetchedDetail.thumbnailUrl,
                                    hapticTime = fetchedDetail.hapticTime,
                                    hapticIntensity = fetchedDetail.hapticIntensity
                                )

                                musicViewModel.saveLastPlayedTrackInPlaylist(
                                    context,
                                    newLastPlayedTrack
                                )
                                WebViewManager.setVideoId(context, newVideoId = fetchedDetail.videoId)
                                musicViewModel.setNowPlayingList(
                                    context,
                                    "playlist",
                                    playlistId,
                                    playlistDetail!!.tracks.map {

                                        MusicDetail(
                                            trackId = it.trackId,
                                            title = it.title,
                                            composer = it.composer,
                                            description = "",
                                            tags = it.tags,
                                            videoId = "",
                                            imageUrl = "",
                                            vrImageUrl = "",
                                            thumbnailUrl = it.thumbnailUrl,
                                            hapticTime = emptyList(),
                                            hapticIntensity = emptyList()
                                        )
                                    }
                                )
                                val updatedPlaylist = musicViewModel.nowPlayingList.value!!.playlist.map { musicDetail ->
                                    if (musicDetail.trackId == item.trackId) {
                                        MusicDetail(
                                            trackId = fetchedDetail.trackId,
                                            title = fetchedDetail.title,
                                            composer = fetchedDetail.composer,
                                            description = fetchedDetail.description,
                                            tags = fetchedDetail.tags,
                                            videoId = fetchedDetail.videoId,
                                            imageUrl = fetchedDetail.imageUrl,
                                            vrImageUrl = fetchedDetail.vrImageUrl,
                                            thumbnailUrl = fetchedDetail.thumbnailUrl,
                                            hapticTime = fetchedDetail.hapticTime,
                                            hapticIntensity = fetchedDetail.hapticIntensity
                                        )
                                    } else {
                                        // 기존 MusicDetail 유지
                                        musicDetail
                                    }
                                }
                                musicViewModel.setNowPlayingList(
                                    context = context,
                                    playlistType = "playlist",
                                    playlistId = playlistId,
                                    playlist = updatedPlaylist
                                )
                            }
                        }

                }
            ) {
                RoundCheckbox(
                    checked = selectedItems.contains(item.playlistTrackId),
                    onCheckedChange = { isChecked ->
                        onSelectedItemsChange(
                            if (isChecked) selectedItems.toMutableSet().apply { add(item.playlistTrackId) }
                            else selectedItems.toMutableSet().apply { remove(item.playlistTrackId) }
                        )
                        allSelected = selectedItems.size == playlistDetail!!.tracks.size
                        onSelectedByTrackIdsChange(
                            if (isChecked) selectedByTrackIds.toMutableSet().apply { add(item.trackId) }
                            else selectedByTrackIds.toMutableSet().apply { remove(item.trackId) }
                        )
                    }
                )
                Spacer(modifier = Modifier.width(6.dp))
                AsyncImage(
                    model = item.thumbnailUrl,
                    contentDescription = "Music Thumbnail",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .width(46.dp)
                        .height(33.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Column {
                    Text(
                        item.title,
                        color = if (isLastPlayedTrack) MainColor else White,
                        fontSize = 16.sp,maxLines = 1, // 한 줄로 제한
                        overflow = TextOverflow.Ellipsis // 말줄임표 표시
                    )
                    Text(item.composer, color = LightGray, fontSize = 12.sp)
                }
            }
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun PreviewPlaylist() {
//    PlaylistComponent()
//}

