package com.fleaudie.chatapp.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.fleaudie.chatapp.util.AuthUtil
import com.google.firebase.auth.PhoneAuthProvider

class CodeViewModel : ViewModel() {
    private var verificationId: String? = null

    fun verifyCode(code: String) {
        verificationId?.let {
            val credential = PhoneAuthProvider.getCredential(it, code)
            AuthUtil.signInWithPhoneAuthCredential(credential,
                onSuccess = {
                    Log.d("CodeViewModel", "signInWithCredential:success")
                },
                onFailure = { exception ->
                    Log.w("CodeViewModel", "signInWithCredential:failure", exception)
                }
            )
        } ?: run {
            Log.e("CodeViewModel", "Verification ID is not initialized")
        }
    }
}