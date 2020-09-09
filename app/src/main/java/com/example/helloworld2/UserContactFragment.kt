package com.example.helloworld2

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.user_contact.*
import kotlinx.android.synthetic.main.user_contact.view.*

class UserContactFragment: Fragment() {

    companion object {
        private lateinit var user: User
        fun newInstance(user: User) : UserContactFragment {
            this.user = user
            return UserContactFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.user_contact, container, false)
        val activity = activity as Context
        view.contactRecView.layoutManager = LinearLayoutManager(activity)

        val ump = ViewModelProvider(this).get(DbUserModel::class.java)
        view.userContactText.text = "Contacts for ${user.firstname}"
        ump.getContacts(user.uid).observe(viewLifecycleOwner, {
            contactRecView.adapter = ContactAdapter(it)
        })

        return view
    }
}