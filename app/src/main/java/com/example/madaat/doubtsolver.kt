package com.example.madaat

import android.os.Build
import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity

class doubtsolver : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chatbot)
        val chatbotWebView = findViewById<WebView>(R.id.chatbotWebView)
        val webSettings = chatbotWebView.settings
        webSettings.javaScriptEnabled = true
        webSettings.domStorageEnabled = true
        webSettings.loadsImagesAutomatically = true
        webSettings.javaScriptCanOpenWindowsAutomatically = true
        webSettings.setSupportMultipleWindows(true)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        }

        chatbotWebView.webViewClient = WebViewClient()
        chatbotWebView.webChromeClient = WebChromeClient()
        chatbotWebView.loadUrl("https://www.chatbase.co/chatbot-iframe/kw2BH-4ZQeFsj1DqKGs35")
    }
}

