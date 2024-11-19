package com.example.classik.ui.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.classik.R
import com.example.classik.ui.theme.LeagueSpartan
import com.example.classik.ui.theme.Sanchez
import com.example.classik.utils.LocalStorageManager
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavController) {
    val context = LocalContext.current
    var showLeftCard by remember { mutableStateOf(false) }
    var showRightCard by remember { mutableStateOf(false) }
    var showLogo by remember { mutableStateOf(false) }

    val accessToken = LocalStorageManager.getAccessToken(context)

    LaunchedEffect(Unit) {
        showLeftCard = true
        delay(1000) // 0.5초 뒤에 오른쪽 카드 페이드 인
        showRightCard = true
        delay(1000) // 0.5초 뒤에 로고 페이드 인
        showLogo = true
        delay(2000) // 스플래시 화면 3초 후 전환
//        navController.navigate("loginScreen")
////        if (accessToken == null || accessToken == "") {
            navController.navigate("loginScreen") {
                popUpTo("splashScreen") { inclusive = true }
                launchSingleTop = true
            }
//        } else {
//            navController.navigate("homeScreen") {
//                popUpTo("splashScreen") { inclusive = true }
//                launchSingleTop = true
//            }
//        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF292929)), // 배경색 설정
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            // Left Image (Mozart) - Slide In from Top + Fade In
            AnimatedVisibility(
                visible = showLeftCard,
                enter = slideInVertically(initialOffsetY = { -1000 }) + fadeIn(tween(1200)) // 위에서 슬라이드 인
            ) {
                Spacer(modifier = Modifier.width(54.dp))

                Box(
                    modifier = Modifier.offset(y = (-50).dp) // 상단으로 50dp 이동
                ) {
                    LeftCard()
                }
            }

            // 로고 텍스트 - Fade In
            AnimatedVisibility(
                visible = showLogo,
                enter = fadeIn(animationSpec = tween(800))
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "C-LASSIK",
                        fontFamily = LeagueSpartan,
                        fontSize = 80.sp,
                        color = Color(0xFFB38756),
                    )
                    Text(
                        text = "CLASSICAL AND LASIK",
                        fontFamily = Sanchez,
                        fontSize = 18.sp,
                        color = Color(0xFFB38756),
                        modifier = Modifier.padding(vertical = 5.dp)
                    )
                }

            }


            // Right Image (Tchaikovsky) - Slide In from Bottom + Fade In
            AnimatedVisibility(
                visible = showRightCard,
                enter = slideInVertically(initialOffsetY = { 1000 }) + fadeIn(tween(1200)) // 아래에서 슬라이드 인
            ) {
                Box(
                    modifier = Modifier.offset(y = 50.dp) // 하단으로 50dp 이동
                ) {
                    RightCard()

                    Spacer(modifier = Modifier.width(54.dp))
                }
            }
        }
    }
}

@Composable
fun LeftCard() {
    Image(
        painter = painterResource(id = R.drawable.mozart_main),
        contentDescription = "Mozart Image",
        modifier = Modifier
            .size(420.dp, 500.dp) // 이미지 크기 설정
    )
}

@Composable
fun RightCard() {
    Image(
        painter = painterResource(id = R.drawable.tcha_main),
        contentDescription = "Tchaikovsky Image",
        modifier = Modifier
            .size(420.dp, 500.dp) // 이미지 크기 설정
    )
}
