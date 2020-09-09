package com.example.helloworld2

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.user_cell.view.*

class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

class UserAdapter(private val users: List<User>, private val listener: UserFragment.SelectedUser) : RecyclerView.Adapter<UserViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        UserViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.user_cell, parent, false)
    )

    override fun getItemCount() = users.size

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.itemView.userText.text = users[position].toString()
        holder.itemView.setOnClickListener {
            listener.onSelect(users[position])
        }

        holder.itemView.setOnLongClickListener {
            listener.onContactSelect(users[position])
            true
        }
    }
}
