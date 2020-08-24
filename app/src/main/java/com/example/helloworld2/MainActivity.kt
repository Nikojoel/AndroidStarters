package com.example.helloworld2

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import kotlinx.android.synthetic.main.activity_main.*
import com.google.android.material.snackbar.Snackbar

const val CHANNEL_ID = "..."

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        helloButton.setOnClickListener {
            helloText.text = if (helloText.text == getString(R.string.helloText)) getString(R.string.summerText) else getString(R.string.helloText)
            showSnack(it)
            showNotification()
        }
    } // OnCreate Ends

    private fun showSnack (view: View) {
        Snackbar.make(view ,R.string.changeText, Snackbar.LENGTH_LONG).setAction(R.string.dismissText) {
            Log.d("debug", "snackbar clicked")
        }.show()
    }

    private fun createNotifChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                getString(R.string.channelName),
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "notif description"
            }
            val notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE)
            as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun showNotification() {
        createNotifChannel()
        val notif = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground )
            .setContentTitle("test notification")
            .setContentText("this is a notification")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()
        NotificationManagerCompat.from(this).notify(1, notif)
    }
}