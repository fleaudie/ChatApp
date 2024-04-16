package com.fleaudie.chatapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.fleaudie.chatapp.R
import com.fleaudie.chatapp.databinding.ItemContactsBinding
import com.fleaudie.chatapp.model.Contact

class ContactsAdapter(private val contacts: List<Contact>, private var mContext: Context) : RecyclerView.Adapter<ContactsAdapter.ContactViewHolder>() {
    inner class ContactViewHolder(var view: ItemContactsBinding) : RecyclerView.ViewHolder(view.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val binding : ItemContactsBinding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.item_contacts, parent, false)
        return ContactViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val contact = contacts[position]
        val t = holder.view
        t.itemContactObject = contact
    }

    override fun getItemCount(): Int {
        return contacts.size
    }
}