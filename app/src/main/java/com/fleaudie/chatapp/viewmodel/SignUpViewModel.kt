package com.fleaudie.chatapp.viewmodel

import android.annotation.SuppressLint
import android.app.Activity
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.fleaudie.chatapp.R
import com.fleaudie.chatapp.model.User
import com.fleaudie.chatapp.util.AuthUtil
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Firebase
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.firestore.firestore
import java.util.concurrent.TimeUnit

class SignUpViewModel : ViewModel() {

    // The SignUpViewModel handles user sign-up operations.
    private lateinit var navController: NavController
    private lateinit var auth: FirebaseAuth
    private lateinit var storedVerificationId: String

    @SuppressLint("StaticFieldLeak")
    private lateinit var activity: Activity
    private val db = Firebase.firestore

    fun setActivity(activity: Activity, navController: NavController) {
        // Set the activity and NavController for navigation purposes.
        this.activity = activity
        this.navController = navController
    }

    fun init() {
        // Initialize the FirebaseAuth instance.
        auth = FirebaseAuth.getInstance()
        // Initialize storedVerificationId
        storedVerificationId = ""
    }

    fun signUp(phoneNumber: String) {

        // Initiate phone number verification process.
        if (::activity.isInitialized) {
            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber, 60, TimeUnit.SECONDS, activity,
                object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                        // Automatic verification completed successfully.
                        AuthUtil.signInWithPhoneAuthCredential(credential,
                            onSuccess = {
                                Log.d("SignUpViewModel", "signInWithCredential:success")
                            },
                            onFailure = { exception ->
                                Log.w(
                                    "SignUpViewModel",
                                    "signInWithCredential:failure",
                                    exception
                                )
                            }

                        )
                    }

                    override fun onVerificationFailed(e: FirebaseException) {
                        Log.d("error_x", "signInWithCredential:sending failed")
                    }

                    override fun onCodeSent(
                        verificationId: String,
                        token: PhoneAuthProvider.ForceResendingToken
                    ) {
                        storedVerificationId = verificationId
                        navController.navigate(R.id.action_signUpFragment_to_codeFragment)
                    }

                })


        }

    }

    fun addFirestore(name: String, surname: String, phoneNumber: String) {
        val userId = auth.currentUser?.uid
        checkIfPhoneNumberExists(phoneNumber) { exists ->
            if (exists) {
                Snackbar.make(
                    activity.findViewById(android.R.id.content),
                    "Number already exists. Want to Sign In?",
                    Snackbar.LENGTH_SHORT
                )
                    .setAction("YES") {
                        navController.navigate(R.id.action_signUpFragment_to_signInFragment)
                    }
                    .show()
            } else {
                val user = userId?.let { User(name, surname, phoneNumber, it) }
                if (user != null) {
                    db.collection("users")
                        .add(user)
                        .addOnSuccessListener {
                            Log.d("firestore", user.name)
                        }
                        .addOnFailureListener {

                        }
                }
            }
        }

    }

    private fun checkIfPhoneNumberExists(phoneNumber: String, callback: (Boolean) -> Unit) {
        db.collection("users")
            .whereEqualTo("phoneNumber", phoneNumber)
            .get()
            .addOnSuccessListener { documents ->
                callback(!documents.isEmpty)
            }
            .addOnFailureListener { exception ->
                Log.w("SignUpViewModel", "Error getting documents: ", exception)
                callback(false)
            }
    }

    fun checkIfUserLoggedIn(){
        if (auth.currentUser != null){
            navController.navigate(R.id.action_signUpFragment_to_chatFragment)
        }
    }

}


