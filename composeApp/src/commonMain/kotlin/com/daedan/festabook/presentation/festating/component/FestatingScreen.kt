package com.daedan.festabook.presentation.festating.component

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigationevent.NavigationEventInfo
import androidx.navigationevent.compose.NavigationBackHandler
import androidx.navigationevent.compose.rememberNavigationEventState
import com.daedan.festabook.BuildKonfig
import com.daedan.festabook.logging.ScreenViewLogger
import com.daedan.festabook.presentation.common.component.ErrorStateScreen
import com.daedan.festabook.presentation.common.component.LoadingStateScreen
import com.daedan.festabook.presentation.festating.FestatingUiState
import com.daedan.festabook.presentation.festating.FestatingViewModel
import com.multiplatform.webview.web.WebView
import com.multiplatform.webview.web.WebViewState
import com.multiplatform.webview.web.rememberWebViewNavigator
import com.multiplatform.webview.web.rememberWebViewState
import dev.zacsweers.metrox.viewmodel.metroViewModel
import io.github.aakira.napier.Napier
import io.ktor.http.URLBuilder

private const val FESTIVAL_ID_QUERY_KEY = "festivalId"
private const val DEVICE_ID_QUERY_KEY = "deviceId"
private const val ORGANIZATION_ID_QUERY_KEY = "organizationId"

@Composable
fun FestatingScreen(
    modifier: Modifier = Modifier,
    festatingViewModel: FestatingViewModel = metroViewModel(),
) {
    ScreenViewLogger("FestaingScreen")
    val festatingUiState by festatingViewModel.festatingUiState.collectAsStateWithLifecycle()

    when (val uiState = festatingUiState) {
        is FestatingUiState.Error -> {
            Napier.e(uiState.throwable.stackTraceToString())
            ErrorStateScreen(modifier = modifier)
        }

        FestatingUiState.Loading -> {
            LoadingStateScreen(modifier = modifier)
        }

        is FestatingUiState.Success -> {
            val url =
                buildUrl(
                    BuildKonfig.FESTA_TING_URL,
                    mapOf(
                        FESTIVAL_ID_QUERY_KEY to uiState.festating.festivalId,
                        DEVICE_ID_QUERY_KEY to uiState.festating.deviceId,
                        ORGANIZATION_ID_QUERY_KEY to uiState.festating.organizationId,
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
    }
}

private fun buildUrl(
    base: String,
    params: Map<String, Any?>,
): String =
    URLBuilder(base)
        .apply {
            params.forEach { (key, value) ->
                if (value != null) {
                    parameters.append(key, value.toString())
                }
            }
        }.buildString()

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
