package com.fleaudie.chatapp.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.fleaudie.chatapp.R
import com.fleaudie.chatapp.data.datasource.UserProfileDataSource
import com.fleaudie.chatapp.data.repository.UserProfileRepository
import com.fleaudie.chatapp.databinding.FragmentUserProfileBinding
import com.fleaudie.chatapp.viewmodel.UserProfileViewModel

class UserProfileFragment : Fragment() {
    private lateinit var binding: FragmentUserProfileBinding
    private lateinit var viewModel: UserProfileViewModel
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_user_profile, container, false)
        binding.userProfileObject = this
        val anim = AnimationUtils.loadAnimation(context, R.anim.fade_in)
        binding.root.startAnimation(anim)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

        viewModel.getUserData { name, surname, phone, uid ->
            binding.txtUserName.text = "$name $surname"
            binding.txtUserNumber.text = "$phone"
            Log.d("UserProfile", "$uid")

            loadProfileImage()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val repository = UserProfileRepository(UserProfileDataSource())
        viewModel = UserProfileViewModel(repository)
    }

    fun profileSettings() {
        viewModel.getUserData { name, surname, _, uid ->
            if (name != null && surname != null && uid != null) {
                val action = UserProfileFragmentDirections.actionUserProfileFragmentToProfileSettingsFragment(
                    name, surname, uid
                )
                navController.navigate(action)
            } else {
                Log.e("UserProfileFragment", "Error: Name, surname or uid is null")
            }
        }
    }

    private fun loadProfileImage() {
        viewModel.getProfileImageUrl { imageUrl ->
            if (!imageUrl.isNullOrEmpty()) {
                Glide.with(requireContext())
                    .load(imageUrl)
                    .into(binding.imgUserPhoto)
            } else {
                binding.imgUserPhoto.setImageResource(R.drawable.empty_profile_image)
            }
        }
    }

    fun signOut(){
        viewModel.logOut()
        view?.let { Navigation.findNavController(it).navigate(R.id.action_userProfileFragment_to_signUpFragment) }
    }
}