package com.fleaudie.chatapp.view

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.fleaudie.chatapp.R
import com.fleaudie.chatapp.data.datasource.UserProfileDataSource
import com.fleaudie.chatapp.data.repository.UserProfileRepository
import com.fleaudie.chatapp.databinding.FragmentProfileSettingsBinding
import com.fleaudie.chatapp.helpers.PopupHelper
import com.fleaudie.chatapp.viewmodel.ProfileSettingsViewModel
import com.google.android.material.snackbar.Snackbar

class ProfileSettingsFragment : Fragment() {
    private lateinit var binding: FragmentProfileSettingsBinding
    private lateinit var viewModel: ProfileSettingsViewModel
    private val PICK_IMAGE_REQUEST = 1
    private lateinit var popupHelper: PopupHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile_settings, container, false)
        binding.profileSettings = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args: ProfileSettingsFragmentArgs by navArgs()
        binding.textViewEditName.text = args.userName
        binding.textViewEditSurname.text = args.userSurname

        popupHelper = PopupHelper(requireContext())

        loadProfileImage()
    }

    fun uploadProfilePhoto() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    @SuppressLint("Recycle")
    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            val imageUri = data.data
            val inputStream = requireContext().contentResolver.openInputStream(imageUri!!)
            val imageBytes = inputStream!!.readBytes()

            viewModel.uploadProfileImage(imageBytes,
                onSuccess = {
                    Snackbar.make(requireView(), "Profile photo uploaded successfully!", Snackbar.LENGTH_SHORT).show()
                },
                onFailure = { exception ->
                    Snackbar.make(requireView(), "Error uploading profile photo: ${exception.message}", Snackbar.LENGTH_SHORT).show()
                }
            )
        }
    }

    fun editName() {
        popupHelper.showEditNamePopup(binding.textViewEditName.text.toString()) { newName ->
            binding.textViewEditName.text = newName

            viewModel.updateUserName(newName,
                onSuccess = {
                    view?.let { it1 -> Snackbar.make(it1, "Name changed successfully!", Snackbar.LENGTH_SHORT).show() }
                }, onFailure = {
                    view?.let { it1 -> Snackbar.make(it1, "Error changing name!", Snackbar.LENGTH_SHORT).show() }
                })
        }
    }

    fun editSurname(){
        popupHelper.showEditSurnamePopup(binding.textViewEditSurname.text.toString()) { newSurname ->
            binding.textViewEditSurname.text = newSurname

            viewModel.updateUserSurname(newSurname,
                onSuccess = {
                    view?.let { it1 -> Snackbar.make(it1, "Surname changed successfully!", Snackbar.LENGTH_SHORT).show() }
                }, onFailure = {
                    view?.let { it1 -> Snackbar.make(it1, "Error changing name!", Snackbar.LENGTH_SHORT).show() }
                })
        }
    }

    private fun loadProfileImage() {
        viewModel.getProfileImageUrl { imageUrl ->
            if (!imageUrl.isNullOrEmpty()) {
                Glide.with(requireContext())
                    .load(imageUrl)
                    .into(binding.imgEditUserPhoto)
            } else {
                binding.imgEditUserPhoto.setImageResource(R.drawable.empty_profile_image)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val repository = UserProfileRepository(UserProfileDataSource())
        viewModel = ProfileSettingsViewModel(repository)
    }

}