package com.fleaudie.chatapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fleaudie.chatapp.data.model.Message
import com.fleaudie.chatapp.data.repository.ChatRepository

class ChatViewModel(private val repository: ChatRepository) : ViewModel() {
    private val _chatList = MutableLiveData<List<Message>>()
    val chatList: LiveData<List<Message>> = _chatList

    fun fetchLatestMessages(userId: String) {
        repository.getLatestMessages(userId,
            onSuccess = { messages ->
                _chatList.value = messages
            },
            onFailure = { error ->
                Log.e("ChatViewModel", "Error fetching latest messages: $error")
            }
        )
    }
}