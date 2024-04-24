package com.fleaudie.chatapp.viewmodel

import com.fleaudie.chatapp.data.repository.UserProfileRepository

class ProfileSettingsViewModel(private var repository: UserProfileRepository) {
    fun uploadProfileImage(imageBytes: ByteArray, onSuccess: (String) -> Unit, onFailure: (Exception) -> Unit) {
        repository.uploadProfileImage(imageBytes, onSuccess, onFailure)
    }

    fun updateUserName(newName: String, onSuccess: () -> Unit, onFailure: (Exception) -> Unit){
        repository.updateUserName(newName, onSuccess, onFailure)
    }
}