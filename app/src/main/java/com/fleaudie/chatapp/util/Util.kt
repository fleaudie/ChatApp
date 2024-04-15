package com.fleaudie.chatapp.util

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential

object AuthUtil {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    fun signInWithPhoneAuthCredential(
        credential: PhoneAuthCredential,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        // Sign in with the received phone authentication credential.
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Sign in with phone authentication succeeded.
                    onSuccess()
                } else {
                    // Sign in with phone authentication failed.
                    onFailure(task.exception!!)
                }
            }
    }
}