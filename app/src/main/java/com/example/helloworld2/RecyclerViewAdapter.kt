package com.example.helloworld2

import android.annotation.SuppressLint
import android.bluetooth.le.ScanResult
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.bt_cell.view.*

class RecyclerViewAdapter(private val context: Context, private val scanResults: HashMap<String, ScanResult>?, private val keys: MutableList<String>) : RecyclerView.Adapter<ViewHolder>() {

    // Inflates the itemView
    override fun onCreateViewHolder(vg: ViewGroup, vt: Int): ViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.bt_cell, vg, false)
        return ViewHolder(itemView)
    }

    // Returns the list size
    override fun getItemCount() = keys.size

    // Sets the textView to value that is found at the index of pos
    @SuppressLint("ResourceAsColor")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(vh: ViewHolder, pos: Int) {
        Log.d("MyLogs", scanResults.toString())
        val result = scanResults?.get(keys[pos])
        if (result != null) {
            if (!result.isConnectable) {
                vh.itemView.btdName.setTextColor(Color.parseColor("#23000000"))
                vh.itemView.btdMac.setTextColor(Color.parseColor("#23000000"))
                vh.itemView.btdRs.setTextColor(Color.parseColor("#23000000"))
            }
            vh.itemView.btdName.text = if (result.device.name == null) "N/A" else result.device.name
            vh.itemView.btdMac.text = result.device.address
            vh.itemView.btdRs.text = "${result.rssi}dBm"
            Log.d("MyLogs", "${result.isConnectable}")
        }
    }
}