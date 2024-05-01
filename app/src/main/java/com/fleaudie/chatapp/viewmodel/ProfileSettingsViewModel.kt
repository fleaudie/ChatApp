package com.fleaudie.chatapp.viewmodel

import androidx.lifecycle.ViewModel
import com.fleaudie.chatapp.data.repository.UserProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
@HiltViewModel
class ProfileSettingsViewModel @Inject constructor(private var repository: UserProfileRepository) : ViewModel() {
    fun uploadProfileImage(imageBytes: ByteArray, onSuccess: (String) -> Unit, onFailure: (Exception) -> Unit) {
        repository.uploadProfileImage(imageBytes, onSuccess, onFailure)
    }

    fun updateUserName(newName: String, onSuccess: () -> Unit, onFailure: (Exception) -> Unit){
        repository.updateUserName(newName, onSuccess, onFailure)
    }

    fun getProfileImageUrl(callback: (String?) -> Unit){
        repository.getProfileImageUrl(callback)
    }

    fun updateUserSurname(newSurname: String, onSuccess: () -> Unit, onFailure: (Exception) -> Unit){
        repository.updateUserSurname(newSurname, onSuccess, onFailure)
    }
}