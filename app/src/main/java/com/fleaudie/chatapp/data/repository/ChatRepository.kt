package com.fleaudie.chatapp.data.repository

import com.fleaudie.chatapp.data.datasource.ChatDataSource
import com.fleaudie.chatapp.data.model.Message

class ChatRepository(private val dataSource: ChatDataSource) {
    fun sendMessage(senderId: String, receiverId: String, text: String, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        dataSource.sendMessage(senderId, receiverId, text, onSuccess, onFailure)
    }

    fun fetchMessages(senderId: String, receiverId: String, onSuccess: (List<Message>) -> Unit, onFailure: (String) -> Unit){
        dataSource.fetchMessages(senderId, receiverId, onSuccess, onFailure)
    }

}