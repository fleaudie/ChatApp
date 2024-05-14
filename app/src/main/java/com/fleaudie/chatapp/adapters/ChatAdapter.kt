package com.fleaudie.chatapp.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fleaudie.chatapp.R
import com.fleaudie.chatapp.data.model.ChatInfo
import com.fleaudie.chatapp.databinding.ItemChatsBinding
import com.fleaudie.chatapp.view.ChatsFragmentDirections
import java.text.SimpleDateFormat
import java.util.*

class ChatAdapter(private var chats: List<ChatInfo>, private val mContext: Context) :
    RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {

    inner class ChatViewHolder(var view: ItemChatsBinding) :
        RecyclerView.ViewHolder(view.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val binding : ItemChatsBinding = DataBindingUtil.inflate(
            LayoutInflater.from(mContext),
            R.layout.item_chats,
            parent,
            false)
        return ChatViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val chat = chats[position]
        val t = holder.view

        val dateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
        val currentTime = Calendar.getInstance().timeInMillis
        val messageTime = chat.lastTimestamp.time
        val differenceInMillis = currentTime - messageTime

        val oneDayInMillis = 24 * 60 * 60 * 1000

        val formattedDate: String = if (differenceInMillis >= oneDayInMillis) {
            dateFormat.format(Date(messageTime))
        } else {
            SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(messageTime))
        }

        with(holder.view) {
            txtLastMessage.text = chat.lastMessage
            txtName.text = chat.partnerName
            txtLastMsgDate.text = formattedDate
            Glide.with(mContext).load(chat.profileImage).into(imgProfile)
        }

        t.cardChats.setOnClickListener {
            val action = ChatsFragmentDirections.actionChatFragmentToMessageFragment(
                chat.partnerId,
                chat.partnerName,
                chat.partnerPhoneNumber
            )
            Navigation.findNavController(it).navigate(action)
        }
    }

    override fun getItemCount(): Int {
        return chats.size
    }

    private fun sortChatsByDate(chats: List<ChatInfo>): List<ChatInfo> {
        return chats.sortedByDescending { it.lastTimestamp }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newChats: List<ChatInfo>) {
        chats = sortChatsByDate(newChats)
        notifyDataSetChanged()
    }
}