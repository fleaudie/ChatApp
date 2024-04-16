package com.fleaudie.chatapp.viewmodel

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class UserProfileViewModel : ViewModel() {
    fun logOut(){
        FirebaseAuth.getInstance().signOut()
    }
}