package com.example.classik.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.classik.R
import com.example.classik.ui.theme.White
import com.example.classik.ui.theme.pretendardRegular
import com.example.classik.viewmodel.SearchViewModel

@Composable
fun SearchDropdown(navController: NavController, modifier: Modifier, viewModel: SearchViewModel) {
    // 고정된 검색 기록 예시
    val context = LocalContext.current

    val searchHistory = viewModel.searchHistory.observeAsState()
    val searchSuccess by viewModel.searchSuccess.observeAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchSearchHistory(context)
    }

    Column(
        modifier = modifier
            .fillMaxWidth(0.9f)
            .heightIn(min = 200.dp)
            .padding(horizontal = 16.dp)
            .background(Color.Black.copy(alpha = 0.9f), RoundedCornerShape(8.dp))
            .padding(vertical = 8.dp)
    ) {
        // 검색 기록 리스트를 구글 검색 기록 스타일로 표시
        searchHistory.value?.forEach { searchItem ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        viewModel.fetchSearch(context, searchItem) {
                            navController.navigate("searchResultScreen/${searchItem}")
                        }
                    }
                    .padding(horizontal = 8.dp, vertical = 12.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Spacer(modifier = Modifier.width(10.dp))
                    Image(
                        painter = painterResource(id = R.drawable.history_icon),
                        contentDescription = "Haptic toggle",
                        modifier = Modifier.size(20.dp),
                        colorFilter = ColorFilter.tint(White)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = searchItem,
                        fontSize = 16.sp,
                        color = Color.White
                    )
                }

            }
        } ?: Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxWidth(0.9f)
                .heightIn(min = 200.dp)
            )  {
            Text(
                "아직 검색 기록이 없습니다.",
                fontSize = 16.sp, color = Color.White,
                fontFamily = pretendardRegular,

            )
        }
    }
}

//@Preview(showBackground = true, widthDp = 1280, heightDp = 800)
//@Composable
//fun SearchDropdownPreview() {
//    Column {
//        SearchNavbar() // SearchNavbar와 SearchDropdown을 같이 미리보기에서 확인
//        Spacer(modifier = Modifier.height(8.dp))
//        SearchDropdown()
//    }
//}
