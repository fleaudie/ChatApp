package com.fleaudie.chatapp.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.fleaudie.chatapp.R
import com.fleaudie.chatapp.databinding.FragmentSignInBinding
import com.fleaudie.chatapp.viewmodel.SignInViewModel
import com.google.android.material.snackbar.Snackbar


class SignInFragment : Fragment() {
    private lateinit var signInViewModel: SignInViewModel
    private lateinit var binding: FragmentSignInBinding
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_sign_in, container, false)
        binding.fragmentSignIn = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize NavController
        navController = Navigation.findNavController(requireView())

        // Initialize SignUpViewModel.
        signInViewModel = ViewModelProvider(this)[SignInViewModel::class.java]
        signInViewModel.setActivity(requireActivity(), navController)
        signInViewModel.initFirebaseAuth()
    }

    fun signIn(){
        val phNumber = binding.editTextPhone.text.toString()
        val cp = binding.countyCodePicker.selectedCountryCode

        val phoneNumber = "+$cp$phNumber"

        if (phoneNumber.isEmpty() || phoneNumber.length != 13) {
            view?.let { Snackbar.make(it, "Please enter a valid phone number", Snackbar.LENGTH_SHORT).show() }
        }

        signInViewModel.signInWithPhoneNumber(phoneNumber)
    }
}