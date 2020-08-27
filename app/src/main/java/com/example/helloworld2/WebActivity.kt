package com.example.helloworld2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient
import kotlinx.android.synthetic.main.activity_web.*

class WebActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web)

        val name = intent.getStringExtra(EXTRA_MESSAGE)?.replace(" ", "_")
        Log.d("MyLogs", name.toString())

        webWiki.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String) : Boolean {
                view?.loadUrl(url)
                return true
            }
        }
        webWiki.loadUrl("https://wikipedia.fi/wiki/$name")
    }


}