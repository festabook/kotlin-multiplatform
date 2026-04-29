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

private const val FESTIVAL_ID_QUERY_KEY = "festivalId"
private const val DEVICE_ID_QUERY_KEY = "deviceId"
private const val ORGANIZATION_ID_QUERY_KEY = "organizationId"

@Composable
fun FestatingScreen(
    organizationId: Long,
    festivalId: Long,
    deviceId: Long,
    modifier: Modifier = Modifier,
) {
    val url =
        buildUrl(
            BuildKonfig.FESTA_TING_URL,
            mapOf(
                FESTIVAL_ID_QUERY_KEY to festivalId,
                DEVICE_ID_QUERY_KEY to deviceId,
                ORGANIZATION_ID_QUERY_KEY to organizationId,
            ),
        )
    val webViewState = rememberConfiguredWebViewState(url = url)
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

private fun buildUrl(
    base: String,
    params: Map<String, Any?>,
): String {
    val query =
        params
            .filterValues { it != null }
            .entries
            .joinToString("&") { (k, v) -> "$k=$v" }

    return if (query.isEmpty()) base else "$base?$query"
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
