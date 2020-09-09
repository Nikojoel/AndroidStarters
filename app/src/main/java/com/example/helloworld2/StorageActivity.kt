package com.example.helloworld2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_storage.*

const val FILENAME = "myFile.txt"

class StorageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_storage)

        writeButton.setOnClickListener {
            val frag = supportFragmentManager.findFragmentByTag("write")
            val manager = supportFragmentManager.beginTransaction()
            if (frag != null && frag.isVisible) {
                manager.replace(R.id.storageLayout, frag, "write").commit()
            } else {
                manager
                    .replace(R.id.storageLayout, EditFileFragment.newInstance(), "write")
                    .addToBackStack(null)
                    .commit()
            }
        }

        readButton.setOnClickListener {
            val frag = supportFragmentManager.findFragmentByTag("read")
            val manager = supportFragmentManager.beginTransaction()
            if (frag != null && frag.isVisible) {
                manager.replace(R.id.storageLayout, frag, "read").commit()
            } else {
                manager
                    .replace(R.id.storageLayout, ReadFileFragment.newInstance(), "read")
                    .addToBackStack(null)
                    .commit()
            }
        }
    }
}