// HapticFeedbackUtil.kt
package com.example.classik.utils

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

object HapticFeedbackUtil {
    private var hapticJob: Job? = null

    fun triggerHapticFeedback(context: Context, intensity: Int) {
        val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val vibrationEffect = VibrationEffect.createOneShot(100, if (intensity + 30 <= 255) intensity+30 else 255 ) // 100ms 동안 진동 발생
            vibrator.vibrate(vibrationEffect)
        } else {
            vibrator.vibrate(100) // 레거시 방식
        }
    }


    fun startHapticFeedback(hapticTimes: List<String>, hapticIntensities: List<String>, context: Context) {
        hapticJob?.cancel()
        hapticJob = CoroutineScope(Dispatchers.Main).launch {
            val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            hapticTimes.map { it.toLongOrNull() }.zip(hapticIntensities.map { it.toIntOrNull() }).forEach { (time, intensity) ->
                if (time != null && intensity != null) {
                    delay(time)
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        vibrator.vibrate(VibrationEffect.createOneShot(100, intensity))
                    } else {
                        vibrator.vibrate(100)
                    }
                }
            }
        }
    }

    fun stopHapticFeedback(context: Context) {
        hapticJob?.cancel() // 햅틱 피드백 중지
        val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        vibrator.cancel() // 현재 진동 취소
    }
}
