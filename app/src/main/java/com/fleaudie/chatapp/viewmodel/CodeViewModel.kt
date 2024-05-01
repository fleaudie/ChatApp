package com.fleaudie.chatapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fleaudie.chatapp.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
@HiltViewModel
class CodeViewModel @Inject constructor(private var repository: AuthRepository) : ViewModel() {

    private val _verificationResult = MutableLiveData<Boolean>()
    val verificationResult: LiveData<Boolean>
        get() = _verificationResult

    fun verifyOtp(verificationId: String, otpCode: String) {
        repository.verifyOtp(verificationId, otpCode){ success ->
            _verificationResult.postValue(success)
        }

    }

    fun writeUserData(phoneNumber: String, uid: String, name: String, surname: String) {
        repository.writeUserData(phoneNumber,uid, name, surname)
    }
}