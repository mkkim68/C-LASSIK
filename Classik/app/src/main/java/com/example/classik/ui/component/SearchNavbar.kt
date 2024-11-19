package com.example.classik.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.classik.R
import com.example.classik.ui.theme.LeagueSpartan
import com.example.classik.ui.theme.pretendardRegular
import com.example.classik.viewmodel.MyPageViewModel
import com.example.classik.viewmodel.SearchViewModel

@Composable
fun SearchNavbar(navController: NavController, searchViewModel: SearchViewModel, myPageViewModel: MyPageViewModel) {
    // 내부에서 상태를 관리하기 위해 remember 사용
    var text by remember { mutableStateOf(TextFieldValue("")) }
    val context = LocalContext.current
    val memberInfo by myPageViewModel.memberInfo.observeAsState()

    val searchSuccess by searchViewModel.searchSuccess.observeAsState()
    val searchResult by searchViewModel.searchResult.observeAsState()
    var isTextFieldFocused by remember { mutableStateOf(false) }

    val focusManager = LocalFocusManager.current
    if (isTextFieldFocused) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f))
                .clickable {
                    focusManager.clearFocus()
                    isTextFieldFocused = false
                }
                .zIndex(1f)
        ) {
            SearchDropdown(
                navController,
                modifier = Modifier
                    .align(Alignment.TopCenter) // 원하는 위치에 맞게 설정
                    .padding(top = 60.dp) // TextField 아래에 여유를 두고 배치
                    .fillMaxWidth(0.8f)
                    .zIndex(100f) // zIndex 설정
                    .offset(x=36.dp),
                searchViewModel
            )
        }
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(Color(0xFF292929))
            .clickable { focusManager.clearFocus() },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // 왼쪽 로고 텍스트
        Text(
            text = "C-LASSIK",
            fontFamily = LeagueSpartan,
            color = Color(0xFFb38756),
            fontSize = 28.sp,
            fontWeight = FontWeight.Normal,
            modifier = Modifier.padding(start = 8.dp)
                .clickable(enabled = true) {
                    navController.navigate("homeScreen")
                }
        )

        Spacer(modifier = Modifier.width(24.dp))

        // 검색창
        Box(
            modifier = Modifier
                .background(Color.White.copy(alpha = 0.1f), shape = RoundedCornerShape(28.dp))
                .height(40.dp)
                .fillMaxWidth(0.9f),
            contentAlignment = Alignment.CenterStart
        ) {
            if (text.text.isEmpty()) {
                Text(
                    text = "검색어를 입력하세요.",
                    color = Color.Gray,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 8.dp)
            ) {
                BasicTextField(
                    value = text,
                    onValueChange = { text = it },
                    singleLine = true,
                    textStyle = TextStyle(
                        color = Color.White,
                        fontSize = 14.sp,
                        fontFamily = pretendardRegular
                    ),
                    cursorBrush = SolidColor(Color.Gray),
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 8.dp)
                        .onFocusChanged { focusState ->
                            isTextFieldFocused = focusState.isFocused
                        },
                    decorationBox = { innerTextField: @Composable () -> Unit ->
                        Box(
                            Modifier.padding(horizontal = 8.dp)
                        ) {
                            innerTextField()
                        }
                    },

                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Search
                    ),
                    keyboardActions = KeyboardActions(
                        onSearch = {
                            // 확인 버튼 눌렀을 때 API 호출 함수 실행
                            searchViewModel.fetchSearch(context, text.text) {
                                navController.navigate("searchResultScreen/${text.text}")
                            }
                            focusManager.clearFocus()
                        }
                    )
                )

                Icon(
                    painter = painterResource(id = R.drawable.search_icon),
                    contentDescription = "Search Icon",
                    tint = Color.Gray,
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        Spacer(modifier = Modifier.width(24.dp))

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

//@Preview(showBackground = true, widthDp = 1280, heightDp = 800)
//@Composable
//fun SearchBarPreview() {
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(16.dp)
//    ) {
//        SearchNavbar()
//    }
//}
