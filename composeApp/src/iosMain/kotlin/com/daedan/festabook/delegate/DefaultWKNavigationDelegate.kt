package com.daedan.festabook.delegate

import com.daedan.festabook.presentation.festating.component.platfom.TOSS_SCHEME
import io.github.aakira.napier.Napier
import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSURL
import platform.UIKit.UIApplication
import platform.WebKit.WKNavigationAction
import platform.WebKit.WKNavigationActionPolicy
import platform.WebKit.WKNavigationDelegateProtocol
import platform.WebKit.WKWebView
import platform.darwin.NSObject

@OptIn(BetaInteropApi::class, ExperimentalForeignApi::class)
class DefaultWKNavigationDelegate :
    NSObject(),
    WKNavigationDelegateProtocol {
    override fun webView(
        webView: WKWebView,
        decidePolicyForNavigationAction: WKNavigationAction,
        decisionHandler: (WKNavigationActionPolicy) -> Unit,
    ) {
        val url = decidePolicyForNavigationAction.request.URL
        val scheme = url?.scheme

        Napier.d("scheme:$scheme")
        scheme.takeIf { it == TOSS_SCHEME }?.let {
            url?.let { nsUrl ->
                Napier.d("nsUrl:$nsUrl")
                openUrl(nsUrl) {
                    // Kotlin Native에서 objc의 하위 타입은 필드 사용 불가
                    (
                        NSURL.URLWithString("itms-apps://itunes.apple.com/app/id839333328")?.let {
                            openUrl(it)
                        }
                    )
                }
            }
            decisionHandler(WKNavigationActionPolicy.WKNavigationActionPolicyCancel)
            return
        }
        decisionHandler(WKNavigationActionPolicy.WKNavigationActionPolicyAllow)
    }

    private fun openUrl(
        url: NSURL,
        onFailure: () -> Unit = {},
    ) {
        UIApplication.sharedApplication.openURL(
            url = url,
            options = emptyMap<Any?, Any?>(),
            completionHandler = { success ->
                if (!success) {
                    onFailure()
                }
            },
        )
    }
}
