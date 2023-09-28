package com.missapps.brabet.screens.webview

import android.annotation.SuppressLint
import android.view.ViewGroup.LayoutParams
import android.webkit.CookieManager
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView

@SuppressLint("SetJavaScriptEnabled")
@Suppress("FunctionNaming")
@Composable
fun WebviewScreen(url: String) {

    var goBack by remember { mutableStateOf(Runnable {}) }
    var canGoBack by remember { mutableStateOf(false) }

    BackHandler(enabled = canGoBack, onBack = goBack::run)

    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = {
            WebView(it).apply {
                layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
                settings.apply {
                    javaScriptEnabled = true
                    domStorageEnabled = true
                    cacheMode = WebSettings.LOAD_DEFAULT
                }
                CookieManager.getInstance().setAcceptCookie(true)
                CookieManager.getInstance().setAcceptThirdPartyCookies(this, true)
                webViewClient = object : WebViewClient() {
                    override fun doUpdateVisitedHistory(
                        view: WebView,
                        url: String?,
                        isReload: Boolean
                    ) {
                        super.doUpdateVisitedHistory(view, url, isReload)
                        canGoBack = view.canGoBack()
                    }
                }
                goBack = Runnable { this.goBack() }
                loadUrl(url)
            }
        }
    )
}
