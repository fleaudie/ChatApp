package com.fleaudie.chatapp.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.fleaudie.chatapp.R
import com.fleaudie.chatapp.databinding.FragmentUserProfileBinding
import com.fleaudie.chatapp.viewmodel.UserProfileViewModel

class UserProfileFragment : Fragment() {
    private lateinit var binding: FragmentUserProfileBinding
    private lateinit var userProfileViewModel: UserProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_user_profile, container, false)
        binding.userProfileObject = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userProfileViewModel = ViewModelProvider(this)[UserProfileViewModel::class.java]
    }

    fun signOut(){
        userProfileViewModel.logOut()
        view?.let { Navigation.findNavController(it).navigate(R.id.action_userProfileFragment_to_signInFragment) }
    }
}