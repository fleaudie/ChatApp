package com.fleaudie.chatapp.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fleaudie.chatapp.data.datasource.AuthDataSource
import com.fleaudie.chatapp.data.repository.AuthRepository
import com.google.firebase.auth.PhoneAuthProvider

class CodeViewModel(private var repository: AuthRepository) : ViewModel() {

    private val _verificationResult = MutableLiveData<Boolean>()
    val verificationResult: LiveData<Boolean>
        get() = _verificationResult

    fun verifyOtp(verificationId: String, otpCode: String) {
        repository.verifyOtp(verificationId, otpCode){ success ->
            _verificationResult.postValue(success)
        }

    }
}