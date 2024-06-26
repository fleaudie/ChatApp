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

    fun getProfileImageUrl(callback: (String?) -> Unit){
        dataSource.getProfileImageUrl(callback)
    }

    fun updateUserSurname(newSurname: String, onSuccess: () -> Unit, onFailure: (Exception) -> Unit){
        dataSource.updateUserSurname(newSurname, onSuccess, onFailure)
    }

    fun updateUserNumber(newNumber: String, onSuccess: () -> Unit, onFailure: (Exception) -> Unit){
        dataSource.updateUserNumber(newNumber, onSuccess, onFailure)
    }
}