package com.example.helloworld2

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.user_recycler.*
import kotlinx.android.synthetic.main.user_recycler.view.*


class UserFragment: Fragment() {

    private lateinit var listener : SelectedUser

    companion object {
        fun newInstance() : UserFragment {
            return UserFragment()
        }
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is SelectedUser) {
            listener = context
        } else {
            throw ClassCastException(context.toString() + " must implement OnDogSelected.")
        }
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.user_recycler, container, false)
        val activity = activity as Context
        view.recView.layoutManager = LinearLayoutManager(activity)

        val ump = ViewModelProvider(this).get(DbUserModel::class.java)
        ump.getUsers().observe(viewLifecycleOwner, {
            recView.adapter = UserAdapter(it.sortedBy { that ->
                that.lastname
            }, listener)
        })

        return view
    }
    interface SelectedUser {
        fun onSelect(user: User)
        fun onContactSelect(user: User)
    }
}