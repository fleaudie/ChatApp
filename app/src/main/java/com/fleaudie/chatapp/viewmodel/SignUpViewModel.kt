package com.fleaudie.chatapp.viewmodel

import android.annotation.SuppressLint
import android.app.Activity
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.fleaudie.chatapp.R
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

class SignUpViewModel : ViewModel() {

    // The SignUpViewModel handles user sign-up operations.
    private lateinit var navController: NavController
    private lateinit var auth: FirebaseAuth
    private lateinit var storedVerificationId: String
    @SuppressLint("StaticFieldLeak")
    private lateinit var activity: Activity

    fun setActivity(activity: Activity, navController: NavController) {
        // Set the activity and NavController for navigation purposes.
        this.activity = activity
        this.navController = navController
    }

    fun init() {
        // Initialize the FirebaseAuth instance.
        auth = FirebaseAuth.getInstance()
    }

    fun signUp(phoneNumber: String) {
        // Initiate phone number verification process.
        if (::activity.isInitialized) {
            PhoneAuthProvider.getInstance().verifyPhoneNumber(phoneNumber, 60, TimeUnit.SECONDS, activity, object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                    // Automatic verification completed successfully.
                    Log.d("error_x", "signInWithCredential:send")
                    signInWithPhoneAuthCredential(credential)
                }

                override fun onVerificationFailed(e: FirebaseException) {
                    // Verification process encountered an error.
                    Log.d("error_x", "signInWithCredential:sending failed")
                }

                override fun onCodeSent(
                    verificationId: String,
                    token: PhoneAuthProvider.ForceResendingToken
                ) {
                    // Verification code has been sent to the user's phone.
                    storedVerificationId = verificationId
                    navController.navigate(R.id.action_signUpFragment_to_codeFragment)
                }
            })
        }
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        // Sign in with the received phone authentication credential.
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Sign in with phone authentication succeeded.
                    Log.d("SignUpViewModel", "signInWithCredential:success")
                } else {
                    // Sign in with phone authentication failed.
                    Log.w("SignUpViewModel", "signInWithCredential:failure", task.exception)
                }
            }
    }

    fun verifyCode(code: String) {
        // Verify the entered verification code.
        val credential = PhoneAuthProvider.getCredential(storedVerificationId, code)
        signInWithPhoneAuthCredential(credential)
    }
}
