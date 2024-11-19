package com.example.classik.utils

import android.content.Context
import android.os.VibrationEffect
import android.os.Vibrator

class HapticFeedbackController(private val context: Context) {
    private val vibrator: Vibrator? = context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator

    fun triggerHapticFeedback(intensity: Int) {
        val vibrationEffect = VibrationEffect.createOneShot(intensity.toLong(), VibrationEffect.DEFAULT_AMPLITUDE)
        vibrator?.vibrate(vibrationEffect)
    }

    fun stopHapticFeedback() {
        vibrator?.cancel()
    }
}