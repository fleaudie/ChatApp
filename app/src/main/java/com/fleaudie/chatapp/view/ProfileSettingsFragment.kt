package com.fleaudie.chatapp.view

import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.LinearLayout
import android.widget.PopupWindow
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.navArgs
import com.fleaudie.chatapp.R
import com.fleaudie.chatapp.data.datasource.UserProfileDataSource
import com.fleaudie.chatapp.data.repository.UserProfileRepository
import com.fleaudie.chatapp.databinding.FragmentProfileSettingsBinding
import com.fleaudie.chatapp.databinding.PopupEditNameBinding
import com.fleaudie.chatapp.viewmodel.ProfileSettingsViewModel
import com.fleaudie.chatapp.viewmodel.UserProfileViewModel
import com.google.android.material.snackbar.Snackbar

class ProfileSettingsFragment : Fragment() {
    private lateinit var binding: FragmentProfileSettingsBinding
    private var popupWindow: PopupWindow? = null
    private lateinit var viewModel: ProfileSettingsViewModel

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
    }

    fun editName(){
        showPopup()
    }

    private fun showPopup() {
        val inflater = LayoutInflater.from(context)
        val binding = PopupEditNameBinding.inflate(inflater, null, false)
        val view = binding.root

        popupWindow = PopupWindow(
            view,
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            true
        )
        popupWindow?.showAtLocation(view, Gravity.BOTTOM, 0, 0)

        val upwardSlideAnimation = AnimationUtils.loadAnimation(context, R.anim.upward_slide)
        view.startAnimation(upwardSlideAnimation)

        binding.btnSaveName.setOnClickListener {
            val name = binding.editName.text.toString()
            viewModel.updateUserName(name,
                onSuccess = {
                    Snackbar.make(it, "Name changed successfully!", Snackbar.LENGTH_SHORT).show()
                    popupWindow!!.dismiss()
            }, onFailure = {
                    Snackbar.make(view, "Error changing name!", Snackbar.LENGTH_SHORT).show()
            })
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val repository = UserProfileRepository(UserProfileDataSource())
        viewModel = ProfileSettingsViewModel(repository)
    }

}