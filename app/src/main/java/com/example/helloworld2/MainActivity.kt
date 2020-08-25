package com.example.helloworld2

import android.app.ApplicationErrorReport
import android.app.NotificationChannel
import android.app.NotificationManager
import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.os.BatteryManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import kotlinx.android.synthetic.main.activity_main.*
import com.google.android.material.snackbar.Snackbar

const val CHANNEL_ID = "123"
const val logs = "MyLogs"

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        createNotifChannel()

        // Notify user if bluetooth is not on
        if (!BluetoothAdapter.getDefaultAdapter().isEnabled) showNotification()

        helloButton.setOnClickListener {
            helloText.text = if (helloText.text == getString(R.string.helloText)) getString(R.string.summerText) else getString(R.string.helloText)
            showSnack(it)
        }
        actionButton.setOnClickListener {
            Toast.makeText(this, getString(R.string.actionText), Toast.LENGTH_SHORT).show()
        }
    } // OnCreate Ends

    private fun showSnack (view: View) {
        Snackbar.make(view ,R.string.changeText, Snackbar.LENGTH_LONG).setAction(R.string.dismissText) {
            Log.d(logs, "snackbar clicked")
        }.show()
    }

    private fun createNotifChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                getString(R.string.channelName),
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = getString(R.string.notifChannel)
            }
            val notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE)
            as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun showNotification() {
        val notif = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground )
            .setContentTitle(getString(R.string.btOn))
            .setContentText(getString(R.string.btText))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()
        NotificationManagerCompat.from(this).notify(1, notif)
    }
}