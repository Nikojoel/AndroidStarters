package com.example.helloworld2

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_list.*
import kotlinx.android.synthetic.main.president_cell.*

const val EXTRA_MESSAGE = "president data"

class ListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)

        val adapter = PresidentListAdapter(this, PresidentModel.presidents)
        presidentList.adapter = adapter
    }

    private fun startIntent(url: String) {
        val intent = Intent(this, WebActivity::class.java).apply {
            putExtra(EXTRA_MESSAGE, url)
        }
        startActivity(intent)
    }


    private inner class PresidentListAdapter(context: Context, private val presidents: MutableList<President>) : BaseAdapter() {
        private val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

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
            /*
            ---This wont work, I've no clue why )):):):):):)):)---
            tvName.text = president.name
            tvStartDuty.text = president.startDuty.toString()
            tvEndDuty.text = president.endDuty.toString()
             */
            val tvName = row.findViewById(R.id.tvName) as TextView
            tvName.text = president.name

            val tvStartDuty = row.findViewById(R.id.tvStartDuty) as TextView
            tvStartDuty.text = president.startDuty.toString()

            val tvEndDuty = row.findViewById(R.id.tvEndDuty) as TextView
            tvEndDuty.text = president.endDuty.toString()

            row.setOnClickListener {
                singleText.text = president.toString()
            }

            row.setOnLongClickListener {
                startIntent(tvName.text.toString())
                true
            }
            return row
        }

    }
}


