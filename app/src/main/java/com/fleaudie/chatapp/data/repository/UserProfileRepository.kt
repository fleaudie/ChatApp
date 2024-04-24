package com.fleaudie.chatapp.data.repository

import com.fleaudie.chatapp.data.datasource.UserProfileDataSource

class UserProfileRepository(private val dataSource: UserProfileDataSource) {
    fun uploadProfileImage(imageBytes: ByteArray, onSuccess: (String) -> Unit, onFailure: (Exception) -> Unit) {
        dataSource.uploadProfileImage(imageBytes, onSuccess, onFailure)
    }

    fun getUserData(callback: (String?, String?, String?, String?) -> Unit) {
        dataSource.getUserData(callback)
    }

    fun updateUserName(newName: String, onSuccess: () -> Unit, onFailure: (Exception) -> Unit){
        dataSource.updateUserName(newName, onSuccess, onFailure)
    }
}