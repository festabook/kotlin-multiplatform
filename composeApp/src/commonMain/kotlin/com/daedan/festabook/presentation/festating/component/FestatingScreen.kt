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

@Composable
fun FestatingScreen(modifier: Modifier = Modifier) {
    val state = rememberWebViewState(BuildKonfig.FESTA_TING_URL)
    val navigator = rememberWebViewNavigator()

    Scaffold(modifier = modifier) { innerPadding ->
        WebView(
            state = state,
            navigator = navigator,
            modifier = Modifier.fillMaxSize().padding(innerPadding),
        )
    }
}
