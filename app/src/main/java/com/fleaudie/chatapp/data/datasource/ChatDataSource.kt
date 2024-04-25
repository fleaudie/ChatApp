package com.fleaudie.chatapp.data.datasource

import android.util.Log
import com.fleaudie.chatapp.data.model.Message
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Date

class ChatDataSource {
    private val firestore: FirebaseFirestore by lazy {
        FirebaseFirestore.getInstance()
    }

    fun sendMessage(senderId: String, receiverId: String, text: String, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        val currentUser = FirebaseAuth.getInstance().currentUser
        val userUID = currentUser?.uid
        if (userUID != null) {
            val message = Message(senderId, receiverId, text, Date()) // Timestamp olarak Date türünde bir veri kullanıyoruz
            val senderChatRef = getUserChatReference(senderId, receiverId)
            val receiverChatRef = getUserChatReference(receiverId, senderId)

            senderChatRef.collection("messages")
                .add(message)
                .addOnSuccessListener { documentReference ->
                    Log.d("ChatDataSource", "sendMessage: Message sent successfully.")
                    onSuccess()
                }
                .addOnFailureListener { e ->
                    Log.e("ChatDataSource", "sendMessage: ${e.message}")
                    onFailure(e.message ?: "Unknown error occurred")
                }

            receiverChatRef.collection("messages")
                .add(message)
        }
    }

    private fun getUserChatReference(userId: String, chatUserId: String): DocumentReference {
        return firestore.collection("users").document(userId).collection("chats").document(chatUserId)
    }

    fun fetchMessages(senderId: String, receiverId: String, onSuccess: (List<Message>) -> Unit, onFailure: (String) -> Unit) {
        val chatRef = getUserChatReference(senderId, receiverId)
        chatRef.collection("messages")
            .orderBy("timestamp")
            .addSnapshotListener { querySnapshot, exception ->
                if (exception != null) {
                    onFailure(exception.message ?: "Unknown error occurred")
                    return@addSnapshotListener
                }

                val messages = mutableListOf<Message>()
                querySnapshot?.documents?.forEach { document ->
                    val timestamp = document.getDate("timestamp")
                    val message = timestamp?.let { document.toObject(Message::class.java)?.copy(timestamp = it) }
                    message?.let { messages.add(it) }
                }
                onSuccess(messages)
            }
    }

}