package com.example.helloworld2

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanFilter
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_blue_tooth.*

@RequiresApi(Build.VERSION_CODES.O)
class BlueToothActivity : AppCompatActivity() {

    private var btAdapter: BluetoothAdapter? = null
    private var scanResults: HashMap<String, ScanResult>? = HashMap()
    private lateinit var recyclerViewAdapter: RecyclerViewAdapter
    private var keys = mutableListOf<String>()

    companion object {
        const val SCAN_PERIOD: Long = 3000
        const val log: String = "MyLogs"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_blue_tooth)

        val btManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        btAdapter = btManager.adapter
        hasPermissions()

        // Sets adapter and layout manager for the recycler view
        initRecycler()

        btButton.setOnClickListener {
            btProgBar.visibility = View.VISIBLE
            btText.visibility = View.VISIBLE
            startScanning()
        }
    }

    private fun initRecycler() {
        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@BlueToothActivity)
            recyclerViewAdapter = RecyclerViewAdapter(this@BlueToothActivity, scanResults!!, keys)
            adapter = recyclerViewAdapter
        }
    }
    private fun hasPermissions(): Boolean {
        if (btAdapter == null || !btAdapter!!.isEnabled) {
            Log.d(log, "No Bluetooth LE capability")
            return false
        } else if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) !=
            PackageManager.PERMISSION_GRANTED) {
            Log.d(log, "No fine location access")
            requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1);
            return true // assuming that the user grants permission
        }
        return true
    }

    private fun startScanning() {
        Log.d(log,"Start scanning")
        val scanCallBack = BtLeScanCallback()
        val bluetoothScanner = btAdapter!!.bluetoothLeScanner

        val settings = ScanSettings.Builder()
            .setScanMode(ScanSettings.SCAN_MODE_LOW_POWER)
            .build()

        val filter: List<ScanFilter>? = null

        // Stop scan after period
        val handler = Handler()
        handler.postDelayed({
            bluetoothScanner.stopScan(scanCallBack)
            btProgBar.visibility = View.GONE
            btText.visibility = View.GONE
            Log.d(log, "Stopped scanning")
            scanResults?.keys?.forEach{
                keys.add(it)
            }
            recyclerViewAdapter.notifyDataSetChanged()
        }, SCAN_PERIOD)

        bluetoothScanner!!.startScan(filter, settings, scanCallBack)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private inner class BtLeScanCallback : ScanCallback() {

        override fun onScanResult(callbackType: Int, result: ScanResult) {
            addScanResult(result)
            Log.d(log,"Added result")
        }

        override fun onBatchScanResults(results: List<ScanResult>) {

        }

        override fun onScanFailed(errorCode: Int) {
            Log.d("DBG", "BLE Scan Failed with code $errorCode")
        }

        private fun addScanResult(result: ScanResult) {
            scanResults?.set(result.device.address, result)
        }
    }
}

