package com.example.classik.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.classik.ui.theme.MainColor
import com.example.classik.ui.theme.White
import com.example.classik.ui.theme.pretendardBold
import com.example.classik.ui.theme.pretendardRegular

@Composable
fun MyPageSectionTitle(title: String, navController:NavController) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("$title", fontSize = 30.sp, fontFamily = pretendardBold, color = MainColor)
        if (title == "최근 재생 기록") {
            TextButton(
                onClick = {
                    navController.navigate("recentPlayScreen")
                },
                modifier = Modifier
                    .clip(RoundedCornerShape(100.dp))
                    .background(Color(0x1AFFFFFF))
                    .width(70.dp)
                    .height(30.dp),
                contentPadding = PaddingValues(0.dp)
            ) {
                Text("모두 보기", fontSize = 12.sp, fontFamily = pretendardRegular, color = White) // 위아래로 여백 설정)
            }
        }
    }
}

//@Preview()
//@Composable
//fun PreviewMyPageSection() {
//    MyPageSectionTitle("최근 재생 기록")
//}