package com.daedan.festabook.presentation.festating.component

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.daedan.festabook.BuildKonfig
import com.multiplatform.webview.web.WebView
import com.multiplatform.webview.web.rememberWebViewNavigator
import com.multiplatform.webview.web.rememberWebViewState

private const val HEADER_FESTIVAL_ID = "festivalId"

@Composable
fun FestatingScreen(
    festivalId: Long,
    modifier: Modifier = Modifier,
) {
    val state =
        rememberWebViewState(
            url = BuildKonfig.FESTA_TING_URL,
            additionalHttpHeaders = mapOf(HEADER_FESTIVAL_ID to festivalId.toString()),
        )
    val navigator = rememberWebViewNavigator()

    Scaffold(modifier = modifier) { innerPadding ->
        WebView(
            state = state,
            navigator = navigator,
            modifier = Modifier.fillMaxSize().padding(innerPadding),
        )
    }
}
