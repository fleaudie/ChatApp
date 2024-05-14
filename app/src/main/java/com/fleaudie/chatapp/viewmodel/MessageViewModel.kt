package com.fleaudie.chatapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fleaudie.chatapp.data.model.Message
import com.fleaudie.chatapp.data.repository.ChatRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MessageViewModel @Inject constructor(private val repository: ChatRepository): ViewModel(){
    private val _messageList = MutableLiveData<List<Message>>()
    val messageList: LiveData<List<Message>> = _messageList

    fun sendMessage(senderId: String, receiverId: String, text: String, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        repository.sendMessage(senderId, receiverId, text, onSuccess, onFailure)
    }

    fun fetchMessages(senderId: String, receiverId: String, onSuccess: (List<Message>) -> Unit, onFailure: (String) -> Unit) {
        repository.fetchMessages(senderId, receiverId,
            onSuccess = { messages ->
                onSuccess(messages)
            },
            onFailure = { error ->
                onFailure(error)
            }
        )
    }
    fun getProfileImageUrls(
        phoneNumber: String,
        onSuccess: (Map<String, String>) -> Unit,
        onFailure: (Exception) -> Unit
    ){
        repository.getProfileImageUrls(phoneNumber, onSuccess, onFailure)
    }
}
