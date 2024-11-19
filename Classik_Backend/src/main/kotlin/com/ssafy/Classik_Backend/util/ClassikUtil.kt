package com.ssafy.Classik_Backend.util

fun splitByComma(str: String): List<String> {
        return str.split(",").map { it.trim() }.filter { it.isNotEmpty() }
}