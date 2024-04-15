package com.fleaudie.chatapp.viewmodel

import android.annotation.SuppressLint
import android.app.Activity
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.fleaudie.chatapp.R
import com.fleaudie.chatapp.util.AuthUtil
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

class SignInViewModel : ViewModel() {
    private lateinit var auth: FirebaseAuth
    private lateinit var storedVerificationId: String
    private lateinit var navController: NavController
    @SuppressLint("StaticFieldLeak")
    private lateinit var activity: Activity

    fun setActivity(activity: Activity, navController: NavController) {
        this.activity = activity
        this.navController = navController
    }

    fun initFirebaseAuth() {
        auth = FirebaseAuth.getInstance()
    }

    fun signInWithPhoneNumber(phoneNumber: String) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            phoneNumber,
            60,
            TimeUnit.SECONDS,
            activity,
            object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                    AuthUtil.signInWithPhoneAuthCredential(credential,
                        onSuccess = {

                        },
                        onFailure = {
                            Snackbar.make(activity.findViewById(android.R.id.content), "Number already exists. Want to Sign Up?", Snackbar.LENGTH_SHORT)
                                .setAction("YES") {
                                    navController.navigate(R.id.action_signInFragment_to_signUpFragment)
                                }
                                .show()
                        })
                }

                override fun onVerificationFailed(e: FirebaseException) {
                    Log.e("YourViewModel", "Verification failed: ${e.message}")
                }

                override fun onCodeSent(
                    verificationId: String,
                    token: PhoneAuthProvider.ForceResendingToken
                ) {
                    storedVerificationId = verificationId
                    navController.navigate(R.id.action_signInFragment_to_codeFragment)
                }
            })
    }

}