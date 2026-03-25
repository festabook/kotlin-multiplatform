package com.daedan.festabook

import androidx.compose.ui.window.ComposeUIViewController
import com.daedan.festabook.presentation.theme.FestabookTheme

@Suppress("ktlint:standard:function-naming")
fun MainViewController() =
    ComposeUIViewController {
        FestabookTheme {
//            FestabookScreen(
//                onAppFinish = { exit(0) },
//            )
        }
    }
