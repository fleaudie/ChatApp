package com.fleaudie.chatapp.data.datasource

import android.app.Activity
import android.content.Context
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.firestore.FirebaseFirestore

class AuthDataSource(private val context: Context) {
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()
    fun sendVerificationCode(
        phoneNumber: String,
        callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    ) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            phoneNumber,
            60, // Timeout duration
            java.util.concurrent.TimeUnit.SECONDS,
            context as Activity,
            callbacks
        )
    }

    fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("SignIn", "successful")
                } else {
                    Log.d("SignIn", "failed")
                }
            }
    }

    fun verifyOtp(verificationId: String, otpCode: String, callback: (Boolean) -> Unit) {
        val credential = PhoneAuthProvider.getCredential(verificationId, otpCode)
        FirebaseAuth.getInstance().signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // OTP verification successful
                    callback(true)
                } else {
                    // OTP verification failed
                    callback(false)
                }
            }
    }
    fun writeUserData(phoneNumber: String, uid: String, name: String, surname: String) {
        val user = hashMapOf(
            "phoneNumber" to phoneNumber,
            "name" to name,
            "surname" to surname,
            "uid" to uid
        )

        db.collection("users")
            .document(uid)
            .set(user)
            .addOnSuccessListener {
                Log.d("AuthDataSource", "User data successfully written to Firestore")
            }
            .addOnFailureListener { e ->
                Log.e("AuthDataSource", "Error writing user data to Firestore: $e")
            }
    }

    fun checkPhoneNumberInDatabase(phoneNumber: String, onSuccess: () -> Unit, onFail: () -> Unit) {

        val usersRef = db.collection("users")
        usersRef.whereEqualTo("phoneNumber", phoneNumber)
            .get()
            .addOnSuccessListener { documents ->
                if (documents.isEmpty) {
                    onSuccess()
                } else {
                    onFail()
                }
            }
            .addOnFailureListener { exception ->
                Log.e("AuthDataSource", "Numara kontrol edilemedi: $exception")
            }
    }
}