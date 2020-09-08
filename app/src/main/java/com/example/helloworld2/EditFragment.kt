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
import kotlinx.android.synthetic.main.user_add.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EditFragment: Fragment() {
    companion object {
        fun newInstance() : EditFragment {
            return EditFragment()
        }
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.user_add, container, false)
        val ump = ViewModelProvider(this).get(DbUserModel::class.java)
        view.addUserButton.setOnClickListener{
            GlobalScope.launch {
                val user = User(0, view.editFirstName.text.toString(), view.editSurName.text.toString())
                ump.insertNew(user)
                withContext(Dispatchers.Main) {
                    activity?.onBackPressed()
                }
            }
        }
        return view
    }
}