package com.example.helloworld2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import kotlinx.android.synthetic.main.activity_graph.*

class GraphActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_graph)

        val data = intent.getIntArrayExtra(DATA_MESSAGE)
        val dataPoints = Array(data!!.size) { i -> DataPoint(i.toDouble(), data[i].toDouble()) }
        Log.d("MyLogs", "${dataPoints.size}")
        graphView.title = "Heart Beat Rate"
        graphView.gridLabelRenderer.horizontalAxisTitle = "Datapoints"
        graphView.gridLabelRenderer.verticalAxisTitle = "Bpm"
        graphView.addSeries(LineGraphSeries(dataPoints))
    }
}