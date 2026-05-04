package com.daedan.festabook.presentation.festating.component.platfom

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.daedan.festabook.delegate.DefaultWKNavigationDelegate
import com.multiplatform.webview.web.WebView
import com.multiplatform.webview.web.WebViewNavigator
import com.multiplatform.webview.web.WebViewState
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.readValue
import platform.CoreGraphics.CGRectZero
import platform.WebKit.WKNavigationDelegateProtocol
import platform.WebKit.WKWebView

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun FestatingWebView(
    webViewState: WebViewState,
    navigator: WebViewNavigator,
    innerPadding: PaddingValues,
    modifier: Modifier,
) {
    val wrappedDelegateWkNavigationDelegate =
        remember {
            DefaultWKNavigationDelegate()
        }
    WebView(
        state = webViewState,
        navigator = navigator,
        modifier =
            Modifier
                .padding(innerPadding)
                .fillMaxSize(),
        factory = { param ->
            object : WKWebView(frame = CGRectZero.readValue(), configuration = param.config) {
                override fun setNavigationDelegate(navigationDelegate: WKNavigationDelegateProtocol?) {
                    super.setNavigationDelegate(wrappedDelegateWkNavigationDelegate)
                }
            }
        },
    )
}
