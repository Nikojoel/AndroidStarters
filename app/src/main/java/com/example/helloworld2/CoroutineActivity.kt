package com.example.helloworld2

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import kotlinx.android.synthetic.main.activity_coroutine.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.URL

class CoroutineActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coroutine)

        val imgUrl = URL("https://cdn.pixabay.com/photo/2014/05/02/21/50/home-office-336378_960_720.jpg")

        coroutineButton.setOnClickListener {
            lifecycleScope.launch(Dispatchers.Main) {
                val img = async(Dispatchers.IO) {getImg(imgUrl)}
                showRes(img.await())
            }
        }
    }

    private suspend fun getImg(url: URL) : Bitmap =
        withContext(Dispatchers.IO) {
            return@withContext BitmapFactory.decodeStream(url.openConnection().getInputStream())
        }

    private fun showRes(serverImg: Bitmap) {
        coroutineImage.setImageBitmap(serverImg)
    }
}