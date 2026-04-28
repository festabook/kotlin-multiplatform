package com.daedan.festabook.presentation.festating.component

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigationevent.NavigationEventInfo
import androidx.navigationevent.compose.NavigationBackHandler
import androidx.navigationevent.compose.rememberNavigationEventState
import com.daedan.festabook.BuildKonfig
import com.multiplatform.webview.web.WebView
import com.multiplatform.webview.web.WebViewState
import com.multiplatform.webview.web.rememberWebViewNavigator
import com.multiplatform.webview.web.rememberWebViewState

@Composable
fun FestatingScreen(
    organizationId: Long,
    festivalId: Long,
    deviceId: Long,
    modifier: Modifier = Modifier,
) {
    val webViewState =
        rememberConfiguredWebViewState(
            url = BuildKonfig.FESTA_TING_URL + "?festivalId=$festivalId&deviceId=$deviceId&organizationId=$organizationId",
        )
    val backState = rememberNavigationEventState(NavigationEventInfo.None)
    val navigator = rememberWebViewNavigator()

    NavigationBackHandler(state = backState, isBackEnabled = navigator.canGoBack) {
        navigator.navigateBack()
    }

    Scaffold(modifier = modifier) { innerPadding ->
        WebView(
            state = webViewState,
            navigator = navigator,
            modifier =
                Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
        )
    }
}

@Composable
private fun rememberConfiguredWebViewState(
    url: String,
    headers: Map<String, String> = emptyMap(),
): WebViewState {
    val state =
        rememberWebViewState(
            url = url,
            additionalHttpHeaders = headers,
        )

    remember(state) {
        state.webSettings.androidWebSettings.domStorageEnabled = true
    }

    return state
}
