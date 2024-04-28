package com.fleaudie.chatapp.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fleaudie.chatapp.R
import com.fleaudie.chatapp.databinding.ItemContactsBinding
import com.fleaudie.chatapp.data.model.Contact
import com.fleaudie.chatapp.helpers.ContactHelper
import com.fleaudie.chatapp.view.ContactsFragmentDirections

class ContactsAdapter(private val contacts: List<Contact>, private var mContext: Context) :
    RecyclerView.Adapter<ContactsAdapter.ContactViewHolder>() {

    private lateinit var contactHelper: ContactHelper

    inner class ContactViewHolder(var view: ItemContactsBinding) :
        RecyclerView.ViewHolder(view.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val binding: ItemContactsBinding = DataBindingUtil.inflate(
            LayoutInflater.from(mContext),
            R.layout.item_contacts,
            parent,
            false
        )
        return ContactViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val contact = contacts[position]
        val t = holder.view
        t.itemContactObject = contact

        contactHelper = ContactHelper(mContext.contentResolver)

        val formattedPhoneNumber = contact.contactNumber.replace("\\s".toRegex(), "")
        contactHelper.checkPhoneNumber(formattedPhoneNumber, onSuccess = { uid ->
            val navController = findNavController(holder.itemView)
            t.cardContacts.setOnClickListener {
                uid?.let {
                    val action = ContactsFragmentDirections.actionContactsFragmentToMessageFragment(
                        uid,
                        contact.contactName,
                        contact.contactNumber
                    )
                    Log.d("TAG", "User exist, UID: $it")
                    navController.navigate(action)
                }
            }
            t.cardContacts.visibility = View.VISIBLE
            t.btnInvite.visibility = View.GONE
        }, onFail = {
            t.cardContacts.visibility = View.VISIBLE
            t.btnInvite.visibility = View.VISIBLE
        })

        contactHelper.getProfileImageUrls(
            formattedPhoneNumber,
            onSuccess = { profileImageUrls ->
                val profileImageUrl = profileImageUrls[formattedPhoneNumber]
                profileImageUrl?.let {
                    Glide.with(mContext)
                        .load(it)
                        .into(t.imgContactProfile)
                }
            }
        ) { exception ->
            Log.e("ContactsAdapter", "Error getting profile image URL: ${exception.message}")
        }
    }

    override fun getItemCount(): Int {
        return contacts.size
    }
}