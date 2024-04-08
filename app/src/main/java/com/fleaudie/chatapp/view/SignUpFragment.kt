package com.fleaudie.chatapp.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.fleaudie.chatapp.R
import com.fleaudie.chatapp.databinding.FragmentSignUpBinding
import com.fleaudie.chatapp.viewmodel.SignUpViewModel

class SignUpFragment : Fragment() {

    // SignUpFragment handles user sign-up UI and interactions.
    private lateinit var binding: FragmentSignUpBinding
    private lateinit var signUpViewModel: SignUpViewModel
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment.
        binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize NavController for fragment navigation.
        navController = Navigation.findNavController(view)

        // Initialize SignUpViewModel.
        signUpViewModel = ViewModelProvider(requireActivity())[SignUpViewModel::class.java]
        signUpViewModel.setActivity(requireActivity(), navController)
        signUpViewModel.init()

        // Set click listeners for sign-up and sign-in buttons.
        binding.btnSignUp.setOnClickListener {
            val countryCode = binding.countyCodePicker.selectedCountryCode
            val number = binding.editTextPhone.text.toString()
            val phoneNumber = "+$countryCode$number"
            Log.d("SignUpFragment", "Phone number: $phoneNumber")
            signUpViewModel.signUp(phoneNumber)
        }
        binding.txtSignIn.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_signUpFragment_to_signInFragment)
        }
    }
}
