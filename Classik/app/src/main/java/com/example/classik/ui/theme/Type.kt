package com.example.classik.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// Set of Material typography styles to start with
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),

    headlineLarge = TextStyle(
        fontFamily = LeagueSpartan,
        fontWeight = FontWeight.Bold,
        fontSize = 72.sp,
        color = MainColor
    ),

    headlineSmall = TextStyle(
        fontFamily = Sanchez,
        fontWeight = FontWeight.Normal,
        fontSize = 24.sp,
        letterSpacing = 0.5.sp,
        color = MainColor
    ),
)