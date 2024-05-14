package com.fleaudie.chatapp.helpers

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.animation.AnimationUtils
import android.widget.LinearLayout
import android.widget.PopupWindow
import com.fleaudie.chatapp.R
import com.fleaudie.chatapp.databinding.PopupAccountSettingsBinding
import com.fleaudie.chatapp.databinding.PopupEditNameBinding
import com.fleaudie.chatapp.databinding.PopupEditSurnameBinding
import com.google.android.material.snackbar.Snackbar

class PopupHelper(private val context: Context) {

    fun showEditNamePopup(onSuccess: (String) -> Unit) {
        val inflater = LayoutInflater.from(context)
        val binding = PopupEditNameBinding.inflate(inflater, null, false)
        val view = binding.root

        val popupWindow = PopupWindow(
            view,
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            true
        )
        popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0)

        val upwardSlideAnimation = AnimationUtils.loadAnimation(context, R.anim.upward_slide)
        view.startAnimation(upwardSlideAnimation)

        binding.btnSaveName.setOnClickListener {
            val newName = binding.editName.text.toString()
            onSuccess(newName)
            popupWindow.dismiss()
        }

        binding.btnCancel.setOnClickListener {
            popupWindow.dismiss()
        }
    }

    fun showEditSurnamePopup(onSuccess: (String) -> Unit) {
        val inflater = LayoutInflater.from(context)
        val binding = PopupEditSurnameBinding.inflate(inflater, null, false)
        val view = binding.root

        val popupWindow = PopupWindow(
            view,
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            true
        )
        popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0)

        val upwardSlideAnimation = AnimationUtils.loadAnimation(context, R.anim.upward_slide)
        view.startAnimation(upwardSlideAnimation)

        binding.btnSaveSurname.setOnClickListener {
            val newSurname = binding.editSurname.text.toString()
            onSuccess(newSurname)
            popupWindow.dismiss()
        }

        binding.btnCancel.setOnClickListener {
            popupWindow.dismiss()
        }
    }

    fun showChangeNumberPopup(onSuccess: (String) -> Unit) {
        val inflater = LayoutInflater.from(context)
        val binding = PopupAccountSettingsBinding.inflate(inflater, null, false)
        val view = binding.root

        val popupWindow = PopupWindow(
            view,
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            true
        )
        popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0)

        val upwardSlideAnimation = AnimationUtils.loadAnimation(context, R.anim.upward_slide)
        view.startAnimation(upwardSlideAnimation)

        binding.btnNext.setOnClickListener {
            val phoneNumber = binding.editNumber.text.toString()
            if (phoneNumber.isBlank()) {
                Snackbar.make(view, "Please enter your number!", Snackbar.LENGTH_SHORT)
                    .show()
            } else {
                onSuccess(phoneNumber)
                popupWindow.dismiss()
            }
        }

        binding.btnCancelNumber.setOnClickListener {
            popupWindow.dismiss()
        }
    }

}