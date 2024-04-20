package com.fleaudie.chatapp.viewmodel

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.navigation.fragment.findNavController
import com.fleaudie.chatapp.data.repository.AuthRepository
import com.fleaudie.chatapp.view.SignInFragmentDirections
import com.fleaudie.chatapp.view.SignUpFragmentDirections
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider

class SignInViewModel(private var repository: AuthRepository) : ViewModel() {

    fun sendVerificationCode(fragment: Fragment, phoneNumber: String) {
        val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            }

            override fun onVerificationFailed(exception: FirebaseException) {
                Log.e("SignUpViewModel", "Verification failed: ${exception.message}")

            }

            override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
                val action = SignInFragmentDirections.actionSignInFragmentToCodeFragment(verificationId)
                fragment.findNavController().navigate(action)
                Log.d("SignUpViewModel", "Verification code sent successfully")
            }
        }
        repository.sendVerificationCode(phoneNumber, callbacks)
    }

    fun checkPhoneNumberInDatabase(phoneNumber: String, onSuccess: () -> Unit, onFail: () -> Unit) {
        repository.checkPhoneNumberInDatabase(phoneNumber, onSuccess, onFail)
    }

}