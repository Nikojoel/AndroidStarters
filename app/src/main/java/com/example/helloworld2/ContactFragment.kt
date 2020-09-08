package com.example.helloworld2

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.withCreated
import kotlinx.android.synthetic.main.contact_add.view.*
import kotlinx.android.synthetic.main.user_add.view.*
import kotlinx.android.synthetic.main.user_add.view.addUserButton
import kotlinx.android.synthetic.main.user_contact.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ContactFragment: Fragment() {

    companion object {
        private lateinit var user: User
        fun newInstance(user: User) : ContactFragment {
            this.user = user
            return ContactFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.contact_add, container, false)
        val ump = ViewModelProvider(this).get(DbUserModel::class.java)
        view.contactText.text = "Add contact to ${user.firstname} ${user.lastname}"
        view.addContactButton.setOnClickListener{
            GlobalScope.launch {
                val contact = ContactInfo(user.uid, view.editContact.text.toString(), view.editContactType.text.toString())
                ump.insertContact(contact)
                withContext(Dispatchers.Main) {
                    activity?.onBackPressed()
                }
            }
        }
        return view
    }
}