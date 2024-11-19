package com.example.classik.ui.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.classik.R
import com.example.classik.ui.theme.Gray
import com.example.classik.ui.theme.LeagueSpartan
import com.example.classik.ui.theme.LightGray
import com.example.classik.ui.theme.Sanchez
import com.example.classik.ui.theme.White
import com.example.classik.ui.theme.pretendardRegular
import com.example.classik.viewmodel.MyPageViewModel

@Composable
fun LoginScreen(navController: NavHostController, showForm: Boolean = true, myPageViewModel: MyPageViewModel) {
    BoxWithConstraints {
        val widthFactor = maxWidth / 1280.dp
        val heightFactor = maxHeight / 800.dp

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF292929))
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .padding(bottom = 24.dp * heightFactor)
                    .offset(y = 32.dp * heightFactor)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "C-LASSIK",
                        fontFamily = LeagueSpartan,
                        color = Color(0xFFb38756),
                        fontSize = (84.sp * widthFactor).value.sp,
                        fontWeight = FontWeight.Normal
                    )
                    Text(
                        text = "CLASSICAL AND LASIK",
                        fontSize = (16.sp * widthFactor).value.sp,
                        fontFamily = Sanchez,
                        color = Color(0xFFb38756),
                        modifier = Modifier.padding(top = 8.dp * heightFactor)
                    )
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 120.dp * widthFactor)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.mozart_main),
                    contentDescription = "Mozart Image",
                    modifier = Modifier
                        .width(438.dp * widthFactor)
                        .height(504.dp * heightFactor)
                )

                AnimatedVisibility(
                    visible = showForm,
                    enter = fadeIn()
                ) {
                    LoginForm(widthFactor, heightFactor, navController, myPageViewModel)
                }
            }
        }
    }
}

@Composable
fun LoginForm(widthFactor: Float, heightFactor: Float, navController: NavHostController, userViewModel:MyPageViewModel) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var initialClick by remember { mutableStateOf(false) }

    val loginSuccess by userViewModel.loginSuccess.observeAsState()
    val errorMessage by userViewModel.loginErrorMessage.observeAsState()

    var emailError by remember { mutableStateOf(false) }
    var passwordError by remember { mutableStateOf(false) }

    val context = LocalContext.current

    Column(
        modifier = Modifier
            .width(359.dp * widthFactor)
            .fillMaxHeight(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "로그인",
            style = MaterialTheme.typography.headlineLarge.copy(
                fontSize = (32.sp * widthFactor).value.sp,
                color = Color.White, fontFamily = pretendardRegular
            )
        )
        Spacer(modifier = Modifier.height(8.dp * heightFactor))

        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp * heightFactor),
            thickness = 2.dp,
            color = Color.White
        )

        Spacer(modifier = Modifier.height(36.dp * heightFactor))

        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
                emailError = false
                userViewModel.setLoginSuccess(false)
                initialClick = false
            },
            label = { Text("Email", color = Color.White) },
            placeholder = { Text("이메일을 입력하세요.", color = Color.White) },
            modifier = Modifier.fillMaxWidth(),
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
            isError = emailError
        )

        Spacer(modifier = Modifier.height(16.dp * heightFactor))

        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
                passwordError = false
                userViewModel.setLoginSuccess(false)
                initialClick = false
            },
            label = { Text("Password", color = Color.White) },
            placeholder = { Text("비밀번호를 입력하세요.", color = Color.White) },
            modifier = Modifier.fillMaxWidth(),
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
            isError = passwordError,
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(modifier = Modifier.height(16.dp * heightFactor))

        if (loginSuccess == true) {
            navController.navigate("homeScreen")
        } else if (errorMessage != null){
            Text("로그인 실패: $errorMessage", color = Color.Red, fontFamily = pretendardRegular)
        }

        if (emailError && initialClick) {
            Text("이메일을 입력하세요.", color = Color.Red, fontFamily = pretendardRegular)
        }

        if (passwordError && initialClick) {
            Text("비밀 번호를 입력하세요.", color = Color.Red, fontFamily = pretendardRegular)
        }


        Spacer(modifier = Modifier.height(16.dp * heightFactor))

        Button(
            onClick = {
//                navController.navigate("homeScreen")
                initialClick = true
                if (email == "") {
                    emailError = true
                } else if (password == "") {
                    passwordError = true
                } else {
                    userViewModel.login(email, password, context = context)
                }
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.White.copy(alpha = 0.2f))
        ) {
            Text("로그인", fontSize = (16.sp * widthFactor).value.sp, color = Color.White)
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            "아직 계정이 없어요 →",
            fontFamily = pretendardRegular,
            color = Color(0xFFbebebe),
            textDecoration = TextDecoration.Underline,
            modifier = Modifier.clickable {
                navController.navigate("signUpScreen")
            }
        )
    }
}

//@Preview(showBackground = true, widthDp = 1280, heightDp = 800)
//@Composable
//fun LoginScreenPreview() {
//    val navController = rememberNavController()
//    LoginScreen(navController = navController, showForm = true)
//}
