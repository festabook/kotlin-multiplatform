package com.daedan.festabook.presentation.festating.component.platfom

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.multiplatform.webview.web.WebViewNavigator
import com.multiplatform.webview.web.WebViewState

@Composable
expect fun FestatingWebView(
    webViewState: WebViewState,
    navigator: WebViewNavigator,
    innerPadding: PaddingValues,
    modifier: Modifier = Modifier,
)

const val TOSS_SCHEME = "supertoss"
