package com.example

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color as ComposeColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      MyApplicationTheme {
        CellflowApp()
      }
    }
  }
}

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun CellflowApp() {
  val context = LocalContext.current
  val webView = remember {
    WebView(context).apply {
      layoutParams = ViewGroup.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.MATCH_PARENT
      )
      setBackgroundColor(Color.parseColor("#0A0E14"))
      setLayerType(View.LAYER_TYPE_HARDWARE, null)
      isVerticalScrollBarEnabled = false
      isHorizontalScrollBarEnabled = false
      overScrollMode = View.OVER_SCROLL_NEVER

      settings.apply {
        javaScriptEnabled = true
        domStorageEnabled = true
        databaseEnabled = true
        allowFileAccess = true
        allowContentAccess = true
        mediaPlaybackRequiresUserGesture = false
        useWideViewPort = true
        loadWithOverviewMode = true
        cacheMode = WebSettings.LOAD_DEFAULT
      }

      webChromeClient = WebChromeClient()
      webViewClient = object : WebViewClient() {
        override fun onPageFinished(view: WebView?, url: String?) {
          super.onPageFinished(view, url)
        }
      }

      loadUrl("file:///android_asset/index.html")
    }
  }

  DisposableEffect(webView) {
    onDispose {
      webView.destroy()
    }
  }

  BackHandler {
    webView.evaluateJavascript(
      "(function() { " +
        "var modal = document.querySelector('.modal-backdrop.active'); " +
        "if (modal) { modal.classList.remove('active'); return true; } " +
        "return false; " +
      "})()"
    ) { result ->
      if (result != "true") {
        if (webView.canGoBack()) {
          webView.goBack()
        }
      }
    }
  }

  Box(
    modifier = Modifier
      .fillMaxSize()
      .background(ComposeColor(0xFF0A0E14))
      .safeDrawingPadding()
  ) {
    AndroidView(
      modifier = Modifier.fillMaxSize(),
      factory = { webView }
    )
  }
}
