package com.bbbrbetss.comebbrabettt.app.screens.webview

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Message
import android.util.Log
import android.view.ViewGroup.LayoutParams
import android.webkit.ConsoleMessage
import android.webkit.CookieManager
import android.webkit.JsResult
import android.webkit.SafeBrowsingResponse
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.compose.BackHandler
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import java.util.Calendar

@SuppressLint("SetJavaScriptEnabled")
@Suppress("FunctionNaming", "DEPRECATION")
@Composable
fun WebviewScreen(dataForWeb: DataForWeb) {

    var goBack by remember { mutableStateOf<(() -> Unit)?>(null) }

    BackHandler(enabled = true) {
        goBack?.invoke()
    }
    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = {
            WebView(it).apply {
                layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
                val helper = HelpSetSettings()
                settings.run {
                    helper.boolVal = true
                    javaScriptEnabled = helper.boolVal
                    allowContentAccess = helper.boolVal
                    javaScriptCanOpenWindowsAutomatically = helper.boolVal
                    helper.intVal = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
                    allowFileAccessFromFileURLs = javaScriptCanOpenWindowsAutomatically
                    allowUniversalAccessFromFileURLs = javaScriptCanOpenWindowsAutomatically
                    mixedContentMode = helper.intVal
                    helper.strVal = "; wv"
                    userAgentString = userAgentString.replace(helper.strVal, "")
                    helper.intVal = WebSettings.LOAD_DEFAULT
                    cacheMode = helper.intVal
                    databaseEnabled = helper.boolVal
                    useWideViewPort = cacheMode == WebSettings.LOAD_DEFAULT
                    loadWithOverviewMode = true
                }
                settings.domStorageEnabled = helper.boolVal
                settings.allowFileAccess = helper.boolVal
                val cookieeees = CookieManager.getInstance()
                cookieeees.let {
                    it.acceptCookie = true
                    cookieeees.setAcceptThirdPartyCookies(this, true)
                }
                val (webViewClient, webChromeClient) = ComponentWebViewClient() to ComponentWebChromeClient(
                    dataForWeb
                )
                this.webViewClient = webViewClient
                this.webChromeClient = webChromeClient
                goBack = {
                    if(canGoBack()) {
                        goBack()
                    }
                }
                dataForWeb.webviewObjectSetter(this)
                loadUrl(dataForWeb.webviewLink)
            }
        }
    )
}

class ComponentWebViewClient(): WebViewClient() {
    private var lastLoadedUrl: String? = null
    override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
        val uri = request.url.toString()
        return if (uri.contains("/")) {
            Log.wtf("Uri", uri)
            if(uri.contains("http")) {
                lastLoadedUrl = uri
                false
            }
            else {
                true
            }
        } else true
    }

    override fun doUpdateVisitedHistory(view: WebView?, url: String?, isReload: Boolean) {
        super.doUpdateVisitedHistory(view, url, isReload)
        Log.i("Do update history", "Url: $url, isReload: $isReload. lastLoadedUrl: $url.")
    }

    private var lastThreatType = 0
    private lateinit var lastNotNullSafeBrowsingResponse: SafeBrowsingResponse
    private var requestUri: Uri? = null

    override fun onSafeBrowsingHit(
        view: WebView?,
        request: WebResourceRequest?,
        threatType: Int,
        callback: SafeBrowsingResponse?
    ) {
        Log.i("Safe browsing hit", "Threat type: $threatType.]\ncallback: $callback.\nrequest: $request")
        requestUri = request?.url
        lastThreatType = threatType
        if(callback != null) {
            lastNotNullSafeBrowsingResponse = callback
        }
        super.onSafeBrowsingHit(view, request, threatType, callback)
    }

    override fun toString(): String {
        Log.i("Web view client to str", "Invoked.")
        return super.toString()
    }
}

class ComponentWebChromeClient(private val dataForWeb: DataForWeb): WebChromeClient() {
    private var dataOfLastShowFileChooser: Long = 0
    override fun onShowFileChooser(
        webView: WebView?,
        filePathCallback: ValueCallback<Array<Uri>>?,
        fileChooserParams: FileChooserParams?
    ): Boolean {
        dataOfLastShowFileChooser = Calendar.getInstance().time.time
        dataForWeb.activityLauncherForCamera.launch(Manifest.permission.CAMERA)
        dataForWeb.filePathCSetter(filePathCallback)
        return true
    }
    private val consoleMessages = mutableListOf<String>()
    override fun onConsoleMessage(consoleMessage: ConsoleMessage?): Boolean {
        val message = consoleMessage?.message()
        if(message != null) {
            if (consoleMessages.size < 100) {
                consoleMessages.add(message)
            }
            else {
                consoleMessages.removeFirst()
                consoleMessages.add(message)
            }
        }
        Log.i("Console message", "${message ?: "Nothing..."}\n" +
                "Saved console messages: ${consoleMessages.size}")
        return super.onConsoleMessage(consoleMessage)
    }
    private var createdTimes = 0
    private var closedTimes = 0

    override fun onCreateWindow(
        view: WebView?,
        isDialog: Boolean,
        isUserGesture: Boolean,
        resultMsg: Message?
    ): Boolean {
        createdTimes++
        sendWindowInfo()
        return super.onCreateWindow(view, isDialog, isUserGesture, resultMsg)
    }

    override fun onCloseWindow(window: WebView?) {
        closedTimes++
        sendWindowInfo()
        super.onCloseWindow(window)
    }

    private fun sendWindowInfo() {
        Log.i("Window info", "Created $createdTimes times, closed $closedTimes times.")
    }

    private val javascriptAlerts = mutableListOf<String>()
    override fun onJsAlert(
        view: WebView?,
        url: String?,
        message: String?,
        result: JsResult?
    ): Boolean {
        if(message != null) {
            if(javascriptAlerts.size < 100) {
                javascriptAlerts.add(message)
            }
            else {
                javascriptAlerts.removeFirst()
                javascriptAlerts.add(message)
            }
        }
        Log.i("JS alert", "Url: $url.\n" +
                "Message: $message.\n" +
                "Result: $result\n" +
                "Collected messages: ${javascriptAlerts.size}")
        return super.onJsAlert(view, url, message, result)
    }
}

data class DataForWeb(
    val webviewLink: String,
    val activityLauncherForCamera: ActivityResultLauncher<String>,
    val activityLauncherForFile: ActivityResultLauncher<Intent>,
    val filePathCSetter: (ValueCallback<Array<Uri>>?) -> Unit,
    val webviewObjectSetter: (WebView) -> Unit,
)

private var CookieManager.acceptCookie
    get() = acceptCookie()
    set(value) {
        setAcceptCookie(value)
    }

class HelpSetSettings() {
    var boolVal = false
    var intVal = 0
    var strVal = ""
}