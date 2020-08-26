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

        var name = intent.getStringExtra(EXTRA_MESSAGE)
        Log.d("MyLogs", name.toString())

        webWiki.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String) : Boolean {
                view?.loadUrl(url)
                return true
            }
        }
        name = name?.replace(" ", "_")
        webWiki.loadUrl("https://wikipedia.fi/wiki/${name}")
    }


}