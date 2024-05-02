package com.fleaudie.chatapp.helpers

import android.content.ContentResolver
import android.util.Log
import com.fleaudie.chatapp.data.model.Chat
import com.fleaudie.chatapp.data.model.Contact
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import javax.inject.Inject

class ChatHelper @Inject constructor(private val contentResolver: ContentResolver) {
    private val firestore = FirebaseFirestore.getInstance()
    private val currentUser = FirebaseAuth.getInstance().currentUser
    private val db = FirebaseFirestore.getInstance()

    fun getContacts(onSuccess: (List<Chat>) -> Unit, onFailure: (Exception) -> Unit) {
        val contactsList = mutableListOf<Chat>()
        if (currentUser != null) {
            val userId = currentUser.uid

            db.collection("users")
                .document(userId)
                .collection("contacts")
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        val contactName = document.getString("name")
                        val contactPhoneNumber = document.getString("phoneNumber")
                        val chat = contactName?.let {
                            if (contactPhoneNumber != null) {
                                Chat(it, contactPhoneNumber)
                            } else {
                                null
                            }
                        }
                        chat?.let {
                            contactsList.add(it)
                        }
                    }
                    onSuccess(contactsList)
                }
                .addOnFailureListener { exception ->
                    onFailure(exception)
                }
        } else {
            onFailure(Exception("User is null"))
        }
    }

    fun checkPhoneNumber(phoneNumber: String, onSuccess: (uid: String?) -> Unit, onFail: () -> Unit) {
        db.collection("users")
            .whereEqualTo("phoneNumber", phoneNumber)
            .get()
            .addOnSuccessListener { documents ->
                if (!documents.isEmpty) {
                    val uid = documents.documents[0].id
                    onSuccess(uid)
                } else {
                    onFail()
                }
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Error checking phone number", e)
            }
    }

    fun getProfileImageUrls(
        phoneNumber: String,
        onSuccess: (Map<String, String>) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val profileImageUrls = mutableMapOf<String, String>()
        db.collection("users")
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

    fun getLastMessage(chatId: String, onSuccess: (String) -> Unit, onFailure: (Exception) -> Unit) {
        firestore.collection("chats")
            .document(chatId)
            .collection("messages")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .limit(1)
            .get()
            .addOnSuccessListener { documents ->
                if (!documents.isEmpty) {
                    val lastMessage = documents.documents[0].getString("text")
                    onSuccess(lastMessage ?: "")
                } else {
                    onSuccess("")
                }
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
            }
    }

    companion object {
        private const val TAG = "ChatHelper"
    }
}