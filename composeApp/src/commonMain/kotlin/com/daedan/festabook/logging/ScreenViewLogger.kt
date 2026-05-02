package com.daedan.festabook.logging

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.daedan.festabook.presentation.common.rememberAnalytics

@Composable
fun ScreenViewLogger(screenName: String) {
    val analytics = rememberAnalytics()
    DisposableEffect(Unit) {
        val enteredTime = currentTimeMillis()
        analytics.logEvent(
            "screen_view",
            mapOf(
                "screen_name" to screenName,
                "entered_at" to enteredTime,
            ),
        )
        onDispose {
            val exitedTime = currentTimeMillis()
            analytics.logEvent(
                "screen_exit",
                mapOf(
                    "screen_name" to screenName,
                    "exit_at" to exitedTime,
                    "duration_sec" to (exitedTime - enteredTime) / 1000,
                ),
            )
        }
    }
}

@Composable
fun ScreenViewVisibilityLogger(
    screenName: String,
    isVisible: Boolean,
) {
    val analytics = rememberAnalytics()
    var enteredTime by remember { mutableLongStateOf(0L) }

    LaunchedEffect(isVisible) {
        if (isVisible) {
            enteredTime = currentTimeMillis()
            analytics.logEvent(
                "screen_view",
                mapOf(
                    "screen_name" to screenName,
                    "entered_at" to enteredTime,
                ),
            )
        } else if (enteredTime != 0L) {
            val exitedTime = currentTimeMillis()
            val durationMs = exitedTime - enteredTime

            analytics.logEvent(
                "screen_exit",
                mapOf(
                    "screen_name" to screenName,
                    "exit_at" to exitedTime,
                    // 소수점까지 기록하기 위해 1000.0(Double)으로 나눔
                    "duration_sec" to durationMs / 1000.0,
                ),
            )
            enteredTime = 0L // 시간 초기화
        }
    }
}
