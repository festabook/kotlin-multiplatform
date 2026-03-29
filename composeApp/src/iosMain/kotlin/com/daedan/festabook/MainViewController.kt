package com.daedan.festabook

import androidx.compose.ui.window.ComposeUIViewController
import com.daedan.festabook.presentation.FestabookScreen
import com.daedan.festabook.presentation.theme.FestabookTheme
import platform.posix.exit

@Suppress("ktlint:standard:function-naming")
fun MainViewController() =
    ComposeUIViewController {
        FestabookTheme {
            FestabookScreen(
                onAppFinish = { },
            )
        }
    }
