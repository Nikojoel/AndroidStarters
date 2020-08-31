package com.example.helloworld2

import android.Manifest
import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import kotlinx.android.synthetic.main.activity_map.*
import kotlin.math.roundToInt

const val REQUEST_CODE = 123

class MapActivity : AppCompatActivity() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var lateLocation: Location
    private var first = false

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        checkPermissions()

        locButton.setOnClickListener {
            fusedLocationClient.requestLocationUpdates(
                createLocationRequest(),
                locationCallback(),
                Looper.getMainLooper()
            )
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == 69) {
            Log.d("MyLogs", grantResults[0].toString()) // 0 is enabled
        }
        if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
            alert(permissions[0])
            Log.d("MyLogs", permissions[0])
        }
    }

    private fun alert(message: String) {
        AlertDialog.Builder(this).setMessage("Please enable $message to use this app")
            .setPositiveButton("Enable") { _, _ ->
                requestPermissions()
            }.create().show()
    }

    private fun createLocationRequest(): LocationRequest? {
        return LocationRequest.create()?.apply {
            interval = 10000
            fastestInterval = 5000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
    }

    private fun locationCallback() = object : LocationCallback() {
        @SuppressLint("SetTextI18n")
        override fun onLocationResult(locationResult: LocationResult?) {
            locationResult ?: return
            val lastLoc = locationResult.locations.size
            Log.d("MyLogs", "$lastLoc")
            locationResult.locations.forEach {location ->
                if (!first) {
                    lateLocation = location
                    first = true
                }
                locationText.text = "Current latitude: ${lateLocation.latitude}\n and longitude: ${lateLocation.longitude}"
                distanceText.text = "Distance from earlier point: ${lateLocation.distanceTo(location).roundToInt() / 1000}km"
                Log.d("MyLogs", "${location.distanceTo(lateLocation)}")
            }
            distanceText.visibility = View.VISIBLE
            locationText.visibility = View.VISIBLE
        }
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            REQUEST_CODE
        )
    }

    private fun checkPermissions() : Boolean {
        return if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions()
            false
        } else {
            true
        }
    }
}