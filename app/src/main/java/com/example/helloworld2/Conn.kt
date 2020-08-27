package com.example.helloworld2

import android.os.Handler
import android.util.Log
import java.io.InputStream
import java.lang.Exception
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class Conn(handler: Handler ) : Runnable {

    private val mHandler = handler

    override fun run() {
        try {
            val url = URL("https://raw.githubusercontent.com/Nikojoel/FSO2020/master/Part2/phonebook/db.json")
            val conn = url.openConnection() as HttpsURLConnection
            conn.requestMethod = "GET"

            val inputStream = conn.inputStream
            val allText = inputStream.bufferedReader().use {
                it.readText()
            }
            val result = StringBuilder()
            result.append(allText)
            val str = result.toString()

            val msg = mHandler.obtainMessage()
            msg.what = 0
            msg.obj = str
            mHandler.sendMessage(msg)

        } catch (e: Exception) {
            Log.d("MyLogs", "error in run: $e")
        }
    }
}