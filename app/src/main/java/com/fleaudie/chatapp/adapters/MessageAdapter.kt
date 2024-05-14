package com.fleaudie.chatapp.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.fleaudie.chatapp.R
import com.fleaudie.chatapp.data.model.Message
import com.fleaudie.chatapp.databinding.ItemMessageBinding
import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MessageAdapter(private var messages: List<Message>, private var mContext: Context) : RecyclerView.Adapter<MessageAdapter.MessageViewHolder>() {

    inner class MessageViewHolder(var view: ItemMessageBinding) : RecyclerView.ViewHolder(view.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageAdapter.MessageViewHolder {
        val binding : ItemMessageBinding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.item_message, parent, false)
        return MessageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MessageAdapter.MessageViewHolder, position: Int) {
        val message = messages[position]
        val binding = holder.view
        Log.e("tag",message.lastMessage)

        val messageDate = getFormattedDate(message.timestamp)


        if (position > 0) {
            val previousMessage = messages[position - 1]
            val previousMessageDate = getFormattedDate(previousMessage.timestamp)
            if (messageDate == previousMessageDate) {
                binding.txtSeperator.visibility = View.GONE
            } else {
                binding.txtSeperator.visibility = View.VISIBLE
                binding.txtSeperator.text = messageDate
            }
        } else {
            binding.txtSeperator.visibility = View.VISIBLE
            binding.txtSeperator.text = messageDate
        }


        val currentUserId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
        if (message.senderId == currentUserId) {
            binding.txtSender.text = message.message
            binding.consReceive.visibility = View.GONE
            binding.consSend.visibility = View.VISIBLE
        } else {
            binding.txtReceiver.text = message.message
            binding.consSend.visibility = View.GONE
            binding.consReceive.visibility = View.VISIBLE
        }


        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        val time = timeFormat.format(message.timestamp)
        binding.txtSendTime.text = time
        binding.txtReceiveTime.text = time
    }

    override fun getItemCount(): Int {
        return messages.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateMessages(newMessages: List<Message>) {
        val oldSize = messages.size
        messages = newMessages
        val newSize = messages.size
        notifyItemRangeInserted(oldSize, newSize - oldSize)
    }

    private fun getFormattedDate(timestamp: Date): String {
        return SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(timestamp)
    }
}