package com.fleaudie.chatapp.viewmodel

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.navigation.fragment.findNavController
import com.fleaudie.chatapp.data.repository.AuthRepository
import com.fleaudie.chatapp.view.SignUpFragmentDirections
import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
@HiltViewModel
class SignUpViewModel @Inject constructor(private var repository: AuthRepository) : ViewModel() {

    fun sendVerificationCode(fragment: Fragment, phoneNumber: String, name: String, surname: String) {
        val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            }

            override fun onVerificationFailed(exception: FirebaseException) {
                Log.e("SignUpViewModel", "Verification failed: ${exception.message}")

            }

            override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
                val action = SignUpFragmentDirections.actionSignUpFragmentToCodeFragment(verificationId, phoneNumber, name, surname)
                Log.e("direction","$name+$surname+$phoneNumber")
                fragment.findNavController().navigate(action)
            }
        }
        repository.sendVerificationCode(phoneNumber, callbacks)
    }

}


