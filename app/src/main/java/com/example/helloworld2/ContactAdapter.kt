package com.example.helloworld2

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.contact_cell.view.*
import kotlinx.android.synthetic.main.user_cell.view.*

class ContactViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

class ContactAdapter(private val contacts: UserContact) : RecyclerView.Adapter<ContactViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ContactViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.contact_cell, parent, false))

    override fun getItemCount() = contacts.contacts?.size ?: 0

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        Log.d("MyLogs", "contact.contact ${contacts.contacts?.get(0)?.toString()}")
        holder.itemView.contactCellText.text = contacts.contacts?.get(position).toString()
        Log.d("MyLogs", "position contact ${contacts.contacts?.get(position).toString()}")
    }
}

