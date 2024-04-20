package com.fleaudie.chatapp.viewmodel

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.fleaudie.chatapp.R
import com.fleaudie.chatapp.data.datasource.AuthDataSource
import com.fleaudie.chatapp.data.model.User
import com.fleaudie.chatapp.data.repository.AuthRepository
import com.fleaudie.chatapp.view.SignUpFragmentDirections
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Firebase
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthMissingActivityForRecaptchaException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import java.util.concurrent.TimeUnit

class SignUpViewModel(private var repository: AuthRepository) : ViewModel() {

    fun sendVerificationCode(fragment: Fragment, phoneNumber: String, name: String, surname: String) {
        val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            }

            override fun onVerificationFailed(exception: FirebaseException) {
                Log.e("SignUpViewModel", "Verification failed: ${exception.message}")

            }

            override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
                val action = SignUpFragmentDirections.actionSignUpFragmentToCodeFragment(verificationId)
                fragment.findNavController().navigate(action)
                val uid = FirebaseAuth.getInstance().currentUser?.uid
                if (uid != null) {
                    repository.writeUserData(phoneNumber, uid, name, surname)
                } else {
                    Log.e("OtpViewModel", "User is not authenticated")
                }
                Log.d("SignUpViewModel", "Verification code sent successfully")
            }
        }
        repository.sendVerificationCode(phoneNumber, callbacks)
    }

    fun checkPhoneNumberInDatabase(phoneNumber: String, onSuccess: () -> Unit, onFail: () -> Unit) {
        repository.checkPhoneNumberInDatabase(phoneNumber, onSuccess, onFail)
    }


}


