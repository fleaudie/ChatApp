package com.fleaudie.chatapp.data.datasource

import android.util.Log
import com.fleaudie.chatapp.data.model.Message
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import java.util.Calendar
import java.util.Date

class ChatDataSource {
    private val firestore: FirebaseFirestore by lazy {
        FirebaseFirestore.getInstance()
    }

    fun sendMessage(senderId: String, receiverId: String, text: String, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        val currentUser = FirebaseAuth.getInstance().currentUser
        val userUID = currentUser?.uid
        if (userUID != null) {
            val message = Message(senderId, receiverId, text, Date())
            val senderChatRef = getUserChatReference(senderId, receiverId)
            val receiverChatRef = getUserChatReference(receiverId, senderId)


            val currentTime = Calendar.getInstance().time
            val lastMessageData = mapOf(
                "lastMessage" to text,
                "timestamp" to currentTime
            )


            senderChatRef.set(lastMessageData, SetOptions.merge())
                .addOnSuccessListener {
                    Log.d("ChatDataSource", "sendMessage: Sender chat document updated")
                }
                .addOnFailureListener { e ->
                    Log.e("ChatDataSource", "sendMessage: Error updating sender chat document", e)
                }


            receiverChatRef.set(lastMessageData, SetOptions.merge())
                .addOnSuccessListener {
                    Log.d("ChatDataSource", "sendMessage: Receiver chat document updated")
                }
                .addOnFailureListener { e ->
                    Log.e("ChatDataSource", "sendMessage: Error updating receiver chat document", e)
                }


            senderChatRef.collection("messages")
                .add(message)
                .addOnSuccessListener {
                    Log.d("ChatDataSource", "sendMessage: Message sent to $receiverId")
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


    fun fetchMessages(
        senderId: String,
        receiverId: String,
        onSuccess: (List<Message>) -> Unit,
        onFailure: (String) -> Unit
    ) {
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
                    message?.let {
                        messages.add(it)
                    }
                }

                onSuccess(messages)
            }
    }


    fun getProfileImageUrls(
        phoneNumber: String,
        onSuccess: (Map<String, String>) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val profileImageUrls = mutableMapOf<String, String>()
        firestore.collection("users")
            .whereEqualTo("phoneNumber", phoneNumber)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val number = document.getString("phoneNumber") ?: continue
                    val profileImageUrl = document.getString("profileImageUrl")
                    if (!profileImageUrl.isNullOrEmpty()) {
                        profileImageUrls[number] = profileImageUrl
                    }
                }
                onSuccess(profileImageUrls)
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
            }
    }

}