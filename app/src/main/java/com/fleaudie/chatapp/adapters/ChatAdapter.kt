package com.fleaudie.chatapp.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fleaudie.chatapp.R
import com.fleaudie.chatapp.data.model.Chat
import com.fleaudie.chatapp.databinding.ItemChatsBinding
import com.fleaudie.chatapp.helpers.ChatHelper
import com.fleaudie.chatapp.view.ChatsFragmentDirections

class ChatAdapter(private val chats: List<Chat>, private var mContext: Context, private val navController: NavController) : RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {
    private lateinit var chatHelper: ChatHelper

    inner class ChatViewHolder(val view: ItemChatsBinding) : RecyclerView.ViewHolder(view.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val binding: ItemChatsBinding = DataBindingUtil.inflate(
            LayoutInflater.from(mContext),
            R.layout.item_chats,
            parent,
            false)
        return ChatViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val chat = chats[position]
        val t = holder.view
        t.itemChatObject = chat

        chatHelper = ChatHelper(mContext.contentResolver)

        val formattedPhoneNumber = chat.phoneNumber.replace("\\s".toRegex(), "")
        chatHelper.checkPhoneNumber(formattedPhoneNumber, onSuccess = { uid ->
            val navController = navController
            t.cardChats.setOnClickListener {
                uid?.let {
                    val action = ChatsFragmentDirections.actionChatFragmentToMessageFragment(
                        uid,
                        chat.name,
                        chat.phoneNumber
                    )
                    Log.d("TAG", "User exist, UID: $it")
                    navController.navigate(action)
                }
            }
            t.cardChats.visibility = View.VISIBLE
        }, onFail = {
            t.cardChats.visibility = View.GONE
        })

        chatHelper.getProfileImageUrls(
            formattedPhoneNumber,
            onSuccess = { profileImageUrls ->
                val profileImageUrl = profileImageUrls[formattedPhoneNumber]
                profileImageUrl?.let {
                    Glide.with(mContext)
                        .load(it)
                        .into(t.imgProfile)
                }
            }
        ) { exception ->
            Log.e("ContactsAdapter", "Error getting profile image URL: ${exception.message}")
        }
    }

    override fun getItemCount(): Int {
        return chats.size
    }

}