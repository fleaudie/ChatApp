package com.fleaudie.chatapp.viewmodel

import androidx.lifecycle.ViewModel
import com.fleaudie.chatapp.data.repository.UserProfileRepository
import com.google.firebase.auth.FirebaseAuth

class UserProfileViewModel(private var repository: UserProfileRepository) : ViewModel() {
    fun logOut(){
        FirebaseAuth.getInstance().signOut()
    }

    fun getUserData(callback: (String?, String?, String?, String?) -> Unit) {
        repository.getUserData(callback)
    }
}