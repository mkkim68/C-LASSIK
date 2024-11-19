package com.example.classik.ui.component

import android.net.Uri
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.classik.R
import com.example.classik.ui.theme.DarkGray
import com.example.classik.ui.theme.Gray
import com.example.classik.ui.theme.LightGray
import com.example.classik.ui.theme.SuperSuperLightGray
import com.example.classik.ui.theme.White
import com.example.classik.ui.theme.pretendardRegular
import com.example.classik.viewmodel.MyPageViewModel

@Composable
fun UserInfoComponent(navController: NavController, viewModel: MyPageViewModel) {
    var isEdit by remember { mutableStateOf(false) }

    val memberInfo by viewModel.memberInfo.observeAsState()

    var password1 by remember { mutableStateOf("") }
    var password2 by remember { mutableStateOf("") }
    var nickname by remember { mutableStateOf(memberInfo?.nickname ?: "") }

    val context = LocalContext.current as ComponentActivity
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        imageUri = uri
    }

    val deleteSuccess by viewModel.deleteSuccess.observeAsState()
    val logoutSuccess by viewModel.logoutSuccess.observeAsState()
    var showError by remember { mutableStateOf(false) }
    val errorMessage = "비밀번호가 일치하지 않습니다."

    LaunchedEffect(memberInfo) {
        viewModel.fetchMemberInfo(context, memberIdRequest = memberInfo?.memberId ?: 0)
    }

    Column(
        modifier = Modifier
            .fillMaxHeight()
            .width(320.dp)
            .padding(horizontal = 36.dp)
            .background(DarkGray),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        if (!isEdit) {
            AsyncImage(
                model = memberInfo?.profileUrl ?: R.drawable.profile_icon,
                contentDescription = "User Image",
                modifier = Modifier
                    .width(180.dp)
                    .height(180.dp)
                    .clip(CircleShape)
                    .background(Color.Transparent)
            )
            Spacer(modifier = Modifier.height(32.dp))
            Text(memberInfo?.nickname ?: "", fontSize = 34.sp, fontFamily = pretendardRegular, color = White)
            Spacer(modifier = Modifier.height(10.dp))
            Text(memberInfo?.email ?: "", fontSize = 16.sp, fontFamily = pretendardRegular, color = SuperSuperLightGray)
            Spacer(modifier = Modifier.height(32.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                TextButton(
                    onClick = {
                        isEdit = true
                    },
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0x1AFFFFFF))
                        .width(115.dp)
                        .height(40.dp)
                ) {
                    Text("정보 수정", fontSize = 16.sp, fontFamily = pretendardRegular, color = White)
                }
                TextButton(
                    onClick = {
                        Log.d("Logout", "누름")
                        viewModel.fetchLogout(context)

                        if (logoutSuccess == true) {
                            navController.navigate("loginScreen")
                        }
                    },
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0x1AFFFFFF))
                        .width(115.dp)
                        .height(40.dp)
                ) {
                    Text("로그아웃", fontSize = 16.sp, fontFamily = pretendardRegular, color = White)
                }
            }


        }
        else {
            AsyncImage(
                model = imageUri ?: viewModel.memberInfo.value?.profileUrl ?: R.drawable.profile_icon,
                contentDescription = "User Image",
                modifier = Modifier
                .width(150.dp)
                .height(150.dp)
                .clip(CircleShape)
                .background(Color.Transparent)
                .clickable(enabled = true) {
                    launcher.launch("image/*")
                }
            )

            Spacer(modifier = Modifier.height(if (showError) 16.dp else 32.dp))
            if (showError) {
                Text(
                    text = errorMessage,
                    color = Color.Red,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
            }
            // 비밀번호 입력란
            OutlinedTextField(
                value = password1,
                onValueChange = {
                    password1 = it
                    showError = false
                    },
                label = { Text("Password", color = Color.White, fontSize = 14.sp, fontFamily = pretendardRegular) }, // Label 텍스트 흰색
                placeholder = { Text("비밀번호를 입력하세요.", color = Color.White, fontSize = 12.sp, fontFamily = pretendardRegular) }, // Placeholder 텍스트 흰색
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .wrapContentSize()
                    .padding(0.dp),
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
                isError = showError,
                visualTransformation = PasswordVisualTransformation()
                )
            Spacer(modifier = Modifier.height(23.dp))
            // 비밀번호 재입력란
            OutlinedTextField(
                value = password2,
                onValueChange = {
                    password2 = it
                    showError = false
                    },
                label = { Text("Password Confirm", color = White, fontSize = 14.sp, fontFamily = pretendardRegular) }, // Label 텍스트 흰색
                placeholder = { Text("비밀번호를 입력하세요.", color = White, fontSize = 12.sp, fontFamily = pretendardRegular) }, // Placeholder 텍스트 흰색
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .wrapContentSize()
                    .padding(0.dp),
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
                isError = showError,
                visualTransformation = PasswordVisualTransformation()
            )
            Spacer(modifier = Modifier.height(23.dp))

            // 닉네임 입력란
            OutlinedTextField(
                value = nickname,
                onValueChange = {nickname= it},
                label = { Text("Nickname", color = Color.White, fontSize = 14.sp, fontFamily = pretendardRegular) }, // Label 텍스트 흰색
                placeholder = { Text("닉네임을 입력하세요.", color = Color.White, fontSize = 12.sp, fontFamily = pretendardRegular) }, // Placeholder 텍스트 흰색
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .wrapContentSize()
                    .padding(0.dp),
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
                )
            )
            Spacer(modifier = Modifier.height(23.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                TextButton(
                    onClick = {
                        // 수정 완료
                        if (password1 == password2) {
                            viewModel.fetchPatchMember(
                                context,
                                memberId = memberInfo!!.memberId,
                                password = password1,
                                nickname = nickname,
                                profileImageUri = imageUri
                            )

                            isEdit = false
                        } else {
                            showError = true
                        }
                    },
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0x1AFFFFFF))
                        .width(115.dp)
                        .height(40.dp)
                ) {
                    Text("수정 완료", fontSize = 16.sp, fontFamily = pretendardRegular, color = White)
                }
                TextButton(
                    onClick = {
                        viewModel.fetchDeleteMember(context, memberInfo!!.memberId)

                        if (deleteSuccess == true) {
                            navController.navigate("loginScreen")
                        }
                    },
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0x1AFF0000))
                        .width(115.dp)
                        .height(40.dp)
                ) {
                    Text("회원 탈퇴", fontSize = 16.sp, fontFamily = pretendardRegular, color = White)
                }
            }
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun PreviewUserInfo() {
//    UserInfoComponent()
//}