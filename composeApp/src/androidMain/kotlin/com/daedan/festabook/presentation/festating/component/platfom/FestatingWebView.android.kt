package com.daedan.festabook.presentation.festating.component.platfom

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.multiplatform.webview.web.PlatformWebViewParams
import com.multiplatform.webview.web.WebView
import com.multiplatform.webview.web.WebViewNavigator
import com.multiplatform.webview.web.WebViewState

@Composable
actual fun FestatingWebView(
    webViewState: WebViewState,
    navigator: WebViewNavigator,
    innerPadding: PaddingValues,
    modifier: Modifier,
) {
    WebView(
        state = webViewState,
        navigator = navigator,
        modifier =
            Modifier
                .padding(innerPadding)
                .fillMaxSize(),
        platformWebViewParams =
            PlatformWebViewParams(
                client = AndroidWebViewClient,
            ),
    )
}
