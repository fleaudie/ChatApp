package com.fleaudie.chatapp.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.fleaudie.chatapp.R
import com.fleaudie.chatapp.data.datasource.AuthDataSource
import com.fleaudie.chatapp.data.repository.AuthRepository
import com.fleaudie.chatapp.databinding.FragmentSignInBinding
import com.fleaudie.chatapp.viewmodel.SignInViewModel
import com.fleaudie.chatapp.viewmodel.SignUpViewModel
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
        navController = Navigation.findNavController(view)

    }

    fun signIn(){
        val number = binding.editTextPhone.text.toString()
        val countryPicker = binding.countyCodePicker.selectedCountryCode.toString()
        val phoneNumber = "+$countryPicker$number"

        signInViewModel.checkPhoneNumberInDatabase(phoneNumber, {
            view?.let {
                Snackbar.make(it, "Phone number doesn't exist. Please sign up.",  Snackbar.LENGTH_SHORT)
                    .setAction("Yes"){
                        navController.navigate(R.id.action_signInFragment_to_signUpFragment)
                    }
                    .show()
            }
        }, {
            signInViewModel.sendVerificationCode(this, phoneNumber)
        })
    }

    fun signUp(){
        navController.navigate(R.id.action_signInFragment_to_signUpFragment)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val authRepository = AuthRepository(AuthDataSource(requireContext()))
        signInViewModel = SignInViewModel(authRepository)
    }
}