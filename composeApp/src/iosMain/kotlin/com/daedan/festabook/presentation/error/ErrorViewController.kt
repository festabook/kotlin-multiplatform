package com.daedan.festabook.presentation.error

import androidx.compose.ui.window.ComposeUIViewController
import com.daedan.festabook.presentation.theme.FestabookTheme
import platform.UIKit.UIViewController
import platform.posix.exit

@Suppress("ktlint:standard:function-naming")
fun ErrorViewController(): UIViewController =
    ComposeUIViewController {
        FestabookTheme {
            ErrorScreen(onRestart = { exit(0) })
        }
    }
