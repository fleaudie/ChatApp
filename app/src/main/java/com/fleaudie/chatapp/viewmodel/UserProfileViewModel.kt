package com.fleaudie.chatapp.viewmodel

import androidx.lifecycle.ViewModel
import com.fleaudie.chatapp.data.repository.UserProfileRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UserProfileViewModel @Inject constructor(private var repository: UserProfileRepository) : ViewModel() {
    fun logOut(){
        FirebaseAuth.getInstance().signOut()
    }

    fun getUserData(callback: (String?, String?, String?, String?) -> Unit) {
        repository.getUserData(callback)
    }

    fun getProfileImageUrl(callback: (String?) -> Unit){
        repository.getProfileImageUrl(callback)
    }

    fun updateUserNumber(newNumber: String, onSuccess: () -> Unit, onFailure: (Exception) -> Unit){
        repository.updateUserNumber(newNumber, onSuccess, onFailure)
    }
}