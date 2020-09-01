package com.example.helloworld2

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.activity_sensor.*

class SensorActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private var proxSensor: Sensor? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sensor)

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        proxSensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY)

        if (proxSensor !== null) {
            Log.d("MyLogs", "found")
        } else {
            alert("No $proxSensor sensor found")
        }
    }

    override fun onSensorChanged(p0: SensorEvent?) {
         if (p0?.sensor == proxSensor) {
             Log.d("MyLogs", "${p0?.values?.get(0)}")
             val data = p0?.values?.get(0)?.toInt()

             if (data == 0) sensorText.text = getString(R.string.nearText) else sensorText.text = getString(R.string.closeText)
             } else {
                Log.d("MyLogs", "something isnt right")
         }
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
        Log.d("MyLogs", "new accuracy: $p1")
    }

    private fun alert(message: String) {
        AlertDialog.Builder(this).setMessage(message)
            .setPositiveButton("Ok") { _, _ ->

            }.create().show()
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onResume() {
        super.onResume()
        proxSensor?.also {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL, SensorManager.SENSOR_STATUS_ACCURACY_MEDIUM)
        }
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

}