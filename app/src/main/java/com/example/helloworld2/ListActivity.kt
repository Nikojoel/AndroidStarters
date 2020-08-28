package com.example.helloworld2

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.activity_list.*
import kotlinx.android.synthetic.main.president_cell.view.*

const val EXTRA_MESSAGE = "president data"
const val log = "MyLogs"

class ListActivity : AppCompatActivity() {

    lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)

        val adapter = PresidentListAdapter(this, PresidentModel.presidents)
        presidentList.adapter = adapter

        // Init
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        // Observe
        viewModel.data.observe(this) {
            Log.d(log, it.query?.searchinfo?.totalhits.toString())
            setHitText(it.query?.searchinfo?.totalhits.toString())
        }
    }

    private fun setHitText(text: String) {
        hitsText.text = "Hits: $text"
    }

    private fun startIntent(url: String) {
        val intent = Intent(this, WebActivity::class.java).apply {
            putExtra(EXTRA_MESSAGE, url)
        }
        startActivity(intent)
    }


    private inner class PresidentListAdapter(
        context: Context,
        private val presidents: MutableList<President>
    ) : BaseAdapter() {
        private val inflater =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        override fun getCount(): Int {
            return presidents.size
        }

        override fun getItem(position: Int): Any {
            return presidents[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
            val row = inflater.inflate(R.layout.president_cell, p2, false)
            val president = presidents[p0]

            row.tvName.text = president.name
            row.tvStartDuty.text = president.startDuty.toString()
            row.tvEndDuty.text = president.endDuty.toString()

            row.setOnClickListener {
                singleText.text = president.toString()
                viewModel.changePresident(president.name)
                //setHitText(withContext(Dispatchers.IO){viewModel.repository.getPresident(president.name).toString()})
            }

            row.setOnLongClickListener {
                startIntent(president.name)
                true
            }
            return row
        }

    }
}


