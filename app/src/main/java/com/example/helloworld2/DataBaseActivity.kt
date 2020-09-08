package com.example.helloworld2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_data_base.*

class DataBaseActivity : AppCompatActivity(), UserFragment.SelectedUser {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data_base)

        supportFragmentManager
            .beginTransaction()
            .add(R.id.main_layout, UserFragment.newInstance())
            .commit()

        addButton.setOnClickListener {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.main_layout, EditFragment.newInstance()).addToBackStack(null)
                .commit()
        }
    }

    override fun onSelect(user: User) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.main_layout, ContactFragment.newInstance(user)).addToBackStack(null)
            .commit()
    }

    override fun onContactSelect(user: User) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.main_layout, UserContactFragment.newInstance(user)).addToBackStack(null)
            .commit()
    }
}
