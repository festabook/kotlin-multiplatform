package com.daedan.festabook.presentation.festating.component.platfom

import android.content.Intent
import android.webkit.WebResourceRequest
import android.webkit.WebView
import androidx.core.net.toUri
import com.multiplatform.webview.web.AccompanistWebViewClient

object AndroidWebViewClient : AccompanistWebViewClient() {
    override fun shouldOverrideUrlLoading(
        view: WebView?,
        request: WebResourceRequest?,
    ): Boolean {
        if (view == null || request == null) return false
        val url = request.url

        url.scheme?.takeIf { it == TOSS_SCHEME || it == "intent" }?.let {
            val intent = Intent.parseUri(url.toString(), Intent.URI_INTENT_SCHEME)
            val packageManager = view.context.packageManager
            if (intent.resolveActivity(packageManager) != null) {
                view.context.startActivity(intent)
                return true
            }

            val packageName = intent.`package` ?: TOSS_PACKAGE_NAME
            val marketIntent = Intent(Intent.ACTION_VIEW, "market://details?id=$packageName".toUri())
            view.context.startActivity(marketIntent)
            return true
        }
        return false
    }

    private const val TOSS_PACKAGE_NAME = "viva.republica.toss"
}
