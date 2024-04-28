package com.fleaudie.chatapp.data.datasource

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID

class UserProfileDataSource {
    private val storageRef = FirebaseStorage.getInstance().reference
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    fun uploadProfileImage(imageBytes: ByteArray, onSuccess: (String) -> Unit, onFailure: (Exception) -> Unit) {
        val imageName = "${UUID.randomUUID()}.jpg"
        val imageRef = storageRef.child("profile_images/$imageName")

        imageRef.putBytes(imageBytes)
            .addOnSuccessListener {
                imageRef.downloadUrl.addOnSuccessListener { uri ->
                    updateUserProfileImageUrl(uri.toString(), onSuccess, onFailure)
                }.addOnFailureListener {
                    onFailure(it)
                }
            }
            .addOnFailureListener {
                onFailure(it)
            }
    }

    private fun updateUserProfileImageUrl(imageUrl: String, onSuccess: (String) -> Unit, onFailure: (Exception) -> Unit) {
        val currentUserUid = getCurrentUserUid()

        if (currentUserUid != null) {
            val userRef = db.collection("users").document(currentUserUid)

            val updates = hashMapOf<String, Any>(
                "profileImageUrl" to imageUrl,
            )

            userRef.update(updates)
                .addOnSuccessListener {
                    onSuccess(imageUrl)
                }
                .addOnFailureListener { exception ->
                    onFailure(exception)
                }
        } else {
            onFailure(Exception("Current user UID is null"))
        }
    }

    fun getProfileImageUrl(callback: (String?) -> Unit) {
        val currentUserUid = getCurrentUserUid()

        if (currentUserUid != null) {
            val userRef = db.collection("users").document(currentUserUid)

            userRef.get()
                .addOnSuccessListener { documentSnapshot ->
                    if (documentSnapshot.exists()) {
                        val profileImageUrl = documentSnapshot.getString("profileImageUrl")
                        callback(profileImageUrl)
                    } else {
                        callback(null)
                    }
                }
                .addOnFailureListener { exception ->
                    Log.e("UserProfileDataSource", "Error getting profile image URL: $exception")
                    callback(null)
                }
        } else {
            callback(null)
        }
    }

    private fun getCurrentUserUid(): String? {
        return auth.currentUser?.uid
    }

    fun getUserData(callback: (String?, String?, String?, String?) -> Unit) {
        val currentUserUid = getCurrentUserUid()

        if (currentUserUid != null) {
            val userRef = db.collection("users").document(currentUserUid)

            userRef.get()
                .addOnSuccessListener { documentSnapshot ->
                    if (documentSnapshot.exists()) {
                        val name = documentSnapshot.getString("name")
                        val surname = documentSnapshot.getString("surname")
                        val phone = documentSnapshot.getString("phoneNumber")
                        if (name != null && surname != null) {
                            callback(name, surname, phone, currentUserUid)
                        } else {
                            callback(null, null, null, null)
                        }
                    } else {
                        callback(null, null, null, null)
                    }
                }
                .addOnFailureListener { exception ->
                    Log.e("AuthDataSource", "Error getting user data: $exception")
                    callback(null, null, null, null)
                }
        } else {
            callback(null, null, null, null)
        }
    }

    fun updateUserName(newName: String, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        val currentUserUid = getCurrentUserUid()

        if (currentUserUid != null) {
            val userRef = db.collection("users").document(currentUserUid)

            val updates = hashMapOf<String, Any>(
                "name" to newName,
            )

            userRef.update(updates)
                .addOnSuccessListener {
                    onSuccess()
                }
                .addOnFailureListener { exception ->
                    onFailure(exception)
                }
        } else {
            onFailure(Exception("Current user UID is null"))
        }
    }

    fun updateUserSurname(newSurname: String, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        val currentUserUid = getCurrentUserUid()

        if (currentUserUid != null) {
            val userRef = db.collection("users").document(currentUserUid)

            val updates = hashMapOf<String, Any>(
                "surname" to newSurname,
            )

            userRef.update(updates)
                .addOnSuccessListener {
                    onSuccess()
                }
                .addOnFailureListener { exception ->
                    onFailure(exception)
                }
        } else {
            onFailure(Exception("Current user UID is null"))
        }
    }

    fun updateUserNumber(newNumber: String, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        val currentUserUid = getCurrentUserUid()

        if (currentUserUid != null) {
            val userRef = db.collection("users").document(currentUserUid)

            val updates = hashMapOf<String, Any>(
                "phoneNumber" to newNumber,
            )

            userRef.update(updates)
                .addOnSuccessListener {
                    onSuccess()
                }
                .addOnFailureListener { exception ->
                    onFailure(exception)
                }
        } else {
            onFailure(Exception("Current user UID is null"))
        }
    }
}