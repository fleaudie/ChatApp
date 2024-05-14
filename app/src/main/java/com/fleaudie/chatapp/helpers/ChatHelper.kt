package com.fleaudie.chatapp.helpers

import android.util.Log
import com.fleaudie.chatapp.data.model.ChatInfo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

class ChatHelper @Inject constructor() {
    private val currentUser = FirebaseAuth.getInstance().currentUser
    private val db = FirebaseFirestore.getInstance()

    private val contactNamesMap = mutableMapOf<String, String>()

    init {
        getContactsFromFirestore()
    }

    private fun getContactsFromFirestore() {
        currentUser?.let { user ->
            val userContactsRef = db.collection("users").document(user.uid).collection("contacts")
            userContactsRef.get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        val phoneNumber = document.getString("phoneNumber")
                        val contactName = document.getString("name")
                        phoneNumber?.let { contactNumber ->
                            contactName?.let { name ->
                                contactNamesMap[contactNumber] = name
                            }
                        }
                    }
                }
                .addOnFailureListener { exception ->
                    Log.e(TAG, "Error getting contacts from Firestore: $exception")
                }
        }
    }

    fun listenForLastMessages(userId: String, onSuccess: (List<ChatInfo>) -> Unit, onFailure: (Exception) -> Unit) {
        val userRef = FirebaseFirestore.getInstance().collection("users").document(userId)

        userRef.collection("chats")
            .addSnapshotListener { chatDocuments, exception ->
                if (exception != null) {
                    onFailure(exception)
                    return@addSnapshotListener
                }

                val chatInfoList = mutableListOf<ChatInfo>()

                chatDocuments?.forEach { chatDocument ->
                    val partnerId = chatDocument.id
                    val lastMessage = chatDocument.getString("lastMessage")
                    val lastTimestamp = chatDocument.getDate("timestamp")

                    FirebaseFirestore.getInstance().collection("users").document(partnerId)
                        .get()
                        .addOnSuccessListener { userDocument ->
                            if (userDocument != null) {
                                val profileImageUrl = userDocument.getString("profileImageUrl")
                                val partnerPhoneNumber = userDocument.getString("phoneNumber")
                                val contactName = contactNamesMap[partnerPhoneNumber]

                                val chatInfo = lastTimestamp?.let {
                                    ChatInfo(
                                        lastMessage ?: "",
                                        partnerId,
                                        profileImageUrl ?: "",
                                        partnerPhoneNumber ?: "",
                                        contactName ?: "",
                                        it
                                    )
                                }
                                if (chatInfo != null) {
                                    chatInfoList.add(chatInfo)
                                }
                                onSuccess(chatInfoList)
                            } else {
                                Log.e(TAG, "User document not found for ID: $partnerId")
                            }
                        }
                        .addOnFailureListener { e ->
                            onFailure(e)
                            Log.e(TAG, "Error getting user document for ID: $partnerId, $e")
                        }
                }
            }
    }

    companion object {
        private const val TAG = "ChatHelper"
    }
}