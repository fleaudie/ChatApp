package com.fleaudie.chatapp.data.repository

import com.fleaudie.chatapp.data.datasource.AuthDataSource
import com.google.firebase.auth.PhoneAuthProvider

class AuthRepository(private var dataSource: AuthDataSource) {
    fun sendVerificationCode(phoneNumber: String, callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks) {
        dataSource.sendVerificationCode(phoneNumber, callbacks)
    }

    fun verifyOtp(verificationId: String, otpCode: String, callback: (Boolean) -> Unit) {
        dataSource.verifyOtp(verificationId, otpCode, callback)
    }
    fun writeUserData(phoneNumber: String, uid: String, name: String, surname: String) {
        dataSource.writeUserData(phoneNumber,uid, name, surname)
    }

    fun checkPhoneNumberInDatabase(phoneNumber: String, onSuccess: () -> Unit, onFail: () -> Unit) {
        dataSource.checkPhoneNumberInDatabase(phoneNumber, onSuccess, onFail)
    }
}